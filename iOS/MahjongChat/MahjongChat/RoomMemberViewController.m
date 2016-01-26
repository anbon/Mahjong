//
//  RoomMemberViewController.m
//  MahjongChat
//
//  Created by Duke on 2016/1/4.
//  Copyright © 2016年 Duke. All rights reserved.
//

#import "RoomMemberViewController.h"
#import "AppDelegate.h"
#import "TQStarRatingView.h"
@interface RoomMemberViewController ()

@end

@implementation RoomMemberViewController
{
    float rating;
    AppDelegate *appDelegate;
    UIView *ratingView;
    TQStarRatingView *starRatingView;
    float currentStarValue;
    int finalValue;
    UIBarButtonItem *more;
    UIActionSheet *actionSheet;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    
    UIButton *imageButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 22, 22)];
    [imageButton setBackgroundImage:[UIImage imageNamed:@"more.png"] forState:UIControlStateNormal];
    [imageButton addTarget:self action:@selector(blockAction) forControlEvents:UIControlEventTouchUpInside];
    more = [[UIBarButtonItem alloc] initWithCustomView:imageButton];
    [self initByDuke];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationItem.rightBarButtonItem = more;
    [[UIView appearanceWhenContainedIn:[UIAlertController class], nil] setTintColor:MAIN_COLOR];
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    self.navigationItem.rightBarButtonItem = nil;

}
#pragma mark - API Delegate
-(void)networkError:(NSString *)err{
    [appDelegate dismissPauseView];
}
-(void)ratingFinished:(NSDictionary *)dic{
    [appDelegate dismissPauseView];
    if ([[dic objectForKey:@"status"]isEqualToString:@"0"]) {
        [ratingView removeFromSuperview];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"評分失敗!!" message:@"您已評過該使用者" delegate:nil cancelButtonTitle:@"我知道了" otherButtonTitles:nil];
        
        [alert show];
    }else if([[dic objectForKey:@"status"]isEqualToString:@"1"]){
        [ratingView removeFromSuperview];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"評分成功!!" message:@"" delegate:nil cancelButtonTitle:@"我知道了" otherButtonTitles:nil];
        
        [alert show];
    }
        
    
}
-(void)blockaddFinished:(NSDictionary *)dic{
    [appDelegate dismissPauseView];
    NSLog(@"bolck dic = %@",dic);
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        
    }else if([[dic objectForKey:@"status"]isEqualToString:@"0"]){
        NSString *msg = [dic objectForKey:@"message"];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"黑名單失敗!!" message:msg delegate:nil cancelButtonTitle:@"確定" otherButtonTitles:nil];
        
        [alert show];
    }
}
#pragma mark - event response
-(void)blockAction{
    actionSheet = [[UIActionSheet alloc]initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:nil otherButtonTitles:@"新增黑名單", nil];
    actionSheet.tintColor = MAIN_COLOR;
    [actionSheet showInView:self.view];
}
-(void)blockActionConn{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"memberUserNum"];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn blockadd:@{@"user_ID":userID}];
    [appDelegate initPauseView:@"更新中..請稍候.."];
}
-(void)sendGrade{
    CGFloat x,y;
    x = 10*RATIO;
    y = 100*RATIO;
    UIColor *line_grayColor = [UIColor colorWithWhite:0.6 alpha:0.6f];
    ratingView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
    UITapGestureRecognizer *tapView = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cancelview:)];
    [ratingView addGestureRecognizer:tapView];
    ratingView.backgroundColor = line_grayColor;
    
    UIView *ratingBg = [[UIView alloc]initWithFrame:CGRectMake(32*RATIO, 130*RATIO, 350*RATIO, 350*RATIO)];
    [ratingBg setBackgroundColor:[UIColor whiteColor]];
    [ratingBg.layer setBorderWidth:0.8f];
    [ratingBg.layer setBorderColor:[MAIN_COLOR CGColor]];
    [ratingBg.layer setCornerRadius:15*RATIO];
    
    starRatingView = [[TQStarRatingView alloc]initWithFrame:CGRectMake(25*RATIO, 100*RATIO, 300*RATIO, 60*RATIO) numberOfStar:5];
    currentStarValue = 5;
    starRatingView.delegate = self;
    [starRatingView setStarValue:currentStarValue];
    
    
    UITapGestureRecognizer *tapsubView = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cancelSubview:)];
    
    
    UIButton *checkBtn = [[UIButton alloc]initWithFrame:CGRectMake(((350*RATIO)/2)-60*RATIO, 350*RATIO - 145*RATIO , 120*RATIO, 50*RATIO)];
    [checkBtn setTitle:@"確認" forState:UIControlStateNormal];
    checkBtn.titleLabel.textColor = [UIColor whiteColor];
    [checkBtn.titleLabel setFont:[UIFont systemFontOfSize:30*RATIO]];
    [checkBtn.layer setBorderWidth:0.8f];
    [checkBtn.layer setCornerRadius:25*RATIO];
    [checkBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
    checkBtn.backgroundColor = MAIN_COLOR;
    [checkBtn.titleLabel setFont:[UIFont systemFontOfSize:20]];
    [checkBtn addTarget:self action:@selector(updateRate) forControlEvents:UIControlEventTouchUpInside];
    
    [ratingBg addGestureRecognizer:tapsubView];
    [ratingBg addSubview:starRatingView];
    [ratingBg addSubview:checkBtn];
    
    [ratingView addSubview:ratingBg];
    
    [appDelegate.mainNavi.view addSubview:ratingView];
    
}
-(void)cancelview:(UITapGestureRecognizer*)sender{
    [ratingView removeFromSuperview];
}
-(void)cancelSubview:(UITapGestureRecognizer*)sender{
}
-(void)updateRate{
#warning API-updateRating
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"memberUserNum"];
    NSString *ratingTemp = [NSString stringWithFormat:@"%d",finalValue];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn rating:@{@"user_ID":userID,@"rating":ratingTemp}];
    [appDelegate initPauseView:@"上傳評分中..請稍候.."];
    
}
-(void)starRatingView:(TQStarRatingView *)view score:(float)score{
    currentStarValue = score;
    NSLog(@"star = %f",currentStarValue);
    finalValue = currentStarValue * 5;
    if (finalValue > 5) {
        finalValue = 5;
    }
    NSLog(@"finalValue = %d",finalValue);
}
#pragma mark - ActionSheet Delegate
- (void)actionSheet:(UIActionSheet *)actionSheetTemp clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    //將按鈕的Title當作判斷的依據
    NSString *title = [actionSheetTemp buttonTitleAtIndex:buttonIndex];
    
    if([title isEqualToString:@"新增黑名單"]) {
        NSLog(@"block push");
        [actionSheet removeFromSuperview];
        actionSheet = [[UIActionSheet alloc]initWithTitle:nil delegate:self cancelButtonTitle:@"返回" destructiveButtonTitle:nil otherButtonTitles:@"確認", nil];
        [actionSheet showInView:self.view];
        
    }
    if ([title isEqualToString:@"確認"]) {
        [self blockActionConn];
    }
}
//- (void)willPresentActionSheet:(UIActionSheet *)actionSheet
//{
//    for (UIView *subview in actionSheet.subviews) {
//        if ([subview isKindOfClass:[UIButton class]]) {
//            UIButton *button = (UIButton *)subview;
//            [button setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
//        }
//    }
//}
#pragma mark - getters & setters
-(void)initByDuke{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    rating = [[defaults objectForKey:@"memberRate"] intValue];
    NSString *name = [defaults objectForKey:@"memberUserName"];
    NSString *gender = [defaults objectForKey:@"memberGender"];
    NSString *age = [defaults objectForKey:@"memberAge"];
    NSString *photo = [defaults objectForKey:@"memberPhoto"];
    
    
    CGFloat y = 0;
    
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT-64)];
    scrollView.backgroundColor = LIGHT_BLUE;
    
    UILabel *headBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 90*RATIO)];
    headBg.backgroundColor = LIGHTG_COLOR;
    
    UIImageView *popupCleanerIcon = [[UIImageView alloc] initWithFrame:CGRectMake(20*RATIO, 10*RATIO, 70*RATIO, 70*RATIO)];
    popupCleanerIcon.contentMode = UIViewContentModeScaleAspectFill;
    [popupCleanerIcon.layer setCornerRadius:15*RATIO];
    popupCleanerIcon.clipsToBounds = YES;
    popupCleanerIcon.backgroundColor = [UIColor clearColor];
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
    
    UILabel *sexTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 60*RATIO, 20*RATIO)];
    sexTitle.backgroundColor = [UIColor clearColor];
    sexTitle.text = @"性別";
    sexTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *sex = [[UILabel alloc]initWithFrame:CGRectMake(90*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
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
    
    UILabel *nickNameTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 60*RATIO, 20*RATIO)];
    nickNameTitle.backgroundColor = [UIColor clearColor];
    nickNameTitle.text = @"暱稱";
    nickNameTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *nickName = [[UILabel alloc]initWithFrame:CGRectMake(90*RATIO, y+25*RATIO, 150*RATIO, 20*RATIO)];
    nickName.backgroundColor = [UIColor clearColor];
    nickName.text = name;
    nickName.textAlignment = NSTextAlignmentLeft;
    
    y += 70*RATIO;
    
    UILabel *ageBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    ageBg.backgroundColor = LIGHT_BLUE;
    
    UILabel *ageTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 60*RATIO, 20*RATIO)];
    ageTitle.backgroundColor = [UIColor clearColor];
    ageTitle.text = @"年齡";
    ageTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *agetext = [[UILabel alloc]initWithFrame:CGRectMake(90*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    agetext.backgroundColor = [UIColor clearColor];
    agetext.text = age;
    agetext.textAlignment = NSTextAlignmentLeft;
    
    y += 70*RATIO;
    
    
    UILabel *ratingBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    ratingBg.backgroundColor = LIGHT_BLUE;
    [scrollView addSubview:ratingBg];
    CGFloat xforStar = 15*RATIO;
    for (int i = 0; i < rating; i++) {
        UIImageView *starFull = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"star_filled.png"]];
        starFull.frame = CGRectMake(xforStar , y+10*RATIO, 50*RATIO ,50*RATIO);
        [scrollView addSubview:starFull];
        xforStar = xforStar + 50*RATIO;
    }
    for (int i = 0; i <5-rating; i++) {
        UIImageView *starFull = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"star.png"]];
        starFull.frame = CGRectMake(xforStar , y+10*RATIO, 50*RATIO ,50*RATIO);
        [scrollView addSubview:starFull];
        xforStar = xforStar + 50*RATIO;
    }
    
    UIButton *grade = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH - 95*RATIO, y+15*RATIO, 80*RATIO, 40*RATIO)];
    [grade.layer setCornerRadius:20*RATIO];
    grade.backgroundColor = MAIN_COLOR;
    [grade setTitle:@"評論" forState:UIControlStateNormal];
    grade.titleLabel.textColor = [UIColor whiteColor];
    [grade addTarget:self action:@selector(sendGrade) forControlEvents:UIControlEventTouchUpInside];
    
    
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
    [scrollView addSubview:grade];
    
    [scrollView setShowsHorizontalScrollIndicator:NO];
    [scrollView setShowsVerticalScrollIndicator:NO];
    [scrollView setScrollEnabled:YES];
    [self.view addSubview:scrollView];
    
}

@end
