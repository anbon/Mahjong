//
//  addRoomViewController.h
//  MahjongChat
//
//  Created by Duke on 2015/12/1.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "APIConn.h"
#import "ZHPickView.h"
#import <RongIMKit/RongIMKit.h>
#define LIGHTG_COLOR [UIColor colorWithRed:0.97 green:0.97 blue:0.97 alpha:1.0];
#define LIGHT_BLUE [UIColor colorWithRed:0.94 green:0.98 blue:0.99 alpha:1.0];//(239,251,253)
@interface addRoomViewController : UIViewController<UITextFieldDelegate,APIConnDelegate,ZHPickViewDelegate,RCIMUserInfoDataSource>

@end
