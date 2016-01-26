//
//  LocationViewController.h
//  MahjongChat
//
//  Created by Duke on 2015/11/22.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "APIConn.h"
#import <RongIMKit/RongIMKit.h>
#import "UIImageView+WebCache.h"
@interface LocationViewController : UITableViewController<APIConnDelegate,UITableViewDelegate,UITableViewDataSource,RCIMUserInfoDataSource,UITextFieldDelegate>

-(void)filterRoom;
@end
