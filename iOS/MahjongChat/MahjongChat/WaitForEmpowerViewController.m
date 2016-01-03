//
//  WaitForEmpowerViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/12/29.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "WaitForEmpowerViewController.h"
#import "AppDelegate.h"
#import "RongViewController.h"
@interface WaitForEmpowerViewController ()

@end

@implementation WaitForEmpowerViewController
{
    NSString *onwer,*pplNeed,*baseSet,*unitSet,*timeSet,*circleSet,*tableSet,*smokeSet,*ruleSet,*photoSet,*locationSet;
    UILabel *ppl;
    AppDelegate *appDelegate;
}
#pragma mark - life Cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    onwer = [[[[defaults objectForKey:@"selectChatRoom"]objectForKey:@"users"]objectAtIndex:0]objectForKey:@"Uname"];
    pplNeed = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"people"];
    baseSet =  [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"base"];
    unitSet = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"unit"];
    timeSet = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"time"];
    circleSet = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"circle"];
    tableSet = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"type"];
    if ([tableSet isEqualToString:@"1"]) {
        tableSet = @"電動";
    }else{
        tableSet = @"手動";
    }
    smokeSet = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"cigarette"];
    if ([smokeSet isEqualToString:@"1"]) {
        smokeSet = @"桌菸";
    }else{
        smokeSet = @"不菸";
   
    }
    ruleSet = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"rule"];
    photoSet = [[[[defaults objectForKey:@"selectChatRoom"]objectForKey:@"users"]objectAtIndex:0]objectForKey:@"photo"];
    locationSet = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"RoomName"];
    
    [self initByDuke];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - API Delegate
-(void)networkError:(NSString *)err{
    
}
#pragma mark - event response
#pragma mark - getters & setters
-(void)initByDuke{
    CGFloat y = 0;
    
    y= y+22;
    
    UIImageView *head = [[UIImageView alloc] initWithFrame:CGRectMake(0-20*RATIO, y, WIDTH+40*RATIO, 180*RATIO)];
    [head setImage:[UIImage imageNamed:@"info_bg.png"]];
    [head setContentMode:UIViewContentModeScaleAspectFill];
    
    UIImageView * popupCleanerIcon = [[UIImageView alloc] initWithFrame:CGRectMake(30*RATIO, 85*RATIO, 90*RATIO, 90*RATIO)];
    popupCleanerIcon.contentMode = UIViewContentModeScaleAspectFill;
    [popupCleanerIcon.layer setCornerRadius:45*RATIO];
    popupCleanerIcon.clipsToBounds = YES;
    //icon
    [popupCleanerIcon sd_setImageWithURL:[NSURL URLWithString:photoSet] placeholderImage:[UIImage imageNamed:@"photo.png"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        popupCleanerIcon.alpha = 0.3;
        [UIView animateWithDuration:0.4f animations:^{
            popupCleanerIcon.alpha = 1.0f;
        }];
    }];
    
    UILabel *location = [[UILabel alloc]initWithFrame:CGRectMake(135*RATIO, 110*RATIO, 280*RATIO, 40*RATIO)];
    location.backgroundColor = [UIColor colorWithWhite:0.5f alpha:0.3f];
    [location.layer setCornerRadius:20*RATIO];
    location.textAlignment = NSTextAlignmentCenter;
    location.textColor = [UIColor whiteColor];
    [location setFont:[UIFont fontWithName:@"Helvetica-Bold" size:30*RATIO]];
    [location setClipsToBounds:YES];
    location.text = [NSString stringWithFormat:@" %@   ",locationSet];//locationSet;
    [location sizeToFit];
    
    y = y+180*RATIO;
    
    UILabel *roomonwerbg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    roomonwerbg.backgroundColor = LIGHT_BLUE;
    
    UILabel *roomOnwer = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, WIDTH, 45*RATIO)];
    roomOnwer.textAlignment = NSTextAlignmentLeft;
    roomOnwer.textColor = [UIColor blackColor];
    roomOnwer.text = onwer;
    
    y = y+65*RATIO;
    
    UILabel *pplbg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    pplbg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *pplTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 45*RATIO)];
    pplTitle.backgroundColor = [UIColor clearColor];
    pplTitle.text = @"缺幾人";
//    [pplTitle sizeToFit];
    pplTitle.textAlignment = NSTextAlignmentLeft;
    
    ppl = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-85*RATIO, y+45*RATIO/2, 70*RATIO, 20*RATIO)];
    ppl.backgroundColor = MAIN_COLOR;
    ppl.textColor = [UIColor whiteColor];
    ppl.text = pplNeed;
    ppl.textAlignment = NSTextAlignmentCenter;
    [ppl setClipsToBounds:YES];
    [ppl.layer setCornerRadius:20*RATIO/2];
    
    y = y+65*RATIO;
    
    UILabel *basebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    basebg.backgroundColor = LIGHT_BLUE;
    
    UILabel *baseTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 45*RATIO)];
    baseTitle.backgroundColor = [UIColor clearColor];
    baseTitle.text = @"底";
    baseTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *base = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-85*RATIO, y+10*RATIO, 70*RATIO, 45*RATIO)];
    base.text = [NSString stringWithFormat:@"$ %@",baseSet];
    [base setFont:[UIFont fontWithName:@"Helvetica-Bold" size:20*RATIO]];
    base.textColor = [UIColor blackColor];
    base.textAlignment = NSTextAlignmentRight;
    
    
    y = y+65*RATIO;
    
    UILabel *unitbg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    unitbg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *unitTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 45*RATIO)];
    unitTitle.backgroundColor = [UIColor clearColor];
    unitTitle.text = @"抬";
    unitTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *unit = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-85*RATIO, y+10*RATIO, 70*RATIO, 45*RATIO)];
    unit.text = [NSString stringWithFormat:@"$ %@",unitSet];
    [unit setFont:[UIFont fontWithName:@"Helvetica-Bold" size:20*RATIO]];
    unit.textColor = [UIColor blackColor];
    unit.textAlignment = NSTextAlignmentRight;
    
    y = y+65*RATIO;
    
    UILabel *timebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    timebg.backgroundColor = LIGHT_BLUE;
    
    UILabel *timeTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 45*RATIO)];
    timeTitle.backgroundColor = [UIColor clearColor];
    timeTitle.text = @"時間";
    timeTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *time = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-85*RATIO, y+45*RATIO/2, 70*RATIO, 20*RATIO)];
    time.text = timeSet;
    time.backgroundColor = MAIN_COLOR;
    time.textColor = [UIColor whiteColor];
    time.textAlignment = NSTextAlignmentCenter;
    [time setClipsToBounds:YES];
    [time.layer setCornerRadius:20*RATIO/2];
    
    
    y = y+65*RATIO;
    
    UILabel *circlebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    circlebg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *circleTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 45*RATIO)];
    circleTitle.backgroundColor = [UIColor clearColor];
    circleTitle.text = @"將數";
    circleTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *circle = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-85*RATIO, y+10*RATIO, 70*RATIO, 45*RATIO)];
    circle.text = [NSString stringWithFormat:@"%@",circleSet];
    circle.textColor = [UIColor blackColor];
    circle.textAlignment = NSTextAlignmentRight;
    
    
    y = y+65*RATIO;
    
    UILabel *tablebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    tablebg.backgroundColor = LIGHT_BLUE;
    
    UILabel *tableTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 45*RATIO)];
    tableTitle.backgroundColor = [UIColor clearColor];
    tableTitle.text = @"桌型";
    tableTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *table = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-85*RATIO, y+45*RATIO/2, 70*RATIO, 20*RATIO)];
    [table setClipsToBounds:YES];
    [table.layer setCornerRadius:20*RATIO/2];
    table.backgroundColor = MAIN_COLOR;
    table.textColor = [UIColor whiteColor];
    table.text = tableSet;
    table.textAlignment = NSTextAlignmentCenter;
    
    y = y+65*RATIO;
#warning member
    UILabel *memberbg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 80*RATIO)];
    memberbg.backgroundColor = LIGHTG_COLOR;
    
    y = y+80*RATIO;
    
    UILabel *cigarettebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 65*RATIO)];
    cigarettebg.backgroundColor = LIGHT_BLUE;
    
    UILabel *cigaretteTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 45*RATIO)];
    cigaretteTitle.backgroundColor = [UIColor clearColor];
    cigaretteTitle.text = @"菸";
    cigaretteTitle.textAlignment = NSTextAlignmentLeft;
    
    UILabel *cigarette = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-85*RATIO, y+45*RATIO/2, 70*RATIO, 20*RATIO)];
    [cigarette setClipsToBounds:YES];
    [cigarette.layer setCornerRadius:20*RATIO/2];
    cigarette.backgroundColor = MAIN_COLOR;
    cigarette.textColor = [UIColor whiteColor];
    cigarette.textAlignment = NSTextAlignmentCenter;
    cigarette.text = smokeSet;
    
    y = y+65*RATIO;
    
    UILabel *rulebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 100*RATIO)];
    rulebg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *ruleTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 80*RATIO, 20*RATIO)];
    ruleTitle.textAlignment = NSTextAlignmentLeft;
    ruleTitle.text = @"規則";
 
    y = y+115*RATIO;
    
    UIButton *checkBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH/2-60*RATIO, y , 120*RATIO, 50*RATIO)];
    [checkBtn setTitle:@"加入" forState:UIControlStateNormal];
    [checkBtn setTitleColor:MAIN_COLOR forState:UIControlStateNormal];
    [checkBtn.titleLabel setFont:[UIFont systemFontOfSize:20*RATIO]];
    [checkBtn.layer setBorderWidth:0.8f];
    [checkBtn.layer setCornerRadius:25*RATIO];
    [checkBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
    [checkBtn addTarget:self action:@selector(nextAction) forControlEvents:UIControlEventTouchUpInside];
    
    y = y+150*RATIO;
    
    NSLog(@"%f  %f",y,HEIGHT);
    
    UIScrollView *backGroud= [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
    
    
    [backGroud addSubview:head];
    [backGroud addSubview:popupCleanerIcon];
    [backGroud addSubview:location];
    [backGroud addSubview:roomonwerbg];
    [backGroud addSubview:roomOnwer];
    [backGroud addSubview:pplbg];
    [backGroud addSubview:pplTitle];
    [backGroud addSubview:ppl];
    [backGroud addSubview:basebg];
    [backGroud addSubview:baseTitle];
    [backGroud addSubview:base];
    [backGroud addSubview:unitbg];
    [backGroud addSubview:unitTitle];
    [backGroud addSubview:unit];
    [backGroud addSubview:timebg];
    [backGroud addSubview:time];
    [backGroud addSubview:timeTitle];
    [backGroud addSubview:circlebg];
    [backGroud addSubview:circleTitle];
    [backGroud addSubview:circle];
    [backGroud addSubview:tablebg];
    [backGroud addSubview:tableTitle];
    [backGroud addSubview:table];
    [backGroud addSubview:cigarettebg];
    [backGroud addSubview:cigaretteTitle];
    [backGroud addSubview:cigarette];
    [backGroud addSubview:rulebg];
    [backGroud addSubview:ruleTitle];
    [backGroud addSubview:checkBtn];

    [backGroud addSubview:checkBtn];
    
    [backGroud setShowsHorizontalScrollIndicator:NO];
    [backGroud setShowsVerticalScrollIndicator:NO];
    [backGroud setContentSize:CGSizeMake(WIDTH, y)];
    [backGroud setBounces:NO];
    [backGroud setScrollEnabled:YES];
    [self.view addSubview:backGroud];
    

}
#pragma mark - rong
-(void)nextView{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *roomName = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"RoomName"];
    NSString *roomNum =  [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"RoonNum"];
    [[RCIMClient sharedRCIMClient] joinGroup:[NSString stringWithFormat:@"%@",roomNum] groupName:roomName success:^(void){
        
        [self performSelectorOnMainThread:@selector(nextView2) withObject:nil waitUntilDone:YES];
        
    } error:nil];
    
}

-(void)nextView2{
    [appDelegate dismissPauseView];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *roomName = [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"RoomName"];
    NSString *roomNum =  [[defaults objectForKey:@"selectChatRoom"]objectForKey:@"RoonNum"];
    RongViewController *temp = [[RongViewController alloc]init];
    temp.targetId = [NSString stringWithFormat:@"%@",roomNum];
    [defaults setObject:temp.targetId forKey:@"groupid"];
    [defaults synchronize];
    temp.conversationType = ConversationType_GROUP;// 传入讨论组类型
    temp.title = roomName;
    [self.navigationController pushViewController:temp animated:YES];
}



@end
