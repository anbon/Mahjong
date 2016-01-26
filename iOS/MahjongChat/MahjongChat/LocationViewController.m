//
//  LocationViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/11/22.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "LocationViewController.h"
#import "AppDelegate.h"
#import "EMAsyncImageView.h"
#import "ChatRoomViewController.h"
#import "ProgressHUD.h"
#import "RongViewController.h"
#import "WaitForEmpowerViewController.h"
#import "MainViewController.h"
@interface LocationViewController ()

@end

@implementation LocationViewController{
    NSMutableArray *roomsum;
    AppDelegate *appDelegate;
    NSIndexPath *_selectedIndexPath;
//    UIBarButtonItem *filter;
    NSTimer *timer;
    UIView *filterView,*filterSubView;
    UISegmentedControl *segment;
    NSString *pplset,*lower,*higher;
    UITextField *moneyMin,*moneyMax;
}
#pragma mark - life Cycle
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)viewDidLoad {
//----------------------------------------------------------------------------------------------------------------------------------------
    [super viewDidLoad];
    roomsum = [NSMutableArray new];

    appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    self.tableView.frame = CGRectMake(0, 0, WIDTH, HEIGHT-WIDTH * (46.0f/320.0f)-64);
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
   
    self.view.translatesAutoresizingMaskIntoConstraints = NO;
    
    [self.view setBackgroundColor:[UIColor clearColor]];
    [self initByDuke];

    [self pullToRefresh];
    
    UIRefreshControl *refresh = [[UIRefreshControl alloc] init];
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:@"往下拉更新資料"];
    refresh.tintColor = [UIColor lightGrayColor];
    [refresh addTarget:self action:@selector(pullToRefresh) forControlEvents:UIControlEventValueChanged];
    self.refreshControl = refresh;

    
    appDelegate.mainViewController.filter = [[UIBarButtonItem alloc]initWithTitle:@"排序"
                                                                    style:UIBarButtonItemStylePlain
                                                                    target:self
                                                                    action:@selector(filterRoom)];
////設定filert 預設值
    pplset = @"3";
    lower = @"";
    higher =@"";
//設定filterView
    filterView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
    UIColor *line_grayColor = [UIColor colorWithWhite:0.6 alpha:0.4f];
    [filterView setBackgroundColor:line_grayColor];
    UITapGestureRecognizer *tapiew = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cancelview:)];
    [filterView addGestureRecognizer:tapiew];
    
    filterSubView = [[UIView alloc]initWithFrame:CGRectMake(WIDTH/2 - 185*RATIO, 100*RATIO, 370*RATIO, 350*RATIO)];
    [filterSubView.layer setBorderColor:[MAIN_COLOR CGColor]];
    [filterSubView.layer setBorderWidth:0.8f];
    [filterSubView.layer setCornerRadius:40*RATIO];
    filterSubView.backgroundColor = [UIColor whiteColor];
    [filterSubView setClipsToBounds:YES];
    UITapGestureRecognizer *tapSubview = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(filterSubViewCancel:)];
    [filterSubView addGestureRecognizer:tapSubview];
    
    UILabel *lab = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 370*RATIO, 50*RATIO)];
    lab.backgroundColor = MAIN_COLOR;
    
    UIImageView *logoimg = [[UIImageView alloc]initWithFrame:CGRectMake(115*RATIO, 5*RATIO, 40*RATIO,40*RATIO)];
    logoimg.contentMode = UIViewContentModeScaleAspectFill;
    logoimg.image = [UIImage imageNamed:@"logo.png"];
    
    //篩選條件
    UILabel *title = [[UILabel alloc]initWithFrame:CGRectMake(110*RATIO, 5*RATIO, 150*RATIO, 40*RATIO)];
    title.textColor = [UIColor whiteColor];
    title.text = @"篩選條件";
    title.textAlignment = NSTextAlignmentCenter;
    title.font = [UIFont systemFontOfSize:25*RATIO];
    
    UILabel *pplselect = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, 90*RATIO, 70*RATIO, 50*RATIO)];
    pplselect.textColor = [UIColor blackColor];
    pplselect.text = @"人數:";
    pplselect.textAlignment = NSTextAlignmentLeft;
    pplselect.font = [UIFont systemFontOfSize:20*RATIO];
    
    segment = [[UISegmentedControl alloc] initWithItems:@[@"正常", @"缺1~2人", @"缺一人"]];
    [segment setFrame:CGRectMake(155*RATIO, 90*RATIO, 200*RATIO, 50*RATIO)];
    [segment setTintColor:MAIN_COLOR];
    [segment setSelectedSegmentIndex:0];
    [segment.layer setCornerRadius:30*RATIO];
    [segment addTarget:self action:@selector(selectSegment:) forControlEvents:UIControlEventValueChanged];
    
    [segment setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor blackColor], NSFontAttributeName:[UIFont systemFontOfSize:16]} forState:UIControlStateNormal];
    
    UILabel *moneyselect = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, 200*RATIO, 40*RATIO, 50*RATIO)];
    moneyselect.textColor = [UIColor blackColor];
    moneyselect.text = @"抬:";
    moneyselect.textAlignment = NSTextAlignmentLeft;
    moneyselect.font = [UIFont systemFontOfSize:20*RATIO];
    
    moneyMin = [[UITextField alloc]initWithFrame:CGRectMake((370-285)*RATIO, 200*RATIO, 130*RATIO, 50*RATIO)];
    moneyMin.delegate = self;
    moneyMin.keyboardType = UIKeyboardTypeNumberPad;
    moneyMin.textAlignment = NSTextAlignmentCenter;
    moneyMin.placeholder = @"最小金額";
    
    UILabel *wave = [[UILabel alloc]initWithFrame:CGRectMake((370-155)*RATIO, 210*RATIO, 15*RATIO, 30*RATIO)];
    wave.textColor = [UIColor blackColor];
    wave.text = @"~";
    wave.textAlignment = NSTextAlignmentLeft;
    wave.font = [UIFont systemFontOfSize:20*RATIO];
    
    moneyMax = [[UITextField alloc]initWithFrame:CGRectMake((370-140)*RATIO, 200*RATIO, 130*RATIO, 50*RATIO)];
    moneyMax.delegate = self;
    moneyMax.keyboardType = UIKeyboardTypeNumberPad;
    moneyMax.textAlignment = NSTextAlignmentCenter;
    moneyMax.placeholder = @"最大金額";
    
    UIButton *confirmBtn = [[UIButton alloc]initWithFrame:CGRectMake(215*RATIO, 350*RATIO-55*RATIO, 100*RATIO, 40*RATIO)];
    [confirmBtn setTitle:@"確認" forState:UIControlStateNormal];
    [confirmBtn setTitleColor:MAIN_COLOR forState:UIControlStateNormal];
    [confirmBtn.titleLabel setFont:[UIFont systemFontOfSize:20*RATIO]];
    [confirmBtn setBackgroundColor:[UIColor whiteColor]];
    [confirmBtn.layer setBorderWidth:0.8f];
    [confirmBtn.layer setCornerRadius:20*RATIO];
    [confirmBtn.layer setBorderColor:[MAIN_COLOR CGColor]];
    [confirmBtn addTarget:self action:@selector(nextAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *PresetBtn = [[UIButton alloc]initWithFrame:CGRectMake(55*RATIO, 350*RATIO-55*RATIO, 100*RATIO, 40*RATIO)];
    [PresetBtn setTitle:@"預設" forState:UIControlStateNormal];
    [PresetBtn setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [PresetBtn.titleLabel setFont:[UIFont systemFontOfSize:20*RATIO]];
    [PresetBtn setBackgroundColor:[UIColor whiteColor]];
    [PresetBtn.layer setBorderWidth:0.8f];
    [PresetBtn.layer setCornerRadius:20*RATIO];
    [PresetBtn.layer setBorderColor:[[UIColor redColor] CGColor]];
    [PresetBtn addTarget:self action:@selector(filterPreset) forControlEvents:UIControlEventTouchUpInside];

    
    [filterSubView addSubview:segment];
    [filterSubView addSubview:lab];
    [filterSubView addSubview:title];
    [filterSubView addSubview:pplselect];
    [filterSubView addSubview:moneyselect];
    [filterSubView addSubview:confirmBtn];
    [filterSubView addSubview:moneyMax];
    [filterSubView addSubview:moneyMin];
    [filterSubView addSubview:wave];
    [filterSubView addSubview:PresetBtn];
    
    [filterView addSubview:filterSubView];
    
    
    
    
}
//----------------------------------------------------------------------------------------------------------------------------------------
- (void)didReceiveMemoryWarning {
//----------------------------------------------------------------------------------------------------------------------------------------
    [super didReceiveMemoryWarning];
}
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)viewWillAppear:(BOOL)animated{
//----------------------------------------------------------------------------------------------------------------------------------------
    [super viewWillAppear:animated];
    
}
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)viewWillDisappear:(BOOL)animated{
//----------------------------------------------------------------------------------------------------------------------------------------
    [super viewWillDisappear:animated];
}
#pragma mark - Segment select

-(void)selectSegment:(UISegmentedControl*)s{
    if(s.selectedSegmentIndex==0){
        //未清潔
        pplset = @"3";
    }else if(segment.selectedSegmentIndex==1){
        //已清潔
        pplset = @"2";
    }else if(segment.selectedSegmentIndex==2){
        //全部
        pplset = @"1";
    }
}

#pragma mark - API Delegate
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)networkError:(NSString *)err{
//----------------------------------------------------------------------------------------------------------------------------------------
    NSLog(@"networkError");
    [appDelegate dismissPauseView];
    UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"獲取房間資訊失敗" message:@"伺服器維修中,請洽詢客服人員" delegate:self cancelButtonTitle:@"確定" otherButtonTitles:nil];
    [alert show];
    [timer invalidate];
}
-(void)seedFinished:(NSDictionary *)dic{
    [appDelegate dismissPauseView];
    NSLog(@"seedfinished dic = %@",dic);
//    NSString *statusTemp = [NSString stringWithFormat:@"%@",[dic objectForKey:@"status"]];
//    if ([statusTemp isEqualToString:@"1"]) {
//        [roomsum removeAllObjects];
//        NSMutableArray *temp = [dic objectForKey:@"message"];
//        for (int i = 0; i < temp.count; i++) {
//            [roomsum addObject:[temp objectAtIndex:i]];
//        }
//    }else{
//        
//    }
//    
//    [self.refreshControl endRefreshing];
//    self.refreshControl.attributedTitle = [[NSAttributedString alloc] initWithString:@"下拉刷新"];
//    [self initByDuke];
    
}
#pragma mark - event response
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)addRoom{
//----------------------------------------------------------------------------------------------------------------------------------------
    NSLog(@"addroom");
}
- (void)pullToRefresh
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"accountID"];
    NSString *locationX = [defaults objectForKey:@"locationX"];
    NSString *locationY = [defaults objectForKey:@"locationY"];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn seed:@{@"user_ID":userID,@"locationX":locationX,@"locationY":locationY}];
    
    self.refreshControl.attributedTitle = [[NSAttributedString alloc] initWithString:@"更新中"];
    [timer invalidate];
    [self initializeTimer];

}
-(void)initializeTimer {
    NSLog(@"inittimer");
    float theInterval = 1.0/0.2;
    timer = [NSTimer scheduledTimerWithTimeInterval:theInterval
                                                target:self
                                              selector:@selector(timerConnSeed)
                                              userInfo:nil
                                               repeats:YES];
}
-(void)timerConnSeed{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"accountID"];
    NSString *locationX = [defaults objectForKey:@"locationX"];
    NSString *locationY = [defaults objectForKey:@"locationY"];
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
    [conn seed:@{@"user_ID":userID,@"locationX":locationX,@"locationY":locationY}];
//    [appDelegate initPauseView:@"更新中..請稍候.."];
}

#pragma mark - getters & setters
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)initByDuke{
//----------------------------------------------------------------------------------------------------------------------------------------
    [self.tableView reloadData];
}
#pragma mark - tableView
//----------------------------------------------------------------------------------------------------------------------------------------
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
//----------------------------------------------------------------------------------------------------------------------------------------
{
    return [roomsum count];
    
}
//----------------------------------------------------------------------------------------------------------------------------------------
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
//----------------------------------------------------------------------------------------------------------------------------------------
    static NSString *CellIdentifier = @"CellIdentifier";
    
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    
    
    
    UIView *view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, 110*RATIO)];
    
    UIImageView *popupCleanerIcon = [[UIImageView alloc] initWithFrame:CGRectMake(15*RATIO , 5*RATIO, 70*RATIO , 70*RATIO)];
    popupCleanerIcon.contentMode = UIViewContentModeScaleAspectFill;
    [popupCleanerIcon.layer setCornerRadius:35*RATIO];
    popupCleanerIcon.clipsToBounds = YES;
    popupCleanerIcon.backgroundColor = [UIColor clearColor];
    //icon
    NSString *iconurl = [NSString stringWithFormat:@"%@",[[[[roomsum objectAtIndex:indexPath.row] objectForKey:@"users"] objectAtIndex:0] objectForKey:@"photo"]];
    [popupCleanerIcon sd_setImageWithURL:[NSURL URLWithString:iconurl] placeholderImage:[UIImage imageNamed:@"photo.png"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
//        popupCleanerIcon.alpha = 0.3;
//        [UIView animateWithDuration:0.4f animations:^{
//            popupCleanerIcon.alpha = 1.0f;
//        }];
    }];
    
    
    UIImageView *people = [[UIImageView alloc]initWithFrame:CGRectMake(95*RATIO, 10*RATIO, 50*RATIO, 50*RATIO)];
    if ([[[roomsum objectAtIndex:indexPath.row]objectForKey:@"count"]isEqualToString:@"3"]) {
        [people setImage:[UIImage imageNamed:@"need3.png"]];
    }else if ([[[roomsum objectAtIndex:indexPath.row]objectForKey:@"people"]isEqualToString:@"2"]){
        [people setImage:[UIImage imageNamed:@"need2.png"]];
    }else if ([[[roomsum objectAtIndex:indexPath.row]objectForKey:@"people"]isEqualToString:@"1"]){
        [people setImage:[UIImage imageNamed:@"need1.png"]];
    }
    [people setContentMode:UIViewContentModeScaleAspectFill];
    
    UILabel *location = [[UILabel alloc]initWithFrame:CGRectMake(170*RATIO, 10*RATIO, 170*RATIO, 30)];
    location.text = [[roomsum objectAtIndex:indexPath.row] objectForKey:@"RoomName"];
    location.textColor = [UIColor blackColor];
    [location setFont:[UIFont systemFontOfSize:25*RATIO]];
    location.textAlignment = NSTextAlignmentCenter;
    [location sizeToFit];
    location.baselineAdjustment = UIBaselineAdjustmentAlignCenters;
    location.adjustsFontSizeToFitWidth = YES;
    
    
    
    UILabel *distance = [[UILabel alloc]initWithFrame:CGRectMake(WIDTH-70*RATIO, 13*RATIO, 60*RATIO, 30)];
    NSString *distancetemp = [NSString stringWithFormat:@"%@km",[[roomsum objectAtIndex:indexPath.row] objectForKey:@"dis"]];
    distance.text = distancetemp;
    distance.textColor = [UIColor blackColor];
    distance.textAlignment = NSTextAlignmentLeft;
    [distance sizeToFit];
    
    
    UILabel *name = [[UILabel alloc]initWithFrame:CGRectMake(15*RATIO, 75*RATIO, 70*RATIO, 30*RATIO)];
    name.text = [[[[roomsum objectAtIndex:indexPath.row] objectForKey:@"users"] objectAtIndex:0] objectForKey:@"Uname"];
    name.textColor = [UIColor blackColor];
    name.textAlignment = NSTextAlignmentCenter;
    name.baselineAdjustment = UIBaselineAdjustmentAlignCenters;
    name.adjustsFontSizeToFitWidth = YES;
    
    
    UIImageView *baseimg = [[UIImageView alloc]initWithFrame:CGRectMake(95*RATIO, 60*RATIO, 30*RATIO, 30*RATIO)];
    [baseimg setImage:[UIImage imageNamed:@"base.png"]];
    [baseimg setContentMode:UIViewContentModeScaleAspectFill];
    
    
    UILabel *base = [[UILabel alloc]initWithFrame:CGRectMake(130*RATIO, 65*RATIO, 60*RATIO, 30*RATIO)];
    NSString *basetemp = [NSString stringWithFormat:@"$%@",[[roomsum objectAtIndex:indexPath.row] objectForKey:@"base"]];
    base.text = basetemp;
    base.textColor = [UIColor blackColor];
    base.textAlignment = NSTextAlignmentCenter;
    base.baselineAdjustment = UIBaselineAdjustmentAlignCenters;
    base.adjustsFontSizeToFitWidth = YES;
    [base sizeToFit];
    
    
    UILabel *line = [[UILabel alloc]initWithFrame:CGRectMake(192*RATIO, 65*RATIO,2*RATIO, 30*RATIO)];
    line.backgroundColor = MAIN_COLOR;
    
    
    UILabel *unitimg = [[UILabel alloc]initWithFrame:CGRectMake(200*RATIO, 65*RATIO, 30*RATIO, 30*RATIO)];
    unitimg.text = @"抬";
    unitimg.textColor = [UIColor blackColor];
    unitimg.textAlignment = NSTextAlignmentLeft;
    [unitimg sizeToFit];
    
    
    UILabel *unit = [[UILabel alloc]initWithFrame:CGRectMake(230*RATIO, 65*RATIO, 60*RATIO, 30*RATIO)];
     NSString *unittemp = [NSString stringWithFormat:@"$%@",[[roomsum objectAtIndex:indexPath.row] objectForKey:@"unit"]];
    unit.text = unittemp;
    unit.textColor = [UIColor blackColor];
    unit.textAlignment = NSTextAlignmentLeft;
    unit.baselineAdjustment = UIBaselineAdjustmentAlignCenters;
    unit.adjustsFontSizeToFitWidth = YES;
    [unit sizeToFit];
    
    
    UIImageView *timeimg = [[UIImageView alloc]initWithFrame:CGRectMake(290*RATIO, 65*RATIO, 20*RATIO, 20*RATIO)];
    [timeimg setImage:[UIImage imageNamed:@"time.png"]];
    [timeimg setContentMode:UIViewContentModeScaleAspectFill];
    
    
    UILabel *time = [[UILabel alloc]initWithFrame:CGRectMake(320*RATIO, 65*RATIO, 60*RATIO, 30*RATIO)];
    time.text = [[roomsum objectAtIndex:indexPath.row] objectForKey:@"time"];
    time.textColor = [UIColor blackColor];
    time.textAlignment = NSTextAlignmentLeft;
    time.baselineAdjustment = UIBaselineAdjustmentAlignCenters;
    time.adjustsFontSizeToFitWidth = YES;
    [time sizeToFit];
    
    
    
    [view addSubview:people];
    [view addSubview:popupCleanerIcon];
    [view addSubview:location];
    [view addSubview:name];
    [view addSubview:time];
    [view addSubview:timeimg];
    [view addSubview:baseimg];
    [view addSubview:line];
    [view addSubview:base];
    [view addSubview:unitimg];
    [view addSubview:unit];
    [view addSubview:distance];
    [cell.contentView addSubview:view];
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    cell.selectionStyle = UITableViewCellSelectionStyleBlue;
    
    return cell;
}

//----------------------------------------------------------------------------------------------------------------------------------------
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
//----------------------------------------------------------------------------------------------------------------------------------------
{
    return 110*RATIO;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"%@",[[roomsum objectAtIndex:indexPath.row] objectForKey:@"name"]);
    _selectedIndexPath=indexPath;

    [[NSUserDefaults standardUserDefaults]setObject:[roomsum objectAtIndex:indexPath.row] forKey:@"selectChatRoom"];
    NSLog(@"roomsum select = %@",[[NSUserDefaults standardUserDefaults]valueForKey:@"selectChatRoom"]);
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:[roomsum objectAtIndex:indexPath.row] forKey:@"selectChatRoom"];
    [defaults synchronize];
    WaitForEmpowerViewController *emp = [WaitForEmpowerViewController new];
    [self.navigationController pushViewController:emp animated:YES];
    
}

#pragma mark - filterView action&event
-(void)filterRoom{
    [appDelegate.mainNavi.view addSubview:filterView];
}
-(void)cancelview:(UIGestureRecognizer*)sender{
    [filterView removeFromSuperview];
    [moneyMin resignFirstResponder];
    [moneyMax resignFirstResponder];
}
-(void)filterSubViewCancel:(UIGestureRecognizer*)sender{
    [moneyMin resignFirstResponder];
    [moneyMax resignFirstResponder];
}
-(void)filterConfirm{
    
}
-(void)filterPreset{
    [segment setSelectedSegmentIndex:0];
    moneyMax.text = @"";
    moneyMin.text = @"";
    pplset = @"3";
}
#pragma mark - TextDelegate Delegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    // allow backspace
    if (!string.length){
        return YES;
    }
    
    // Prevent invalid character input, if keyboard is numberpad
    if (textField.keyboardType == UIKeyboardTypeNumberPad){
        if ([string rangeOfCharacterFromSet:[[NSCharacterSet decimalDigitCharacterSet] invertedSet]].location != NSNotFound){
            // BasicAlert(@"", @"This field accepts only numeric entries.");
            return NO;
        }
    }
    
    // verify max length has not been exceeded
    NSString *updatedText = [textField.text stringByReplacingCharactersInRange:range withString:string];
    
    if (updatedText.length > 6){ // 4 was chosen for SSN verification
        // suppress the max length message only when the user is typing
        // easy: pasted data has a length greater than 1; who copy/pastes one character?
        if (string.length > 1){
            // BasicAlert(@"", @"This field accepts a maximum of 4 characters.");
        }
        
        return NO;
    }
    
    // only enable the OK/submit button if they have entered all numbers for the last four of their SSN (prevents early submissions/trips to authentication server)
    //self.answerButton.enabled = (updatedText.length == 4);
    
    return YES;
}



@end
