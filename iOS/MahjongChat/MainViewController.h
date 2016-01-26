//
//  MainViewController.h
//  MahjongChat
//
//  Created by Duke on 2015/11/20.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AnimateTabbar.h"
@interface MainViewController : UIViewController<AnimateTabbarDelegate>
{
    UIBarButtonItem *edit;
}
@property (retain, nonatomic) UIButton *imageButtonForEdit;
@property (retain, nonatomic) AnimateTabbarView *tabbar;
@property (nonatomic, strong) IBOutlet UIView *childView;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *filter;
@end
