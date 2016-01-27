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
#import "BPush.h"
@interface AppDelegate ()
@property (strong, nonatomic) LoginViewController *loginViewController;
@property (strong, nonatomic) SingInViewController *SingInViewController;
@end

@implementation AppDelegate
{
    UIView *pauseView;
    NSString *userIDDDD,*userName,*userUrl;
}


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    location = [[CLLocationManager alloc]init];
    location.delegate = self;
    location.allowsBackgroundLocationUpdates = YES;
    [location requestAlwaysAuthorization];
    location.desiredAccuracy = kCLLocationAccuracyBest;
    [location startUpdatingLocation];
    
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
    [[RCIM sharedRCIM] setUserInfoDataSource:self];
    [[RCIM sharedRCIM] setGroupInfoDataSource:self];
    
    
  
    /**
     * 推送处理1
     */
    if ([application
         respondsToSelector:@selector(registerUserNotificationSettings:)]) {
        //注册推送, 用于iOS8以及iOS8之后的系统
        UIUserNotificationSettings *settings = [UIUserNotificationSettings
                                                settingsForTypes:(UIUserNotificationTypeBadge |
                                                                  UIUserNotificationTypeSound |
                                                                  UIUserNotificationTypeAlert)
                                                categories:nil];
        [application registerUserNotificationSettings:settings];
    } else {
        //注册推送，用于iOS8之前的系统
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge |
        UIRemoteNotificationTypeAlert |
        UIRemoteNotificationTypeSound;
        [application registerForRemoteNotificationTypes:myTypes];
    }

//百度推播
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0) {
        UIUserNotificationType myTypes = UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
        
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:myTypes categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    }else {
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge|UIRemoteNotificationTypeAlert|UIRemoteNotificationTypeSound;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:myTypes];
    }
    [BPush registerChannel:launchOptions apiKey:@"TuEclAqQLeIYezkC5ejSGnee" pushMode:BPushModeProduction withFirstAction:nil withSecondAction:nil withCategory:nil isDebug:YES];
    // App 是用户点击推送消息启动
    NSDictionary *userInfo = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
    NSLog(@"useinfo=%@",userInfo);
    if (userInfo) {
        NSLog(@"从消息启动:%@",userInfo);
        [BPush handleNotification:userInfo];
    }
    //角标清0
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    
    
    [BPush bindChannelWithCompleteHandler:^(id result,NSError *error){
        NSLog(@"bphander=%@",result);
        NSString *channel = [result objectForKey:@"channel_id"];
        NSLog(@"chanl=%@",channel);
        NSLog(@"RRRRRRRRRRRRRRRRRRRRRRRTTTTTTTTTTTTTT");
        [[NSUserDefaults standardUserDefaults]setObject:channel forKey:@"channel_ID"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        //---------------------------------------------------------------------------
        //自動登入
        NSLog(@"RRRRRRRRRRRRRRRRRRRRRRR");
        if (([[NSUserDefaults standardUserDefaults] valueForKey:@"account_login"] && [[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"]) && ![[[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"]isEqualToString:@""]) {
            APIConn *conn=[[APIConn alloc] init];
            conn.apiDelegate=self;
            [conn verify:@{@"username":[[NSUserDefaults standardUserDefaults] valueForKey:@"account_login"],@"password":[[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"],@"channel":[[NSUserDefaults standardUserDefaults] objectForKey:@"channel_ID"]}];
        }
        
        //---------------------------------------------------------------------------
        if (error) {
            [[NSUserDefaults standardUserDefaults]setObject:@"" forKey:@"channel_ID"];
            [[NSUserDefaults standardUserDefaults]synchronize];
        }
    }];

    
    
    
    return YES;

}

#pragma mark - Push Notification

#ifdef __IPHONE_8_0

- (void)application:(UIApplication *)application handleActionWithIdentifier:(NSString *)identifier forRemoteNotification:(NSDictionary *)userInfo completionHandler:(void(^)())completionHandler
{
    //handle the actions
    if ([identifier isEqualToString:@"declineAction"]){
        NSLog(@"decline");
    }
    else if ([identifier isEqualToString:@"answerAction"]){
        NSLog(@"answer");
    }
}
#endif

- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    NSString *token =
    [[[[deviceToken description] stringByReplacingOccurrencesOfString:@"<"
                                                           withString:@""]
      stringByReplacingOccurrencesOfString:@">"
      withString:@""]
     stringByReplacingOccurrencesOfString:@" "
     withString:@""];
    
    NSLog(@"test:%@",deviceToken);
    [BPush registerDeviceToken:deviceToken];
    [BPush bindChannelWithCompleteHandler:^(id result, NSError *error) {
        // 需要在绑定成功后进行 settag listtag deletetag unbind 操作否则会失败
        if (result) {
            [BPush setTag:@"DukeLiu" withCompleteHandler:^(id result, NSError *error) {
                if (result) {
//                    [BPush bindChannelWithCompleteHandler:^(id result,NSError *error){
//                        NSLog(@"bphander=%@",result);
//                        NSString *channel = [result objectForKey:@"channel_id"];
//                        NSLog(@"chanl=%@",channel);
//                        NSLog(@"RRRRRRRRRRRRRRRRRRRRRRRTTTTTTTTTTTTTT");
//                        [[NSUserDefaults standardUserDefaults]setObject:channel forKey:@"channel_ID"];
//                        [[NSUserDefaults standardUserDefaults]synchronize];
//                        //---------------------------------------------------------------------------
//                        //自動登入
//                        NSLog(@"RRRRRRRRRRRRRRRRRRRRRRR");
//                        if (([[NSUserDefaults standardUserDefaults] valueForKey:@"account_login"] && [[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"]) && ![[[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"]isEqualToString:@""]) {
//                            APIConn *conn=[[APIConn alloc] init];
//                            conn.apiDelegate=self;
//                            [conn verify:@{@"username":[[NSUserDefaults standardUserDefaults] valueForKey:@"account_login"],@"password":[[NSUserDefaults standardUserDefaults] valueForKey:@"pwd_login"],@"channel":[[NSUserDefaults standardUserDefaults] objectForKey:@"channel_ID"]}];
//                        }
//                        //---------------------------------------------------------------------------
//                    }];
                    NSLog(@"设置tag成功");
                }
                if (error) {
                    [[NSUserDefaults standardUserDefaults]setObject:@"" forKey:@"channel_ID"];
                    [[NSUserDefaults standardUserDefaults]synchronize];
                }
            }];
        }
    }];
    
    [[RCIMClient sharedRCIMClient] setDeviceToken:token];
}
- (void)onRCIMConnectionStatusChanged:(RCConnectionStatus)status {
    if (status == ConnectionStatus_KICKED_OFFLINE_BY_OTHER_CLIENT) {
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle:@"提示"
                              message:@"您"
                              @"的帳號在别的裝置上登入，將為您轉回登入頁面"
                              delegate:nil
                              cancelButtonTitle:@"知道了"
                              otherButtonTitles:nil, nil];
        [alert show];
        
//        UINavigationController *_navi =
//        [[UINavigationController alloc] initWithRootViewController:_mainViewController];
//        self.window.rootViewController = _navi;
        self.window.rootViewController=_loginViewController;
    }
}
//

//===================
-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler
{
    NSLog(@"********** iOS7.0之后 background **********");
    // 应用在前台 或者后台开启状态下，不跳转页面，让用户选择。
    if (application.applicationState == UIApplicationStateActive || application.applicationState == UIApplicationStateBackground) {
        NSLog(@"acitve or background");
        UIAlertView *alertView =[[UIAlertView alloc]initWithTitle:@"收到一条消息" message:userInfo[@"aps"][@"alert"] delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [alertView show];
    }
    else//杀死状态下，直接跳转到跳转页面。
    {
        CATransition *transition=[CATransition animation];
        [transition setDelegate:self];
        [transition setDuration:0.5f];
        [transition setType:kCATransitionFade];
        [self.window removeFromSuperview];
        [self.window.layer addAnimation:transition forKey:@"MAIN"];
        self.window.rootViewController=_mainNavi;
    }
    
//    [self.viewController addLogString:[NSString stringWithFormat:@"backgroud : %@",userInfo]];
    completionHandler(UIBackgroundFetchResultNewData);
    NSLog(@"backgroud : %@",userInfo);

}
// 在 iOS8 系统中，还需要添加这个方法。通过新的 API 注册推送服务
- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{
    
    [application registerForRemoteNotifications];
    
    
}
// 当 DeviceToken 获取失败时，系统会回调此方法
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    NSLog(@"DeviceToken 获取失败，原因：%@",error);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    // App 收到推送的通知
    [BPush handleNotification:userInfo];
    
    NSLog(@"%@", userInfo);
    
    NSLog(@"********** ios7.0之前 **********");
    // 应用在前台 或者后台开启状态下，不跳转页面，让用户选择。
    if (application.applicationState == UIApplicationStateActive || application.applicationState == UIApplicationStateBackground) {
        NSLog(@"acitve or background");
        UIAlertView *alertView =[[UIAlertView alloc]initWithTitle:@"收到一条消息" message:userInfo[@"aps"][@"alert"] delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [alertView show];
    }
    else//杀死状态下，直接跳转到跳转页面。
    {
        NSLog(@"background gogogogo");
    }
    
    
    NSLog(@"%@",userInfo);
    
}
- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification
{
    NSLog(@"接收本地通知啦！！！");
    [BPush showLocalNotificationAtFront:notification identifierKey:nil];
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
    [alert.layer setBorderWidth:0.8f];
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
        [defaults setObject:[[dic objectForKey:@"data"]objectForKey:@"rate"] forKey:@"accountRate"];
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
- (void)getUserInfoWithUserId:(NSString *)userId completion:(void (^)(RCUserInfo *))completion{
        NSLog(@"getUserInfo work");
        NSLog(@"userID= %@",userId);
        RCUserInfo *user = [[RCUserInfo alloc]init];
        user.userId = userId;
    userIDDDD = userId;
    
    [self performSelectorOnMainThread:@selector(SearchIDDDDDDDDD) withObject:nil waitUntilDone:YES];
#warning dispatch api until get member info
    user.name = userName;
    user.portraitUri = userUrl;
        return completion(user);
    
}
-(void)SearchIDDDDDDDDD{
    APIConn *conn = [APIConn new];
    conn.apiDelegate = self;
    [conn Search_ID:@{@"user_ID":userIDDDD}];
}
-(void)Search_IDFinished:(NSDictionary *)dic{
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        userName = [[dic objectForKey:@"message"]objectForKey:@"name"];
        userUrl = [[dic objectForKey:@"message"]objectForKey:@"photo"];
    }else{
        userName =@"";
        userUrl = @"http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png";
    }
}
#pragma mark - location
-(void)locationManager:(CLLocationManager *)manager
    didUpdateLocations:(NSArray *)locations {
    NSLog(@"loc=%@",locations.lastObject);
    CLLocation *c = [locations objectAtIndex:0];
    NSLog(@"緯度 經度 ＝ %f   %f",c.coordinate.latitude,c.coordinate.longitude);
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:[NSString stringWithFormat:@"%f",c.coordinate.latitude] forKey:@"locationX"];
    [defaults setObject:[NSString stringWithFormat:@"%f",c.coordinate.longitude] forKey:@"locationY"];
    [defaults synchronize];
    [location stopUpdatingLocation];
}
-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"error = %@",error);
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"0" forKey:@"locationX"];
    [defaults setObject:@"0" forKey:@"locationY"];
    [defaults synchronize];
//    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"獲取您的位置失敗!" message:@"請開啟定位,開啟前無法使用本app" delegate:nil cancelButtonTitle:@"確定" otherButtonTitles:nil];
//    
//    [alert show];
}

@end
