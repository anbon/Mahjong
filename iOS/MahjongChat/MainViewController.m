//
//  MainViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/11/20.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "MainViewController.h"

#import "SearchViewController.h"
#import "LocationViewController.h"
#import "ProfileViewController.h"
#import "addRoomViewController.h"
#import "AppDelegate.h"

@interface MainViewController ()

@end

@implementation MainViewController{
    UIViewController *currentVC;
    SearchViewController *searchVC;
    LocationViewController *locationVC;
    ProfileViewController *profileVC;
    UIBarButtonItem *addRoomBtn;
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"mainview");
    
    UIButton *imageButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 22, 22)];
    [imageButton setBackgroundImage:[UIImage imageNamed:@"addnew.png"] forState:UIControlStateNormal];
    [imageButton addTarget:self action:@selector(addRoom) forControlEvents:UIControlEventTouchUpInside];
    addRoomBtn = [[UIBarButtonItem alloc] initWithCustomView:imageButton];
    
    _imageButtonForEdit = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 50, 22)];
    [_imageButtonForEdit setBackgroundImage:[UIImage imageNamed:@"edit.png"] forState:UIControlStateNormal];
    [_imageButtonForEdit setBackgroundImage:[UIImage imageNamed:@"confirm6.png"] forState:UIControlStateSelected];
    [_imageButtonForEdit addTarget:self action:@selector(editByMain:) forControlEvents:UIControlEventTouchUpInside];
    
    edit = [[UIBarButtonItem alloc] initWithCustomView:_imageButtonForEdit];
    
    //
    self.navigationItem.rightBarButtonItem = addRoomBtn;
    
    self.view.frame = CGRectMake(0, 0, WIDTH, HEIGHT);
    
    
    self.navigationController.navigationBar.translucent = NO;
    self.navigationController.navigationBar.barTintColor = MAIN_COLOR;
    self.navigationController.navigationBar.tintColor= [UIColor whiteColor];
    [self.navigationController.navigationBar
     setTitleTextAttributes:@{NSForegroundColorAttributeName : [UIColor whiteColor]}];
    self.view.translatesAutoresizingMaskIntoConstraints = NO;
    
    _tabbar=[[AnimateTabbarView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT-64)];
    _tabbar.delegate=self;
    
    self.childView=[[UIView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT-64-TAB_HEIGHT)];
    [self.view addSubview:self.childView];
    [self.childView setBackgroundColor:[UIColor clearColor]];
    
    searchVC=[[SearchViewController alloc] initWithNibName:@"SearchViewController" bundle:nil];
    [self addChildViewController:searchVC];
    
    locationVC=[[LocationViewController alloc] initWithNibName:@"LocationViewController" bundle:nil];
    [self addChildViewController:locationVC];
    
    profileVC=[[ProfileViewController alloc] initWithNibName:@"ProfileViewController" bundle:nil];
    [self addChildViewController:profileVC];
    
    
    
    [self.childView addSubview:locationVC.view];
    currentVC=locationVC;
    
    [self.view addSubview:_tabbar];
    _filter = [[UIBarButtonItem alloc]initWithTitle:@"篩選"
                                              style:UIBarButtonItemStylePlain
                                             target:self
                                             action:@selector(locationViewFilter)];
    self.navigationItem.leftBarButtonItem = _filter;
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - tab
// callback
int g_flags=1;
-(void)FirstBtnClick{
    
    if(g_flags==1)
        return;
    [self transitionFromViewController:currentVC toViewController:locationVC duration:0 options:0 animations:^{
    }  completion:^(BOOL finished) {
        currentVC=locationVC;
        g_flags=1;
        _imageButtonForEdit.selected = NO;
        self.navigationItem.rightBarButtonItem = addRoomBtn;
        self.navigationItem.leftBarButtonItem = _filter;
        self.title = @"";
    }];
}
-(void)SecondBtnClick{
    if(g_flags==2)
        return;
    [self transitionFromViewController:currentVC toViewController:searchVC duration:0 options:0 animations:^{
    }  completion:^(BOOL finished) {
        currentVC=searchVC;
        g_flags=2;
        self.navigationItem.rightBarButtonItem=nil;
        _imageButtonForEdit.selected = NO;
        self.title = @"搜尋";
    }];
    
}
-(void)ThirdBtnClick{
    if(g_flags==3)
        return;
    [self transitionFromViewController:currentVC toViewController:profileVC duration:0 options:0 animations:^{
    }  completion:^(BOOL finished) {
        currentVC=profileVC;
        g_flags=3;
       // self.navigationItem.rightBarButtonItem=editProfile;
        self.navigationItem.rightBarButtonItem=edit;
        _imageButtonForEdit.selected = NO;
        self.title = @"我";
    }];
}

-(void)addRoom{
    addRoomViewController *add = [addRoomViewController new];
    [self.navigationController pushViewController:add animated:YES];
    NSLog(@"addroom move");
}

-(void)editByMain:(UIButton *)sender{
    [profileVC editProfile];
    sender.selected = !sender.selected;
   
    
    NSLog(@"main edit push");
}

-(void)locationViewFilter{
    [locationVC filterRoom];
    NSURL *url = [NSURL URLWithString:@"http://www.jackercleaning.com/"];
    UIActivityViewController *avc = [[UIActivityViewController alloc] initWithActivityItems:[NSArray arrayWithObjects:@"潔客幫 - 給您便利居家清潔打掃服務", url, nil] applicationActivities:nil];
    [self presentViewController:avc animated:YES completion:nil];
}

@end
