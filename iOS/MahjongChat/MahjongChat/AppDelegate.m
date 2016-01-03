//
//  AppDelegate.m
//  MahjongChat
//
//  Created by Duke on 2015/10/15.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "AppDelegate.h"
#import "LoginViewController.h"
#import "SingInViewController.h"
#import <RongIMKit/RongIMKit.h>
#import "APIConn.h"
@interface AppDelegate ()
@property (strong, nonatomic) LoginViewController *loginViewController;
@property (strong, nonatomic) SingInViewController *SingInViewController;
@end

@implementation AppDelegate
{
    UIView *pauseView;
}


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    _mainViewController = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
    _mainNavi=[[UINavigationController alloc] initWithRootViewController:_mainViewController];
    [_mainNavi.navigationBar setTranslucent:NO];
    [_mainNavi.navigationBar setOpaque:YES];
    
    
    _loginViewController = [[LoginViewController alloc] initWithNibName:@"LoginViewController" bundle:nil];
    _SingInViewController = [[SingInViewController alloc] initWithNibName:@"SingInViewController" bundle:nil];
    
    self.window.rootViewController=_loginViewController;
    [self.window makeKeyAndVisible];
    
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
//------------------------------------------------------------
    [[RCIM sharedRCIM] initWithAppKey:@"25wehl3uwk9bw" ];
    [[RCIM sharedRCIM] setConnectionStatusDelegate:self];
    
    
//---------------------------------------------------------------------------
//自動登入
    if (([[NSUserDefaults standardUserDefaults] valueForKey:@"account_login"] && [[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"]) && ![[[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"]isEqualToString:@""]) {
        APIConn *conn=[[APIConn alloc] init];
        conn.apiDelegate=self;
        [conn verify:@{@"username":[[NSUserDefaults standardUserDefaults] valueForKey:@"account_login"],@"password":[[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"]}];
    }
//---------------------------------------------------------------------------
    
    /**
     * 推送处理1
     */
    if ([application
         respondsToSelector:@selector(registerUserNotificationSettings:)]) {
        UIUserNotificationSettings *settings = [UIUserNotificationSettings
                                                settingsForTypes:(UIUserNotificationTypeBadge |
                                                                  UIUserNotificationTypeSound |
                                                                  UIUserNotificationTypeAlert)
                                                categories:nil];
        [application registerUserNotificationSettings:settings];
    } else {
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge |
        UIRemoteNotificationTypeAlert |
        UIRemoteNotificationTypeSound;
        [application registerForRemoteNotificationTypes:myTypes];
    }


    return YES;

}
- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    NSString *token =
    [[[[deviceToken description] stringByReplacingOccurrencesOfString:@"<"
                                                           withString:@""]
      stringByReplacingOccurrencesOfString:@">"
      withString:@""]
     stringByReplacingOccurrencesOfString:@" "
     withString:@""];
    
    [[RCIMClient sharedRCIMClient] setDeviceToken:token];
}
- (void)onRCIMConnectionStatusChanged:(RCConnectionStatus)status {
    if (status == ConnectionStatus_KICKED_OFFLINE_BY_OTHER_CLIENT) {
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle:@"提示"
                              message:@"您"
                              @"的帐号在别的设备上登录，您被迫下线！"
                              delegate:nil
                              cancelButtonTitle:@"知道了"
                              otherButtonTitles:nil, nil];
        [alert show];
        
        UINavigationController *_navi =
        [[UINavigationController alloc] initWithRootViewController:_mainViewController];
        self.window.rootViewController = _navi;
    }
}


//======================================================================================================================================================
- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

-(void)loginToMain{
    CATransition *transition=[CATransition animation];
    [transition setDelegate:self];
    [transition setDuration:0.5f];
    [transition setType:kCATransitionFade];
    [self.window removeFromSuperview];
    [self.window.layer addAnimation:transition forKey:@"MAIN"];
    self.window.rootViewController=_mainNavi;
}
-(void)loginToSingIn{
    CATransition *transition=[CATransition animation];
    [transition setDelegate:self];
    [transition setDuration:0.5f];
    [transition setType:kCATransitionFade];
    [self.window removeFromSuperview];
    [self.window.layer addAnimation:transition forKey:@"MAIN"];
    self.window.rootViewController=_SingInViewController;
}
-(void)initPauseView:(NSString*)status{
    NSLog(@"view show");
    pauseView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
    UIView *alert = [[UIView alloc]initWithFrame:CGRectMake(WIDTH/2 - 135*RATIO, 100*RATIO, 270*RATIO, 150*RATIO)];
    [alert.layer setBorderColor:[MAIN_COLOR CGColor]];
    [alert.layer setBorderWidth:3.2f];
    [alert.layer setCornerRadius:40*RATIO];
    alert.backgroundColor = [UIColor whiteColor];
    [alert setClipsToBounds:YES];
    
    UILabel *lab = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 270*RATIO, 50*RATIO)];
    lab.backgroundColor = MAIN_COLOR;
    
    UIImageView *logoimg = [[UIImageView alloc]initWithFrame:CGRectMake(115*RATIO, 5*RATIO, 40*RATIO,40*RATIO)];
    logoimg.contentMode = UIViewContentModeScaleAspectFill;
    logoimg.image = [UIImage imageNamed:@"logo.png"];
    
    UILabel *statusView = [[UILabel alloc]initWithFrame:CGRectMake(0, 90*RATIO, 270*RATIO, 30*RATIO)];
    statusView.text = status;
    //[statusView sizeToFit];
    statusView.textAlignment = NSTextAlignmentCenter;
    [statusView setClipsToBounds:YES];
    [statusView setFont:[UIFont systemFontOfSize:15]];
    
    statusView.textColor = [UIColor blackColor];
    
    
    [alert addSubview:lab];
    [alert addSubview:logoimg];
    [alert addSubview:statusView];
    
    [pauseView addSubview:alert];
    
    UIColor *line_grayColor = [UIColor colorWithWhite:0.6 alpha:0.4f];
    pauseView.backgroundColor = line_grayColor;
    [self.window addSubview:pauseView];
}
-(void)dismissPauseView{
    [pauseView removeFromSuperview];
}
#pragma mark - API Delegate
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)networkError:(NSString *)err{
    //----------------------------------------------------------------------------------------------------------------------------------------
    NSLog(@"networkerror");
    [self dismissPauseView];
}
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)verifyFinished:(NSDictionary *)dic{                                                                                    //後台登入完成
    //----------------------------------------------------------------------------------------------------------------------------------------
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        NSLog(@"%@",dic);
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:[dic objectForKey:@"message"] forKey:@"accountToken"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"num"] forKey:@"accountID"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"name"] forKey:@"accountName"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"email"] forKey:@"accountEmail"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"username"] forKey:@"accountUsername"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"password"] forKey:@"accountPassword"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"gender"] forKey:@"accountGender"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"age"] forKey:@"accountAge"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"location_x"] forKey:@"accountLocationX"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"location_y"] forKey:@"accountLocationY"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"photo"] forKey:@"accountPhoto"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"level"] forKey:@"accountLevel"];
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"dis"] forKey:@"accountDistance"];
        NSLog(@"login token = %@",[dic objectForKey:@"message"]);
        [defaults synchronize];
        NSLog(@"login success");
        [self loginRongCloud];
        [self loginToMain];
        [self dismissPauseView];
    }else{
        NSLog(@"login fail");
    }
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
        [self dismissPauseView];
        NSLog(@"login error status: %ld.", (long)status);
    } tokenIncorrect:^{
        [self dismissPauseView];
        NSLog(@"token無效，請確保生成token 使用的appkey和初始化時的appkey一致");
    }];
}

@end
