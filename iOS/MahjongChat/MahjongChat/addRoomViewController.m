//
//  addRoomViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/12/1.
//  Copyright © 2015年 Duke. All rights reserved.
//
#import "AppDelegate.h"
#import "addRoomViewController.h"
#import "APIConn.h"
#import "ZHPickView.h"
#import "RongViewController.h"
@interface addRoomViewController ()


@end

@implementation addRoomViewController
{
    ZHPickView *pplPicker, *timePicker;
    UIButton *cigaretteBtn,*tableBtn,*pplBtn,*timeBtn;
    BOOL tableedit,smokeedit;
    int pplresult,tableresult,smokeresult;
    UITextField *location,*base,*unit,*circle,*rule;
    AppDelegate *appDelegate;
    NSString *timeresult;
}
#pragma mark - life Cycle
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)viewDidLoad
//----------------------------------------------------------------------------------------------------------------------------------------
{
    [super viewDidLoad];
    appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    tableedit = NO;
    smokeedit = NO;
    pplresult = 3;
    timeresult = @"0AM";
    tableresult = 0;//手動0 自動1
    smokeresult = 0;//不菸0 菸1
    pplPicker = [[ZHPickView alloc] initPickviewWithArray:@[@"3", @"2", @"1"] isHaveNavControler:NO];
    [pplPicker setDelegate:self];
    
    
    timePicker = [[ZHPickView alloc] initPickviewWithArray:@[@"0AM",@"1AM",@"2AM",@"3AM",@"4AM",@"5AM",@"6AM",@"7AM",@"8AM",@"9AM",@"10AM",@"11AM",@"12PM",@"1PM",@"2PM",@"3PM",@"4PM",@"5PM",@"6PM",@"7PM",@"8PM",@"9PM",@"10PM",@"11PM"] isHaveNavControler:NO];
    [timePicker setDelegate:self];
    [self initByDuke];
}
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)didReceiveMemoryWarning
//----------------------------------------------------------------------------------------------------------------------------------------
{
    [super didReceiveMemoryWarning];
}
#pragma mark - API Delegate
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)networkError:(NSString *)err
//----------------------------------------------------------------------------------------------------------------------------------------
{
    NSLog(@"networkError");
}
-(void)creatChatFinished:(NSDictionary *)dic{
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        NSLog(@"success");
        [[NSUserDefaults standardUserDefaults]setObject:[dic objectForKey:@"RoomNum"] forKey:@"RoomNum"];
        NSLog(@"%@ roomnum=",[[NSUserDefaults standardUserDefaults] valueForKey:@"RoomNum"]);
        [self nextView];
    }else if ([[dic objectForKey:@"status"]isEqualToString:@"0"]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"創立房間失敗" message:@"" delegate:nil cancelButtonTitle:@"我知道了" otherButtonTitles:nil];
        
        [alert show];
    }
}
#pragma mark - event response
-(void)pplset
{
    [self.view endEditing:YES];
    [pplPicker show];
}
-(void)timeset
{
    [self.view endEditing:YES];
    [timePicker show];
}
-(void)tableset
{
    if (tableedit == NO) {//手動->自動
        [tableBtn setTitle:@"自動" forState:UIControlStateNormal];
        tableedit = YES;
        tableresult = 1;
    }else{
        [tableBtn setTitle:@"手動" forState:UIControlStateNormal];
        tableedit = NO;
        tableresult = 0;
    }
}
-(void)smokeset
{
    if (smokeedit == NO) {//不菸->菸
        [cigaretteBtn setTitle:@"菸" forState:UIControlStateNormal];
        smokeedit = YES;
        smokeresult = 1;
    }else{
        [cigaretteBtn setTitle:@"不菸" forState:UIControlStateNormal];
        smokeedit = NO;
        smokeresult = 0;
    }
}
-(void)nextAction
{
    if ([location.text isEqualToString:@""] || [base.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"創立房間失敗!" message:@"您還有欄位尚未填寫" delegate:nil cancelButtonTitle:@"確定" otherButtonTitles:nil];
        
        [alert show];
    }else if ([unit.text isEqualToString:@""] || [circle.text isEqualToString:@""]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"創立房間失敗!" message:@"您還有欄位尚未填寫" delegate:nil cancelButtonTitle:@"確定" otherButtonTitles:nil];
        
        [alert show];
    }else if ([rule.text isEqualToString:@""]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"創立房間失敗!" message:@"您還有欄位尚未填寫" delegate:nil cancelButtonTitle:@"確定" otherButtonTitles:nil];
        
        [alert show];
    }else{
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *userID = [defaults objectForKey:@"accountID"];
        NSString *locationtemp = @"";
        
        NSString *timetemp = timeresult;
        NSString *people = [NSString stringWithFormat:@"%d",pplresult];
        NSString *type = [NSString stringWithFormat:@"%d",tableresult];
        NSString *cigar = [NSString stringWithFormat:@"%d",smokeresult];
        APIConn *conn=[[APIConn alloc] init];
        conn.apiDelegate=self;
        [conn creatChat:@{@"user_Id":userID,@"name":location.text,@"base":base.text,@"unit":unit.text,@"circle":circle.text,@"time":timetemp,@"location":locationtemp,@"people":people,@"type":type,@"cigarette":cigar,@"rule":rule.text}];
        
    }
    
    
}
#pragma mark - ZHPick Delegate

-(void)toobarDonBtnHaveClick:(ZHPickView *)pickView resultString:(NSString *)resultString{
    if (pickView == pplPicker) {
        [self pplSelectFinished:resultString];
    }else if(pickView == timePicker){
        [self timeSelectFinished:resultString];
    }
}

-(void)pickerCancel{
    
}

-(void)pplSelectFinished:(NSString*)ppl{
    [pplBtn setTitle:ppl forState:UIControlStateNormal];
    timeresult = ppl;
    
}

-(void)timeSelectFinished:(NSString*)time{
    [timeBtn setTitle:time forState:UIControlStateNormal];
    
}

#pragma mark - TextField Delegate
//----------------------------------------------------------------------------------------------------------------------------------------
- (BOOL)textFieldShouldReturn:(UITextField *)textField
//----------------------------------------------------------------------------------------------------------------------------------------
{
    [textField resignFirstResponder];
    return YES;
}
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)textFieldDidBeginEditing:(UITextField *)textField
//----------------------------------------------------------------------------------------------------------------------------------------
{
    
}
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)textFieldDidEndEditing:(UITextField *)textField
//----------------------------------------------------------------------------------------------------------------------------------------
{
}
#pragma mark - getters & setters
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)initByDuke
//----------------------------------------------------------------------------------------------------------------------------------------
{
    CGFloat y = 0;
    
    
    
    UIImageView *head = [[UIImageView alloc] initWithFrame:CGRectMake(0-20*RATIO, y, WIDTH+40*RATIO, 180*RATIO)];
    [head setImage:[UIImage imageNamed:@"info_bg.png"]];
    [head setContentMode:UIViewContentModeScaleAspectFill];
    
    y = y+180*RATIO;
    
    UILabel *locationbg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    locationbg.backgroundColor = LIGHT_BLUE;
    
    location = [[UITextField alloc]initWithFrame:CGRectMake(15*RATIO, y, WIDTH, 35*RATIO)];
    location.placeholder = @"地點";
    location.delegate = self;
    
    y = y+40*RATIO;
    
    UILabel *pplbg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    pplbg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *pplTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 40*RATIO, 70*RATIO)];
    pplTitle.backgroundColor = [UIColor clearColor];
    pplTitle.text = @"缺幾人";
    [pplTitle sizeToFit];
    pplTitle.textAlignment = NSTextAlignmentLeft;
    
    pplBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH-90*RATIO, y+10*RATIO, 70*RATIO, 20*RATIO)];
    [pplBtn.layer setCornerRadius:20*RATIO/2];
    pplBtn.backgroundColor = MAIN_COLOR;
    [pplBtn setTitle:@"3" forState:UIControlStateNormal];
    pplBtn.titleLabel.textColor = [UIColor whiteColor];
    [pplBtn addTarget:self action:@selector(pplset) forControlEvents:UIControlEventTouchUpInside];
    
    y = y+40*RATIO;
    
    UILabel *basebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    basebg.backgroundColor = LIGHT_BLUE;
    
    base = [[UITextField alloc]initWithFrame:CGRectMake(15*RATIO, y, WIDTH, 35*RATIO)];
    base.placeholder = @"底";
    base.delegate = self;
    
    y = y+40*RATIO;
    
    UILabel *unitbg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    unitbg.backgroundColor = LIGHTG_COLOR;
    
    unit = [[UITextField alloc]initWithFrame:CGRectMake(15*RATIO, y, WIDTH, 35*RATIO)];
    unit.placeholder = @"抬";
    unit.delegate = self;
    
    y = y+40*RATIO;
    
    UILabel *timebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    timebg.backgroundColor = LIGHT_BLUE;
    
    UILabel *timeTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 40*RATIO, 20*RATIO)];
    timeTitle.backgroundColor = [UIColor clearColor];
    timeTitle.text = @"時間";
    timeTitle.textAlignment = NSTextAlignmentLeft;
    
    timeBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH-90*RATIO, y+10*RATIO, 70*RATIO, 20*RATIO)];
    [timeBtn.layer setCornerRadius:20*RATIO/2];
    timeBtn.backgroundColor = MAIN_COLOR;
    [timeBtn setTitle:@"0AM" forState:UIControlStateNormal];
    timeBtn.titleLabel.textColor = [UIColor whiteColor];
    [timeBtn addTarget:self action:@selector(timeset) forControlEvents:UIControlEventTouchUpInside];
    
    y = y+40*RATIO;
    
    UILabel *circlebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    circlebg.backgroundColor = LIGHTG_COLOR;
    
    circle = [[UITextField alloc]initWithFrame:CGRectMake(15*RATIO, y, WIDTH, 35*RATIO)];
    circle.placeholder = @"將數";
    circle.delegate = self;
    
    y = y+40*RATIO;
    
    UILabel *tablebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    tablebg.backgroundColor = LIGHT_BLUE;
    
    UILabel *tableTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 40*RATIO, 20*RATIO)];
    tableTitle.backgroundColor = [UIColor clearColor];
    tableTitle.text = @"桌型";
    tableTitle.textAlignment = NSTextAlignmentLeft;
    
    tableBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH-90*RATIO, y+10*RATIO, 70*RATIO, 20*RATIO)];
    [tableBtn.layer setCornerRadius:20*RATIO/2];
    tableBtn.backgroundColor = MAIN_COLOR;
    [tableBtn setTitle:@"手動" forState:UIControlStateNormal];
    tableBtn.titleLabel.textColor = [UIColor whiteColor];
    [tableBtn addTarget:self action:@selector(tableset) forControlEvents:UIControlEventTouchUpInside];
    
    y = y+40*RATIO;
    
    UILabel *cigarettebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 40*RATIO)];
    cigarettebg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *cigaretteTitle = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, y+10*RATIO, 40*RATIO, 20*RATIO)];
    cigaretteTitle.backgroundColor = [UIColor clearColor];
    cigaretteTitle.text = @"菸";
    cigaretteTitle.textAlignment = NSTextAlignmentLeft;
    
    cigaretteBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH-90*RATIO, y+10*RATIO, 70*RATIO, 20*RATIO)];
    [cigaretteBtn.layer setCornerRadius:20*RATIO/2];
    cigaretteBtn.backgroundColor = MAIN_COLOR;
    [cigaretteBtn setTitle:@"不菸" forState:UIControlStateNormal];
    cigaretteBtn.titleLabel.textColor = [UIColor whiteColor];
    [cigaretteBtn addTarget:self action:@selector(smokeset) forControlEvents:UIControlEventTouchUpInside];
    
    y = y+40*RATIO;
    
    UILabel *rulebg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 60*RATIO)];
    rulebg.backgroundColor = LIGHT_BLUE;
    
    rule = [[UITextField alloc]initWithFrame:CGRectMake(15*RATIO, y, WIDTH, 70*RATIO)];
    rule.placeholder = @"規則";
    rule.delegate = self;
    
    y = y+70*RATIO;
    
    UIButton *checkBtn = [[UIButton alloc]initWithFrame:CGRectMake(WIDTH/2-60*RATIO, y , 120*RATIO, 50*RATIO)];
    [checkBtn setTitle:@"確認" forState:UIControlStateNormal];
//    checkBtn.titleLabel.textColor = MAIN_COLOR;
//    checkBtn.tintColor = MAIN_COLOR;
    [checkBtn setTitleColor:MAIN_COLOR forState:UIControlStateNormal];
    [checkBtn.titleLabel setFont:[UIFont systemFontOfSize:20*RATIO]];
    [checkBtn.layer setBorderWidth:0.8f];
    [checkBtn.layer setCornerRadius:25*RATIO];
    [checkBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
    [checkBtn addTarget:self action:@selector(nextAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIScrollView *backGroud= [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
    
    
    [backGroud addSubview:head];
    [backGroud addSubview:locationbg];
    [backGroud addSubview:pplbg];
    [backGroud addSubview:pplTitle];
    [backGroud addSubview:pplBtn];
    [backGroud addSubview:basebg];
    [backGroud addSubview:unitbg];
    [backGroud addSubview:timebg];
    [backGroud addSubview:timeTitle];
    [backGroud addSubview:timeBtn];
    [backGroud addSubview:circlebg];
    [backGroud addSubview:tablebg];
    [backGroud addSubview:tableTitle];
    [backGroud addSubview:tableBtn];
    [backGroud addSubview:cigarettebg];
    [backGroud addSubview:cigaretteTitle];
    [backGroud addSubview:cigaretteBtn];
    [backGroud addSubview:rulebg];
    
    [backGroud addSubview:location];
    [backGroud addSubview:base];
    [backGroud addSubview:unit];
    [backGroud addSubview:circle];
    [backGroud addSubview:rule];
    [backGroud addSubview:checkBtn];
    
    [backGroud setShowsHorizontalScrollIndicator:NO];
    [backGroud setShowsVerticalScrollIndicator:NO];
    [backGroud setContentSize:CGSizeMake(WIDTH, y)];
    [backGroud setScrollEnabled:YES];
    [self.view addSubview:backGroud];
    
}
#pragma mark - rong
-(void)nextView{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *group = [defaults objectForKey:@"RoomNum"];
    NSString *roomname = location.text;
    NSLog(@"group = %@,roomname= %@",group,roomname);
    [[RCIMClient sharedRCIMClient] joinGroup:[NSString stringWithFormat:@"%@",group] groupName:roomname success:^(void){
        
        [self performSelectorOnMainThread:@selector(nextView2) withObject:nil waitUntilDone:YES];
        
    } error:nil];
    
}

-(void)nextView2{
    [appDelegate dismissPauseView];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *roomname = location.text;
    NSString *group = [defaults objectForKey:@"RoomNum"];
    NSLog(@"group = %@,roomname= %@",group,roomname);
    RongViewController *temp = [[RongViewController alloc]init];
    temp.targetId = [NSString stringWithFormat:@"%@",group];
    [defaults setObject:temp.targetId forKey:@"groupid"];
    [defaults synchronize];
    temp.conversationType = ConversationType_GROUP;// 传入讨论组类型
    temp.title = roomname;
    [self.navigationController pushViewController:temp animated:YES];
}



@end
