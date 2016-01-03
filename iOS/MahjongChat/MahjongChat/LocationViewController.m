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
@interface LocationViewController ()

@end

@implementation LocationViewController{
    NSMutableArray *roomsum;
    AppDelegate *appDelegate;
    NSIndexPath *_selectedIndexPath;
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
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"accountID"];
    
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
#warning need location X & Y
    [conn seed:@{@"user_ID":userID,@"locationX":@"24.9529757",@"locationY":@"121.54284089999999"}];
    
    UIRefreshControl *refresh = [[UIRefreshControl alloc] init];
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:@"往下拉更新資料"];
    refresh.tintColor = [UIColor lightGrayColor];
    [refresh addTarget:self action:@selector(pullToRefresh) forControlEvents:UIControlEventValueChanged];
    self.refreshControl = refresh;
    
    
    
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


#pragma mark - API Delegate
//----------------------------------------------------------------------------------------------------------------------------------------
-(void)networkError:(NSString *)err{
//----------------------------------------------------------------------------------------------------------------------------------------
    NSLog(@"networkError");
}
-(void)seedFinished:(NSDictionary *)dic{
    NSLog(@"%@",dic);
    
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        [roomsum removeAllObjects];
        NSMutableArray *temp = [dic objectForKey:@"message"];
        for (int i = 0; i < temp.count; i++) {
            [roomsum addObject:[temp objectAtIndex:i]];
        }
    }else{
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"獲取房間資訊失敗" message:@"請下拉重新更新" delegate:self cancelButtonTitle:@"確定" otherButtonTitles:nil];
        [alert show];
    }
    
    [self.refreshControl endRefreshing];
    self.refreshControl.attributedTitle = [[NSAttributedString alloc] initWithString:@"下拉刷新"];
    [self initByDuke];
    
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
    APIConn *conn=[[APIConn alloc] init];
    conn.apiDelegate=self;
#warning need location X & Y
    [conn seed:@{@"user_ID":userID,@"locationX":@"25.0187083",@"locationY":@"121.5540413"}];
    self.refreshControl.attributedTitle = [[NSAttributedString alloc] initWithString:@"更新中"];
    

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
        popupCleanerIcon.alpha = 0.3;
        [UIView animateWithDuration:0.4f animations:^{
            popupCleanerIcon.alpha = 1.0f;
        }];
    }];
    
    
    UIImageView *people = [[UIImageView alloc]initWithFrame:CGRectMake(95*RATIO, 10*RATIO, 50*RATIO, 50*RATIO)];
    if ([[[roomsum objectAtIndex:indexPath.row]objectForKey:@"people"]isEqualToString:@"3"]) {
        [people setImage:[UIImage imageNamed:@"need3.png"]];
    }else if ([[[roomsum objectAtIndex:indexPath.row]objectForKey:@"people"]isEqualToString:@"2"]){
        [people setImage:[UIImage imageNamed:@"need2.png"]];
    }else if ([[[roomsum objectAtIndex:indexPath.row]objectForKey:@"people"]isEqualToString:@"1"]){
        [people setImage:[UIImage imageNamed:@"need1.png"]];
    }
    [people setContentMode:UIViewContentModeScaleAspectFill];
    
    UILabel *location = [[UILabel alloc]initWithFrame:CGRectMake(150*RATIO, 10*RATIO, 190*RATIO, 30)];
    location.text = [[roomsum objectAtIndex:indexPath.row] objectForKey:@"RoomName"];
    location.textColor = [UIColor blackColor];
    [location setFont:[UIFont systemFontOfSize:25*RATIO]];
    location.textAlignment = NSTextAlignmentCenter;
//    [location sizeToFit];
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
////----------------------------------------------------------------------------------------------------------------------------------------
//-(void)tableView:(UITableView *)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath
////----------------------------------------------------------------------------------------------------------------------------------------
//{
//    NSLog(@"%@",indexPath);
//}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"%@",[[roomsum objectAtIndex:indexPath.row] objectForKey:@"name"]);
    _selectedIndexPath=indexPath;
//    [self nextView];
//    [appDelegate initPauseView:@"進入房間中..請稍候.."];

    [[NSUserDefaults standardUserDefaults]setObject:[roomsum objectAtIndex:indexPath.row] forKey:@"selectChatRoom"];
    NSLog(@"roomsum select = %@",[[NSUserDefaults standardUserDefaults]valueForKey:@"selectChatRoom"]);
    
    WaitForEmpowerViewController *emp = [WaitForEmpowerViewController new];
    [self.navigationController pushViewController:emp animated:YES];
    
}



#pragma mark - Location Delegate



@end
