//
//  SearchReslutViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/12/26.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "SearchReslutViewController.h"
#import "MainViewController.h"
#import "AppDelegate.h"
@interface SearchReslutViewController ()

@end

@implementation SearchReslutViewController

#pragma mark - life Cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    [self initByDuke];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - API Delegate
#pragma mark - event response
#pragma mark - getters & setters
-(void)initByDuke{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *num = [defaults objectForKey:@"searchResultNum"];
    NSString *name = [defaults objectForKey:@"searchResultName"];
    NSString *email = [defaults objectForKey:@"searchResultEmail"];
    NSString *username = [defaults objectForKey:@"searchResultUsername"];
    NSString *password = [defaults objectForKey:@"searchResultPassword"];
    NSString *gender = [defaults objectForKey:@"searchResultGender"];
    NSString *age = [defaults objectForKey:@"searchResultAge"];
    NSString *locationX = [defaults objectForKey:@"searchResultLocationX"];
    NSString *locationY = [defaults objectForKey:@"searchResultLocationY"];
    NSString *photo = [defaults objectForKey:@"searchResultPhoto"];
    NSString *level = [defaults objectForKey:@"searchResultLevel"];
    NSString *distance = [defaults objectForKey:@"searchResultDistance"];
    
    CGFloat y = 0;
    
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT-64)];
    scrollView.backgroundColor = LIGHT_BLUE;
    
    UILabel *headBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 90*RATIO)];
    headBg.backgroundColor = LIGHTG_COLOR;
    
    UIImageView *popupCleanerIcon = [[UIImageView alloc] initWithFrame:CGRectMake(20*RATIO, 10*RATIO, 70*RATIO, 70*RATIO)];
    popupCleanerIcon.contentMode = UIViewContentModeScaleAspectFill;
    [popupCleanerIcon.layer setCornerRadius:15*RATIO];
    popupCleanerIcon.clipsToBounds = YES;
    popupCleanerIcon.backgroundColor = [UIColor redColor];
    //icon
    [popupCleanerIcon sd_setImageWithURL:[NSURL URLWithString:photo] placeholderImage:[UIImage imageNamed:@"photo.png"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        popupCleanerIcon.alpha = 0.3;
        [UIView animateWithDuration:0.4f animations:^{
            popupCleanerIcon.alpha = 1.0f;
        }];
    }];
    
    y += 90*RATIO;
    
    UILabel *sexBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    sexBg.backgroundColor = LIGHT_BLUE;
    
    UILabel *sexTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    sexTitle.backgroundColor = [UIColor clearColor];
    sexTitle.text = @"性別";
    sexTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *sex = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    sex.backgroundColor = [UIColor clearColor];
    if ([gender isEqualToString:@"0"]) {
        sex.text = @"女";
    }else{
        sex.text = @"男";
    }
    sex.textAlignment = NSTextAlignmentLeft;
    
    
    
    y += 70*RATIO;
    
    UILabel *nickNameBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    nickNameBg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *nickNameTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    nickNameTitle.backgroundColor = [UIColor clearColor];
    nickNameTitle.text = @"暱稱";
    nickNameTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *nickName = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    nickName.backgroundColor = [UIColor clearColor];
    nickName.text = name;
    nickName.textAlignment = NSTextAlignmentLeft;
    
    y += 70*RATIO;
    
    UILabel *ageBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    ageBg.backgroundColor = LIGHT_BLUE;
    
    UILabel *ageTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    ageTitle.backgroundColor = [UIColor clearColor];
    ageTitle.text = @"年齡";
    ageTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *agetext = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    agetext.backgroundColor = [UIColor clearColor];
    agetext.text = age;
    agetext.textAlignment = NSTextAlignmentLeft;
    
    y += 70*RATIO;
    
    UILabel *locationBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    locationBg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *locationTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    locationTitle.backgroundColor = [UIColor clearColor];
    locationTitle.text = @"地區";
    locationTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *locationXY = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 80*RATIO, 20*RATIO)];
    locationXY.backgroundColor = [UIColor clearColor];
    locationXY.text = @"地區(by後台)";
    locationXY.textAlignment = NSTextAlignmentLeft;
    
    y += 70*RATIO;
    
    UILabel *ratingBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    ratingBg.backgroundColor = LIGHT_BLUE;
#warning rating stiil need to insert
    
    y += 70*RATIO;
    
    [scrollView addSubview:headBg];
    [scrollView addSubview:popupCleanerIcon];
    [scrollView addSubview:sexBg];
    [scrollView addSubview:sexTitle];
    [scrollView addSubview:sex];
    [scrollView addSubview:nickNameBg];
    [scrollView addSubview:nickNameTitle];
    [scrollView addSubview:nickName];
    [scrollView addSubview:ageBg];
    [scrollView addSubview:ageTitle];
    [scrollView addSubview:agetext];
//    [scrollView addSubview:locationBg];
//    [scrollView addSubview:locationTitle];
//    [scrollView addSubview:locationXY];
    [scrollView addSubview:ratingBg];
    
    [scrollView setShowsHorizontalScrollIndicator:NO];
    [scrollView setShowsVerticalScrollIndicator:NO];
    [scrollView setScrollEnabled:YES];
    [self.view addSubview:scrollView];
    
}


@end
