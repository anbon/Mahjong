//
//  PauseView.h
//  MahjongChat
//
//  Created by Duke on 2015/11/30.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface PauseView : UIView
+(PauseView *)pauseView;
-(void)pauseViewShow:(NSString *)status;
-(void)dismissPauseView;


@property (nonatomic, retain) UIWindow *window;
@property (nonatomic, retain) UIView *background;
@property (nonatomic, retain) UIActivityIndicatorView *spinner;
@property (nonatomic, retain) UIImageView *image;
@property (nonatomic, retain) UILabel *label;


@end
