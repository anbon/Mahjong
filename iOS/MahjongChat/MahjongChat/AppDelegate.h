//
//  AppDelegate.h
//  MahjongChat
//
//  Created by Duke on 2015/10/15.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainViewController.h"
#import <RongIMKit/RongIMKit.h>
#import "APIConn.h"
#define WIDTH [[UIScreen mainScreen] bounds].size.width
#define HEIGHT [[UIScreen mainScreen] bounds].size.height
#define RATIO WIDTH / 414
#define MAIN_COLOR [UIColor colorWithRed:97/255.0f green:206/255.0f blue:229/255.0f alpha:1.0f]
#define LIGHT_MAIN_COLOR [UIColor colorWithRed:54/255.0f green:187/255.0f blue:217/255.0f alpha:1.0f]
#define TINT_COLOR [UIColor colorWithRed:255/255.0f green:197/255.0f blue:13/255.0f alpha:1.0f]
#define ERROR_COLOR [UIColor colorWithRed:237/255.0f green:28/255.0f blue:36/255.0f alpha:1.0f]
#define BORDER_COLOR [UIColor colorWithRed:204/255.0f green:204/255.0f blue:204/255.0f alpha:1.0f]
#define TEXT_COLOR [UIColor colorWithRed:128/255.0f green:128/255.0f blue:128/255.0f alpha:1.0f]
#define TAB_HEIGHT WIDTH * (46.0f/320.0f)

#define LIGHTG_COLOR [UIColor colorWithRed:0.97 green:0.97 blue:0.97 alpha:1.0];
#define LIGHT_BLUE [UIColor colorWithRed:0.94 green:0.98 blue:0.99 alpha:1.0];

@interface AppDelegate : UIResponder <UIApplicationDelegate,RCIMConnectionStatusDelegate,APIConnDelegate,RCIMUserInfoDataSource>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) MainViewController *mainViewController;
@property (retain, nonatomic) UINavigationController *mainNavi;
-(void)loginToMain;
-(void)loginToSingIn;
-(void)initPauseView:(NSString*)status;
-(void)dismissPauseView;
@end

