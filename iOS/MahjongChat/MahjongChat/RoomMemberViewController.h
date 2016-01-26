//
//  RoomMemberViewController.h
//  MahjongChat
//
//  Created by Duke on 2016/1/4.
//  Copyright © 2016年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIImageView+WebCache.h"
#import "TQStarRatingView.h"
#import "APIConn.h"
@interface RoomMemberViewController : UIViewController<StarRatingViewDelegate,APIConnDelegate,UIActionSheetDelegate>

@end
