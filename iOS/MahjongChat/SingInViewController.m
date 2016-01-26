//
//  SingInViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/11/20.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "SingInViewController.h"
#import "AppDelegate.h"
#import "APIConn.h"
#import "ProgressHUD.h"
@interface SingInViewController ()

@end

@implementation SingInViewController
{
    UITextField *account;
    UITextField *pwd;
    AppDelegate *appDelegate;
    UIView *pauseView;
}


#pragma mark - life Cycle
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)viewDidLoad {
//----------------------------------------------------------------------------------------------------------------------------------------
    [super viewDidLoad];
    [self initByDuke];
    self.view.translatesAutoresizingMaskIntoConstraints = NO;
    appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [self.view setBackgroundColor:MAIN_COLOR];
    [appDelegate dismissPauseView];
}
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)didReceiveMemoryWarning{
//----------------------------------------------------------------------------------------------------------------------------------------
    [super didReceiveMemoryWarning];
}
#pragma mark - API Delegate
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)networkError:(NSString *)err{
//----------------------------------------------------------------------------------------------------------------------------------------
    NSLog(@"networkerror");
    [appDelegate dismissPauseView];
}
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)verifyFinished:(NSDictionary *)dic{                                                                                    //後台登入完成
//----------------------------------------------------------------------------------------------------------------------------------------
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        NSLog(@"%@",dic);
        
        [appDelegate loginToMain];
        [appDelegate dismissPauseView];
        [[NSUserDefaults standardUserDefaults] setValue:account.text forKey:@"account_login"];
        [[NSUserDefaults standardUserDefaults] setValue:pwd.text forKey:@"pwd_login"];
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:[dic objectForKey:@"message"] forKey:@"accountToken"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"num"] forKey:@"accountID"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"name"] forKey:@"accountName"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"email"] forKey:@"accountEmail"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"username"] forKey:@"accountUsername"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"password"] forKey:@"accountPassword"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"gender"] forKey:@"accountGender"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"age"] forKey:@"accountAge"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"photo"] forKey:@"accountPhoto"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"level"] forKey:@"accountLevel"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"rate"] forKey:@"accountRate"];
        
        
        
        [defaults synchronize];
        [self loginRongCloud];
        NSLog(@"login success");
    }else{
        NSLog(@"login fail");
        [appDelegate dismissPauseView];
    }
}
#pragma mark - event response
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)login
//----------------------------------------------------------------------------------------------------------------------------------------
{
    [appDelegate initPauseView:@"登入中..請稍候.."];
    if ([account.text isEqualToString:@""] || [pwd.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"登入失敗!!" message:@"請輸入帳號密碼" delegate:nil cancelButtonTitle:@"我知道了" otherButtonTitles:nil];
        
        [alert show];
        [appDelegate dismissPauseView];
    }else{
        APIConn *conn=[[APIConn alloc] init];
        conn.apiDelegate=self;
        [conn verify:@{@"username":account.text,@"password":pwd.text}];
    }
    
    NSLog(@"viewtest");
    
    
}
//----------------------------------------------------------------------------------------------------------------------------------------
- (BOOL)textFieldShouldReturn:(UITextField *)textField
//----------------------------------------------------------------------------------------------------------------------------------------
{
    [textField resignFirstResponder];
    return YES;
}

-(void)cancelview{
    NSLog(@"hahaha..oh");
}

-(void)tapView:(UITapGestureRecognizer*)tap{//UITextField account,pwd
    [account resignFirstResponder];
    [pwd resignFirstResponder];
    
}
#pragma mark - getters & setters
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)initByDuke{
//----------------------------------------------------------------------------------------------------------------------------------------
    account = [[UITextField alloc]initWithFrame:CGRectMake(WIDTH/2-175*RATIO, 120*RATIO, 350*RATIO, 40*RATIO)];
    account.placeholder = @"請輸入手機號碼";
    [account setTextColor:[UIColor whiteColor]];
    [account setLeftViewMode:UITextFieldViewModeAlways];
    UIImageView *imgView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 40*RATIO+10*RATIO, 40*RATIO)];
    UIImageView *searchView = [[UIImageView alloc]initWithFrame:CGRectMake(10*RATIO, 40*RATIO*0.2, 40*RATIO*0.6, 40*RATIO*0.6)];
    imgView.contentMode = UIViewContentModeScaleAspectFill;
    searchView.contentMode = UIViewContentModeScaleAspectFill;
    searchView.image = [UIImage imageNamed:@"dot.png"];
    [account setDelegate:self];
    account.returnKeyType = UIReturnKeyDone;
    
    account.leftView= imgView;
    [account addSubview:searchView];
    
    pwd = [[UITextField alloc]initWithFrame:CGRectMake(WIDTH/2-175*RATIO, 180*RATIO, 350*RATIO, 40*RATIO)];
    pwd.placeholder = @"請輸入密碼";
    [pwd setTextColor:[UIColor whiteColor]];
    [pwd setLeftViewMode:UITextFieldViewModeAlways];
    UIImageView *imgView1 = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 40*RATIO+10*RATIO, 40*RATIO)];
    UIImageView *searchView1 = [[UIImageView alloc]initWithFrame:CGRectMake(10*RATIO, 40*RATIO*0.2, 40*RATIO*0.6, 40*RATIO*0.6)];
    imgView1.contentMode = UIViewContentModeScaleAspectFill;
    searchView1.contentMode = UIViewContentModeScaleAspectFill;
    searchView1.image = [UIImage imageNamed:@"dot.png"];
    [pwd setDelegate:self];
    pwd.returnKeyType = UIReturnKeyDone;
    
    pwd.leftView= imgView1;
    [pwd addSubview:searchView1];
    
    UIView *line = [[UIView alloc]initWithFrame:CGRectMake(WIDTH/2-175*RATIO, 161*RATIO, 350*RATIO, 2*RATIO)];
    [line setBackgroundColor:[UIColor whiteColor]];
    UIView *line1 = [[UIView alloc]initWithFrame:CGRectMake(WIDTH/2-175*RATIO, 221*RATIO, 350*RATIO, 2*RATIO)];
    [line1 setBackgroundColor:[UIColor whiteColor]];
    
    UIButton *checkBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH/2-60*RATIO, 260*RATIO , 120*RATIO, 50*RATIO)];
    [checkBtn setTitle:@"確認" forState:UIControlStateNormal];
    checkBtn.titleLabel.textColor = MAIN_COLOR;
    [checkBtn.titleLabel setFont:[UIFont systemFontOfSize:30*RATIO]];
    [checkBtn.layer setBorderWidth:0.8f];
    [checkBtn.layer setCornerRadius:25*RATIO];
    [checkBtn.layer setBorderColor:[[UIColor whiteColor] CGColor]];
    [checkBtn addTarget:self action:@selector(login) forControlEvents:UIControlEventTouchUpInside];
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapView:)];
    
    [self.view addGestureRecognizer:tap];
    [self.view addSubview:account];
    [self.view addSubview:line];
    [self.view addSubview:pwd];
    [self.view addSubview:line1];
    [self.view addSubview:checkBtn];
    
    
}
#pragma mark - rong
-(void)loginRongCloud
{
    //登录融云服务器,开始阶段可以先从融云API调试网站获取，之后token需要通过服务器到融云服务器取。
    NSString*token=[[NSUserDefaults standardUserDefaults] valueForKey:@"accountToken"];
    [[RCIM sharedRCIM] connectWithToken:token success:^(NSString *userId) {
        
        
        
        //设置用户信息提供者,页面展现的用户头像及昵称都会从此代理取
        [[RCIM sharedRCIM] setUserInfoDataSource:self];
        
        
        NSLog(@"Login successfully with userId: %@.", userId);
        dispatch_async(dispatch_get_main_queue(), ^{
            
        });
        
    } error:^(RCConnectErrorCode status) {
        [appDelegate dismissPauseView];
        NSLog(@"login error status: %ld.", (long)status);
    } tokenIncorrect:^{
        [appDelegate dismissPauseView];
        NSLog(@"token無效，請確保生成token 使用的appkey和初始化時的appkey一致");
    }];
}
@end
