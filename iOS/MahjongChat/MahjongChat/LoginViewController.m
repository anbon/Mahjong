//
//  LoginViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/11/18.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "LoginViewController.h"
#import "APIConn.h"
#import "AppDelegate.h"
#import "ProgressHUD.h"
@interface LoginViewController ()

@end

@implementation LoginViewController{
    AppDelegate *appDelegate;
}

#pragma mark - life Cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    [self initByDuke];
    [self.view setBackgroundColor:MAIN_COLOR];
    appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    NSLog(@"login page");
    self.view.translatesAutoresizingMaskIntoConstraints = NO;
//    APIConn *conn=[[APIConn alloc] init];
//    conn.apiDelegate=self;
//    [conn verify:@{@"username":@"3213123",@"password":@"32132131"}];
   
}

- (void)didReceiveMemoryWarning{
    [super didReceiveMemoryWarning];
}
#pragma mark - API Delegate
-(void)networkError:(NSString *)err{
    NSLog(@"netWorkError");
}
-(void)verifyFinished:(NSDictionary *)dic{
    NSLog(@"%@",dic);
    NSLog(@"verfysuccess");
    [[NSUserDefaults standardUserDefaults] setValue:[dic objectForKey:@"message"] forKey:@"User_Token"];
}

#pragma mark - event response
-(void)login{
    [appDelegate initPauseView:@"請稍候.."];
    [appDelegate loginToSingIn];
}
#pragma mark - getters & setters
-(void)initByDuke{
    UIImageView *logo = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH/2-60*RATIO, 130*RATIO, 120*RATIO, 120*RATIO)];
    [logo setImage:[UIImage imageNamed:@"logo.png"]];
    [logo setContentMode:UIViewContentModeScaleAspectFit];
    [self.view addSubview:logo];
    
    UIButton *registerBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH/2-150*RATIO, HEIGHT-110*RATIO, 120*RATIO, 30*RATIO)];
    [registerBtn setBackgroundImage:[UIImage imageNamed:@"register.png"] forState:UIControlStateNormal];
    [self.view addSubview:registerBtn];
    
    UIButton *loginBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH/2+30*RATIO, HEIGHT-110*RATIO, 120*RATIO, 30*RATIO)];
    [loginBtn setBackgroundImage:[UIImage imageNamed:@"signin.png"] forState:UIControlStateNormal];
    [self.view addSubview:loginBtn];
    [loginBtn addTarget:self action:@selector(login) forControlEvents:UIControlEventTouchUpInside];
    
    UILabel *Mahjong = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH/2-115*RATIO, HEIGHT-180*RATIO, 230*RATIO, 40*RATIO)];
    [Mahjong setText:@"M a h j o n g"];
    [Mahjong setTextColor:[UIColor whiteColor]];
    [Mahjong setTextAlignment:NSTextAlignmentCenter];
    [Mahjong setFont:[UIFont systemFontOfSize:40*RATIO]];
    [Mahjong sizeToFit];
    [self.view addSubview:Mahjong];
}

@end
