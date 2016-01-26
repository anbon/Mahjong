//
//  RongViewController.h
//  MahjongChat
//
//  Created by Duke on 2015/12/1.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <RongIMKit/RongIMKit.h>
#import "APIConn.h"
#import "UIImageView+WebCache.h"
//#import <RongIMLib/RCMessageContentView.h>
@interface RongViewController : RCConversationViewController<APIConnDelegate,RCMessageCellDelegate,RCRealTimeLocationObserver,RCChatSessionInputBarControlDelegate>

@property (nonatomic) RCUserAvatarStyle portaitStyle; 
@end
