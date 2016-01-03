//
//  ProfileViewController.h
//  MahjongChat
//
//  Created by Duke on 2015/11/22.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIImageView+WebCache.h"
#import "TQStarRatingView.h"
#import "ZHPickView.h"
#import "APIConn.h"
@interface ProfileViewController : UIViewController<StarRatingViewDelegate,ZHPickViewDelegate,APIConnDelegate>

-(void)editProfile;
@end
