//
//  SingInViewController.h
//  MahjongChat
//
//  Created by Duke on 2015/11/20.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "APIConn.h"
#import <RongIMKit/RongIMKit.h>
@interface SingInViewController : UIViewController<APIConnDelegate,UITextFieldDelegate,RCIMUserInfoDataSource>

@end
