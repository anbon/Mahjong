//
//  RongViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/12/1.
//  Copyright © 2015年 Duke. All rights reserved.
//
#import <RongIMKit/RongIMKit.h>
#import "RongViewController.h"
#import "AppDelegate.h"
#import "RoomMemberViewController.h"
#import <RongIMKit/RCChatSessionInputBarControl.h>
#import <RongIMKit/RCConversationViewController.h>

//#import "RCConversationViewController.h"
//#import "RCRealTimeLocationManager.h"
@interface RongViewController ()

@end

@implementation RongViewController{
    UIBarButtonItem *test;
    NSTimer *timer,*timer2;
    UIView *verify;
    AppDelegate *appdelegate;
    RongViewController *rong;
    NSString *Uname,*Unum,*age,*gender,*level,*photo,*rate;
    UIBarButtonItem *backBtn;
}
#pragma mark - life Cycle
- (void)viewDidLoad {
    self.defaultHistoryMessageCountOfChatRoom = -1;
    self.chatSessionInputBarControl.delegate = self;
    self.defaultInputType = RC_CHAT_INPUT_BAR_STYLE_CONTAINER;
    [super viewDidLoad];
    
//    rong = (RongViewController*)[UIApplication sharedApplication].delegate;
    backBtn = [[UIBarButtonItem alloc]initWithTitle:@" 退出房間" style:UIBarButtonItemStylePlain target:self action:@selector(backToMain)];
    self.navigationController.navigationItem.hidesBackButton = YES;
    appdelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [appdelegate dismissPauseView];
    if ([[[NSUserDefaults standardUserDefaults] objectForKey:@"Identity"]isEqualToString:@"RoomOnwer"]) {
        //timer
        [self initializeTimer];
        [self initializeTimer2];
        NSLog(@"room onwer");
    }else{
        //yoyoyoyoyoyo i'm roomMember
        NSLog(@"room member");
        [self initializeTimer2];
    }
    
    rong.defaultInputType = RCChatSessionInputBarInputText;
    self.conversationMessageCollectionView.backgroundColor = [UIColor whiteColor];
    self.chatSessionInputBarControl.delegate = self;
    self.defaultInputType = RC_CHAT_INPUT_BAR_STYLE_CONTAINER;
    self.chatSessionInputBarControl.emojiButton = nil;
    self.chatSessionInputBarControl.emojiButton.hidden =YES;
    self.defaultInputType = RC_CHAT_INPUT_BAR_STYLE_CONTAINER;
    CGRect rect = CGRectMake(0.0f, 64, WIDTH, HEIGHT);
    self.view.frame = rect;
    
    
    
    
    NSLog(@"rongviewviewdidloadfinished");
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationItem.leftBarButtonItem = backBtn;
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [timer invalidate];
    [timer2 invalidate];
    self.navigationItem.leftBarButtonItem = nil;
}
- (void)willDisplayMessageCell:(RCMessageBaseCell *)cell
                   atIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"wildispkayMessage");
    if ([cell isMemberOfClass:[RCTextMessageCell class]]) {
        RCTextMessageCell *textCell=(RCTextMessageCell *)cell;
        //      自定义气泡图片的适配
        UIImage *image=textCell.bubbleBackgroundView.image;
        textCell.bubbleBackgroundView.image=[textCell.bubbleBackgroundView.image resizableImageWithCapInsets:UIEdgeInsetsMake(image.size.height * 0.8, image.size.width * 0.8,image.size.height * 0.2, image.size.width * 0.2)];
        //      更改字体的颜色
        textCell.textLabel.textColor=[UIColor redColor];
    }
}
- (void)willDisplayConversationTableCell:(RCMessageBaseCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"123123123");
    if ([cell isKindOfClass:[RCMessageCell class]]) {
        NSLog(@"block in");
        RCMessageCell *messageCell = (RCMessageCell *)cell;
        //messageCell.portraitImageView
        UIImageView *portraitImageView= (UIImageView *)messageCell.portraitImageView;
//        portraitImageView.layer.cornerRadius = 20;
        [portraitImageView.layer setCornerRadius:10];
    }
}
#pragma mark - API Delegate
-(void)networkError:(NSString *)err{
    
}
-(void)Search_ChatFinished:(NSDictionary *)dic{
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        Uname = [[dic objectForKey:@"message"] objectForKey:@"Uname"];
        Unum = [[dic objectForKey:@"message"] objectForKey:@"Unum"];
        age = [[dic objectForKey:@"message"] objectForKey:@"age"];
        gender = [[dic objectForKey:@"message"] objectForKey:@"gender"];
        level = [[dic objectForKey:@"message"] objectForKey:@"level"];
        photo = [[dic objectForKey:@"message"] objectForKey:@"photo"];
        rate = [[dic objectForKey:@"message"] objectForKey:@"rate"];
        [timer invalidate];
        [self initAddMemberView];
    }else{
        
    }
}
-(void)VerifyChatFinished:(NSDictionary *)dic{
//    NSLog(@"verfi dic=%@",dic);
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        //success
        [verify removeFromSuperview];
        [self initializeTimer];
    }else if([[dic objectForKey:@"status"]isEqualToString:@"0"]){
        [verify removeFromSuperview];
        [self initializeTimer];
    }
}
-(void)DestinyFinished:(NSDictionary *)dic{
//    NSLog(@"destiny dic=%@",dic);
    
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        
    }else if([[dic objectForKey:@"status"]isEqualToString:@"0"]){
        NSString *msg = [NSString stringWithFormat:@"%@",[dic objectForKey:@"message"]];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"離開房間!!" message:msg delegate:nil cancelButtonTitle:@"確定" otherButtonTitles:nil];
        
        [alert show];
        [self.navigationController popToViewController:[[appdelegate.mainNavi viewControllers]objectAtIndex:0] animated:NO];
    }
}
-(void)Search_IDFinished:(NSDictionary *)dic{
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"name"] forKey:@"memberUserName"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"num"] forKey:@"memberUserNum"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"level"] forKey:@"memberLevel"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"photo"] forKey:@"memberPhoto"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"age"] forKey:@"memberAge"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"gender"] forKey:@"memberGender"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"rate"] forKey:@"memberRate"];
        [defaults synchronize];
        
        RoomMemberViewController *result = [RoomMemberViewController new];
        [self.navigationController pushViewController:result animated:YES];
    }
    
}
#pragma mark - event response
-(void)backToMain{
    [self.navigationController popToViewController:[[appdelegate.mainNavi viewControllers]objectAtIndex:0] animated:NO];
}
-(void)tapIcon:(UITapGestureRecognizer*)sender{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:Uname forKey:@"memberUserName"];
    [defaults setObject:Unum forKey:@"memberUserNum"];
    [defaults setObject:level forKey:@"memberLevel"];
    [defaults setObject:photo forKey:@"memberPhoto"];
    [defaults setObject:age forKey:@"memberAge"];
    [defaults setObject:gender forKey:@"memberGender"];
    [defaults setObject:rate forKey:@"memberRate"];
    [defaults synchronize];
    
    [verify removeFromSuperview];
    RoomMemberViewController *result = [RoomMemberViewController new];
    [self.navigationController pushViewController:result animated:YES];
    
}
-(void)cancelSearchRoomView{
    [verify removeFromSuperview];
    [self initializeTimer];
}

-(void)verifyAgree{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *room_ID = [defaults objectForKey:@"CreatRoomNum"];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn VerifyChat:@{@"status":@"1",@"user_ID":Unum,@"room_ID":room_ID}];
}
-(void)verifyBlock{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *room_ID = [defaults objectForKey:@"CreatRoomNum"];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn VerifyChat:@{@"status":@"0",@"user_ID":Unum,@"room_ID":room_ID}];
}
#pragma mark - timerDelegate
-(void)initializeTimer {
    float theInterval = 1.0/0.2;
    timer = [NSTimer scheduledTimerWithTimeInterval:theInterval
                                             target:self
                                           selector:@selector(timerConnSearchRoom)
                                           userInfo:nil
                                            repeats:YES];
}

-(void)timerConnSearchRoom{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"accountID"];
    NSString *room_ID = [defaults objectForKey:@"CreatRoomNum"];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn Search_Chat:@{@"user_ID":userID,@"room_ID":room_ID}];
}
-(void)initializeTimer2 {
    float theInterval = 1.0/0.2;
    timer2 = [NSTimer scheduledTimerWithTimeInterval:theInterval
                                             target:self
                                           selector:@selector(timerConnDestiny)
                                           userInfo:nil
                                            repeats:YES];
}

-(void)timerConnDestiny{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"accountID"];
    NSString *room_ID = [defaults objectForKey:@"RoomNum"];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn Destiny:@{@"user_ID":userID,@"room_ID":room_ID}];
}
#pragma mark - getters & setters
-(void)initAddMemberView{
    verify = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
    [verify setBackgroundColor:[UIColor clearColor]];
    
    UIView *alert = [[UIView alloc]initWithFrame:CGRectMake(WIDTH/2 - 135*RATIO, 100*RATIO, 270*RATIO, 150*RATIO)];
    [alert.layer setBorderColor:[MAIN_COLOR CGColor]];
    [alert.layer setBorderWidth:2.0f];
    [alert.layer setCornerRadius:40*RATIO];
    alert.backgroundColor = [UIColor whiteColor];
    [alert setClipsToBounds:YES];
    
    UILabel *lab = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 270*RATIO, 50*RATIO)];
    lab.backgroundColor = MAIN_COLOR;
    
    UIImageView *popupMemberIcon = [[UIImageView alloc] initWithFrame:CGRectMake(115*RATIO, 5*RATIO, 40*RATIO,40*RATIO)];
    popupMemberIcon.contentMode = UIViewContentModeScaleAspectFill;
    [popupMemberIcon.layer setCornerRadius:20*RATIO];
    popupMemberIcon.clipsToBounds = YES;
    popupMemberIcon.backgroundColor = [UIColor clearColor];
    //icon
    NSString *iconurl = [NSString stringWithFormat:@"%@", photo];
    [popupMemberIcon sd_setImageWithURL:[NSURL URLWithString:iconurl] placeholderImage:[UIImage imageNamed:@"photo.png"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
    }];
    UITapGestureRecognizer *tapIcon = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapIcon:)];
    [popupMemberIcon addGestureRecognizer:tapIcon];
    
    UIButton *memberBtn = [[UIButton alloc]initWithFrame:CGRectMake(115*RATIO, 5*RATIO, 40*RATIO,40*RATIO)];
    [memberBtn.layer setCornerRadius:20*RATIO];
    [memberBtn addTarget:self action:@selector(tapIcon:) forControlEvents:UIControlEventTouchUpInside];
    
    
    UILabel *statusView = [[UILabel alloc]initWithFrame:CGRectMake(0, 60*RATIO, 270*RATIO, 30*RATIO)];
    statusView.text = Uname;
    [statusView setFont:[UIFont systemFontOfSize:20*RATIO]];
    statusView.textAlignment = NSTextAlignmentCenter;
    
    UIButton *cancelBtn = [[UIButton alloc]initWithFrame:CGRectMake(35*RATIO, 100*RATIO, 80*RATIO, 35*RATIO)];
    [cancelBtn setTitle:@"拒絕" forState:UIControlStateNormal];
    [cancelBtn setTitleColor:MAIN_COLOR forState:UIControlStateNormal];
    [cancelBtn.titleLabel setFont:[UIFont systemFontOfSize:15*RATIO]];
    [cancelBtn.layer setBorderWidth:0.8f];
    [cancelBtn.layer setCornerRadius:35/2*RATIO];
    [cancelBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
    [cancelBtn addTarget:self action:@selector(verifyBlock) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *admitBtn = [[UIButton alloc]initWithFrame:CGRectMake(155*RATIO, 100*RATIO, 80*RATIO, 35*RATIO)];
    [admitBtn setTitle:@"接受" forState:UIControlStateNormal];
    [admitBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [admitBtn.titleLabel setFont:[UIFont systemFontOfSize:15*RATIO]];
    admitBtn.backgroundColor = MAIN_COLOR;
    [admitBtn.layer setBorderWidth:0.8f];
    [admitBtn.layer setCornerRadius:35/2*RATIO];
    [admitBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
    [admitBtn addTarget:self action:@selector(verifyAgree) forControlEvents:UIControlEventTouchUpInside];
    
    statusView.textAlignment = NSTextAlignmentCenter;
    [statusView setClipsToBounds:YES];
    [statusView setFont:[UIFont systemFontOfSize:15]];
    
    statusView.textColor = [UIColor blackColor];
    
    
    [alert addSubview:lab];
    [alert addSubview:popupMemberIcon];
    [alert addSubview:memberBtn];
    [alert addSubview:statusView];
    [alert addSubview:cancelBtn];
    [alert addSubview:admitBtn];
    
    alert.layer.shadowColor = [[UIColor blackColor] CGColor];
    alert.layer.shadowOffset = CGSizeMake(3.0f, 3.0f); // [水平偏移, 垂直偏移]
    alert.layer.shadowOpacity = 0.5f; // 0.0 ~ 1.0 的值
    alert.layer.shadowRadius = 10.0f; // 陰影發散的程度
    
    [verify addSubview:alert];
    
    [appdelegate.mainNavi.view addSubview:verify];
}
#pragma mark - rong
- (void)didTapCellPortrait:(NSString *)userId{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString * accountID = [defaults objectForKey:@"accountID"];
#pragma mark - push to Member
    NSLog(@"tap userID = %@",userId);
    if ([accountID isEqualToString:userId]) {
        
    }else{
        APIConn *conn = [APIConn new];
        conn.apiDelegate = self;
        [conn Search_ID:@{@"user_ID":userId}];
    }
    
   
}

@end
