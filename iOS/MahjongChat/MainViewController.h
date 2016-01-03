//
//  MainViewController.h
//  MahjongChat
//
//  Created by Duke on 2015/11/20.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AnimateTabbar.h"
#import <CoreLocation/CoreLocation.h>
@interface MainViewController : UIViewController<AnimateTabbarDelegate,CLLocationManagerDelegate>
{
    CLLocationManager *location;
    UIBarButtonItem *edit;
//    UIButton *imageButtonForEdit;
}
@property (retain, nonatomic) UIButton *imageButtonForEdit;
@property (retain, nonatomic) AnimateTabbarView *tabbar;
@property (nonatomic, strong) IBOutlet UIView *childView;
@end
