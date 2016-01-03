//
//  RongViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/12/1.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "RongViewController.h"
@interface RongViewController ()

@end

@implementation RongViewController{
    UIBarButtonItem *test;
}
#pragma mark - life Cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    test.title = @"123";
    self.navigationController.navigationItem.rightBarButtonItem = test;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *target_id = [defaults objectForKey:@"groupid"];

    
}

@end
