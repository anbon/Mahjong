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
#import "WaitForEmpowerViewController.h"
@interface SearchReslutViewController ()

@end

@implementation SearchReslutViewController{
    float  rating;
    UIButton *follow;
}

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
-(void)networkError:(NSString *)err{
    
}
-(void)followFinished:(NSDictionary *)dic{
    NSLog(@"follow dic=%@",dic);
    if([[dic objectForKey:@"status"]isEqualToString:@"1"]){
        NSString *msg = [dic objectForKey:@"message"];
        if ([msg isEqualToString:@"1"]) {//n->Y
            [follow setTitle:@"UnFollow" forState:UIControlStateNormal];
            [follow setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            [follow.layer setBackgroundColor:[[UIColor redColor] CGColor]];
        }else{
            [follow.layer setBackgroundColor:[MAIN_COLOR CGColor]];
            [follow setTitle:@"Follow" forState:UIControlStateNormal];
            [follow setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        }
    }
}
#pragma mark - event response
-(void)followAction{
#warning  api follow/unfollow
    APIConn *conn = [APIConn new];
    conn.apiDelegate = self;
    [conn follow:@{@"user_ID":[[NSUserDefaults standardUserDefaults] objectForKey:@"accountID"],
                   @"Follow_ID":[[NSUserDefaults standardUserDefaults] objectForKey:@"searchResultNum"]}];

}
-(void)goToRoom{
    [[NSUserDefaults standardUserDefaults]setObject:[[NSUserDefaults standardUserDefaults] objectForKey:@"searchResult"] forKey:@"selectChatRoom" ];
    [[NSUserDefaults standardUserDefaults] synchronize];//selectChatRoom
    WaitForEmpowerViewController *emp = [WaitForEmpowerViewController new];
    [self.navigationController pushViewController:emp animated:YES];
}
#pragma mark - getters & setters
-(void)initByDuke{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *name = [defaults objectForKey:@"searchResultName"];
    NSString *gender = [defaults objectForKey:@"searchResultGender"];
    NSString *age = [defaults objectForKey:@"searchResultAge"];
    NSString *photo = [defaults objectForKey:@"searchResultPhoto"];
    rating = [[defaults objectForKey:@"searchResultRate"] intValue];
    
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
    
    follow = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH-110*RATIO, y+30*RATIO, 90*RATIO, 30*RATIO)];
    if ([[[NSUserDefaults standardUserDefaults] objectForKey:@"followOrNot"]isEqualToString:@"1"]) {
        [follow setTitle:@"UnFollow" forState:UIControlStateNormal];
        [follow setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [follow.layer setBackgroundColor:[[UIColor redColor] CGColor]];
    }else{
        [follow.layer setBackgroundColor:[MAIN_COLOR CGColor]];
        [follow setTitle:@"Follow" forState:UIControlStateNormal];
        [follow setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    }
    [follow.layer setCornerRadius:15*RATIO];
    [follow addTarget:self action:@selector(followAction) forControlEvents:UIControlEventTouchUpInside];
    
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
    
    UILabel *nickName = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 90*RATIO, 20*RATIO)];
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
    
    
    UILabel *ratingBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    ratingBg.backgroundColor = LIGHT_BLUE;
    [scrollView addSubview:ratingBg];
#warning rating stiil need to insert
    CGFloat xforStar = 15*RATIO;
    for (int i = 0; i < rating; i++) {
        UIImageView *starFull = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"star_filled.png"]];
        starFull.frame = CGRectMake(xforStar , y+10*RATIO, 50*RATIO ,50*RATIO);
        [scrollView addSubview:starFull];
        xforStar = xforStar + 50*RATIO;
        NSLog(@"star i = %d",i);
    }
    for (int i = 0; i <5-rating; i++) {
        UIImageView *starFull = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"star.png"]];
        starFull.frame = CGRectMake(xforStar , y+10*RATIO, 50*RATIO ,50*RATIO);
        [scrollView addSubview:starFull];
        xforStar = xforStar + 50*RATIO;
    }

    y = y+120*RATIO;
    
    if ([[[NSUserDefaults standardUserDefaults] objectForKey:@"RoomingOrNot"]isEqualToString:@"1"]) {
        UIButton *gotoRoomBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH/2-60*RATIO, y , 120*RATIO, 50*RATIO)];
        [gotoRoomBtn setTitle:@"加入房間" forState:UIControlStateNormal];
        [gotoRoomBtn setTitleColor:MAIN_COLOR forState:UIControlStateNormal];
        [gotoRoomBtn.titleLabel setFont:[UIFont systemFontOfSize:20*RATIO]];
        [gotoRoomBtn.layer setBorderWidth:0.8f];
        [gotoRoomBtn.layer setCornerRadius:25*RATIO];
        [gotoRoomBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
        [gotoRoomBtn addTarget:self action:@selector(goToRoom) forControlEvents:UIControlEventTouchUpInside];
        
        [scrollView addSubview:gotoRoomBtn];

    }
    
    y += 70*RATIO;
    
    [scrollView addSubview:headBg];
    [scrollView addSubview:popupCleanerIcon];
    [scrollView addSubview:sexBg];
    [scrollView addSubview:sexTitle];
    [scrollView addSubview:sex];
    [scrollView addSubview:nickNameBg];
    [scrollView addSubview:nickNameTitle];
    [scrollView addSubview:nickName];
    [scrollView addSubview:follow];
    [scrollView addSubview:ageBg];
    [scrollView addSubview:ageTitle];
    [scrollView addSubview:agetext];
    
    [scrollView setShowsHorizontalScrollIndicator:NO];
    [scrollView setShowsVerticalScrollIndicator:NO];
    [scrollView setScrollEnabled:YES];
    [self.view addSubview:scrollView];
    
}


@end
