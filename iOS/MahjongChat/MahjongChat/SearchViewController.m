//
//  SearchViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/11/22.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "SearchViewController.h"
#import "AppDelegate.h"
#import "SearchReslutViewController.h"
@interface SearchViewController ()

@end

@implementation SearchViewController
{
    UITextField *search;
    AppDelegate *appDelegate;
}

#pragma mark - life Cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [self initByDuke];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
#pragma mark - API Delegate
-(void)searchUserFinished:(NSDictionary *)dic{
    NSLog(@"member dic = %@",dic);
    NSString *msg = [dic objectForKey:@"message"];
    [appDelegate dismissPauseView];
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"num"] forKey:@"searchResultNum"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"name"] forKey:@"searchResultName"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"email"] forKey:@"searchResultEmail"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"username"] forKey:@"searchResultUsername"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"password"] forKey:@"searchResultPassword"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"gender"] forKey:@"searchResultGender"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"age"] forKey:@"searchResultAge"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"photo"] forKey:@"searchResultPhoto"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"level"] forKey:@"searchResultLevel"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"rate"] forKey:@"searchResultRate"];
        [defaults setObject:[dic objectForKey:@"message"] forKey:@"searchResult"];
        if ([[[dic objectForKey:@"message"] objectForKey:@"follow"]isEqualToString:@"1"]) {//追隨
            [defaults setObject:@"1" forKey:@"followOrNot"];
        }else{
            [defaults setObject:@"0" forKey:@"followOrNot"];
        }
        if ([[[dic objectForKey:@"message"] objectForKey:@"Rooming"]isEqualToString:@"1"]) {//有開房
            [defaults setObject:@"1" forKey:@"RoomingOrNot"];
        }else{
            [defaults setObject:@"0" forKey:@"RoomingOrNot"];
        }
        [defaults setObject:[dic objectForKey:@"RoomMessage"] forKey:@"selectChatRoom"];
        [defaults synchronize];
        
        
        SearchReslutViewController *result = [SearchReslutViewController new];
        [self.navigationController pushViewController:result animated:YES];
    }else if([[dic objectForKey:@"status"]isEqualToString:@"0"]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"查詢失敗" message:@"" delegate:nil cancelButtonTitle:@"我知道了" otherButtonTitles:nil];
        
        [alert show];
        
        SearchReslutViewController *result = [SearchReslutViewController new];
        [self.navigationController pushViewController:result animated:YES];
    }
}
#pragma mark - event response
-(void)searchAction{
    if ([search.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"請輸入欲查詢之會員名稱" message:@"" delegate:nil cancelButtonTitle:@"確定" otherButtonTitles:nil];
        
        [alert show];
    }else{
        [appDelegate initPauseView:@"查詢中..請稍候.."];
        APIConn *conn = [APIConn new];
        conn.apiDelegate = self;
        [conn searchUser:@{@"username":search.text,@"accountID":[[NSUserDefaults standardUserDefaults] objectForKey:@"accountID"]}];
   
    }
    //需修正
    
}
-(void)tapView:(UITapGestureRecognizer*)tap{//UITextField search
    [search resignFirstResponder];
    
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}
#pragma mark - getters & setters
-(void)initByDuke{
    search = [[UITextField alloc]initWithFrame:CGRectMake(WIDTH/2-150*RATIO, 64+20*RATIO, 300*RATIO, 50*RATIO)];
    search.placeholder = @"輸入ID";
    search.delegate = self;
    [search setLeftViewMode:UITextFieldViewModeAlways];
    [search.layer setBorderColor:[MAIN_COLOR CGColor]];
    [search.layer setBorderWidth:1.5f];
    [search.layer setCornerRadius:25*RATIO];
    UIImageView *imgView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 40*RATIO+10*RATIO, 50*RATIO)];
    imgView.contentMode = UIViewContentModeScaleAspectFill;
    search.leftView = imgView;
    
    UIButton *checkBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH/2-60*RATIO, 64+120*RATIO , 120*RATIO, 50*RATIO)];
    [checkBtn setTitle:@"確認" forState:UIControlStateNormal];
    checkBtn.titleLabel.textColor = [UIColor whiteColor];
    [checkBtn.titleLabel setFont:[UIFont systemFontOfSize:30*RATIO]];
    [checkBtn.layer setBorderWidth:0.8f];
    [checkBtn.layer setCornerRadius:25*RATIO];
    [checkBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
    checkBtn.backgroundColor = MAIN_COLOR;
    [checkBtn.titleLabel setFont:[UIFont systemFontOfSize:20]];
    [checkBtn addTarget:self action:@selector(searchAction) forControlEvents:UIControlEventTouchUpInside];
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapView:)];

    [self.view addGestureRecognizer:tap];
    [self.view addSubview:checkBtn];
    [self.view addSubview:search];
}




@end
