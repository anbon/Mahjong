//
//  ProfileViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/11/22.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "ProfileViewController.h"
#import "AppDelegate.h"
#import "CropImageViewController.h"
#import "MainViewController.h"
#import "ZHPickView.h"
#import "TQStarRatingView.h"
@interface ProfileViewController ()

@end

@implementation ProfileViewController
{
    UIBarButtonItem *editing;
    UILabel *sex,*nickName,*agetext,*locationXY;
    UITextField *nickNameText,*ageText,*locationText;
    BOOL editStatus;
    UIButton *changePhoto,*sexText;
    AppDelegate *appdelegate;
    ZHPickView *sexPicker;
    NSString *accountID;
    UIImageView *popupCleanerIcon;
    TQStarRatingView *starRatingView;
    float rating;
}
#pragma mark - life Cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    editStatus = NO;
    
    
    UIButton *imageButtonForEdit = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 50, 22)];
    [imageButtonForEdit setBackgroundImage:[UIImage imageNamed:@"confirm6.png"] forState:UIControlStateNormal];
    [imageButtonForEdit addTarget:self action:@selector(editProfile) forControlEvents:UIControlEventTouchUpInside];
    editing = [[UIBarButtonItem alloc] initWithCustomView:imageButtonForEdit];
    
    appdelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    sexPicker = [[ZHPickView alloc] initPickviewWithArray:@[@"男", @"女"] isHaveNavControler:NO];
    [sexPicker setDelegate:self];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    accountID = [defaults objectForKey:@"accountID"];
    rating = [[defaults objectForKey:@"accountRate"] intValue];
    NSLog(@"rating = %f",rating);
    [self initByDuke];
    [sexText setHidden:YES];
    [nickNameText setHidden:YES];
    [ageText setHidden:YES];
    [changePhoto setHidden:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    NSLog(@"pro view will aorear");
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *name = [defaults objectForKey:@"accountUsername"];
    NSString *gender = [defaults objectForKey:@"accountGender"];
    NSString *age = [defaults objectForKey:@"accountAge"];
    NSString *photo = [defaults objectForKey:@"accountPhoto"];
    if ([gender isEqualToString:@"0"]) {
        sex.text = @"女";
    }else{
        sex.text = @"男";
    }
    nickName.text = name;
    agetext.text = age;
    
    [popupCleanerIcon sd_setImageWithURL:[NSURL URLWithString:photo] placeholderImage:[UIImage imageNamed:@"photo.png"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        popupCleanerIcon.alpha = 0.3;
        [UIView animateWithDuration:0.4f animations:^{
            popupCleanerIcon.alpha = 1.0f;
        }];
    }];

    
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    NSString *sextemp = sexText.titleLabel.text;
    [sexText setTitle:sextemp forState:UIControlStateNormal];
    [sex setHidden:NO];
    [sexText setHidden:YES];
    
    NSString *nickNametemp = nickNameText.text;
    [nickName setText:nickNametemp];
    [nickName setHidden:NO];
    [nickNameText setHidden:YES];
    
    NSString *agetemp = ageText.text;
    [agetext setText:agetemp];
    [agetext setHidden:NO];
    [ageText setHidden:YES];
    
    
    
    [sexText resignFirstResponder];
    [nickNameText resignFirstResponder];
    [ageText resignFirstResponder];
    [changePhoto setHidden:YES];
    editStatus = NO;
    appdelegate.mainViewController.imageButtonForEdit.selected = NO;

}
#pragma mark - API Delegate
-(void)networkError:(NSString *)err{
    [appdelegate dismissPauseView];
}
-(void)UserInfoFinished:(NSDictionary *)dic{
    [appdelegate dismissPauseView];
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"個人資料更新成功" message:@"" delegate:self cancelButtonTitle:@"確定" otherButtonTitles:nil];
        [alert show];
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:nickName.text forKey:@"accountUsername"];
        [defaults setObject:sex.text forKey:@"accountGender"];
        [defaults setObject:agetext.text forKey:@"accountAge"];
        [defaults synchronize];
        
    }else{
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *name = [defaults objectForKey:@"accountUsername"];
        NSString *gender = [defaults objectForKey:@"accountGender"];
        NSString *age = [defaults objectForKey:@"accountAge"];
        if ([gender isEqualToString:@"0"]) {
            sex.text = @"女";
        }else{
            sex.text = @"男";
        }
        nickName.text = name;
        agetext.text = age;
    }
}
#pragma mark - event response
-(void)editProfile{
    if (editStatus == NO) {
        NSLog(@"no>YES");
        
        sexText.titleLabel.text = sex.text;
        [sex setHidden:YES];
        [sexText setHidden:NO];
        
        [nickNameText setText:nickName.text];
        [nickName setHidden:YES];
        [nickNameText setHidden:NO];
        
        [ageText setText:agetext.text];
        [agetext setHidden:YES];
        [ageText setHidden:NO];
        
        
        [changePhoto setHidden:NO];
        editStatus = YES;
        NSLog(@"bar item  = editing");
        
        
        
        
    }else{
        
        NSLog(@"YES>NO");
        NSString *sextemp = sexText.titleLabel.text;
        [sexText setTitle:sextemp forState:UIControlStateNormal];
        [sex setHidden:NO];
        [sexText setHidden:YES];
        
        NSString *nickNametemp = nickNameText.text;
        [nickName setText:nickNametemp];
        [nickName setHidden:NO];
        [nickNameText setHidden:YES];
        
        NSString *agetemp = ageText.text;
        [agetext setText:agetemp];
        [agetext setHidden:NO];
        [ageText setHidden:YES];
        

        
        [sexText resignFirstResponder];
        [nickNameText resignFirstResponder];
        [ageText resignFirstResponder];
        [changePhoto setHidden:YES];
        editStatus = NO;
        
        
        NSString *sextempforapi;
        APIConn *conn = [APIConn new];
        conn.apiDelegate = self;
        if ([sex.text isEqualToString:@"女"]) {
            sextempforapi = @"0";
        }else{
            sextempforapi = @"1";
        }
        [appdelegate initPauseView:@"更新中..請稍候.."];
        [conn User_Info:@{@"user_ID":accountID,@"gender":sextempforapi,@"name":nickName.text,@"age":agetext.text}];
    }
    

}
-(void)goToCropPage{
    CropImageViewController *crop = [CropImageViewController new];
    [self.navigationController pushViewController:crop animated:YES];
    NSLog(@"addroom move");
}

-(void)showPicker{
    [self.view endEditing:YES];
    [sexPicker show];
}
-(void)tapView:(UITapGestureRecognizer*)tap{//UITextField *nickNameText,*ageText,*locationText;
    [nickNameText resignFirstResponder];
    [ageText resignFirstResponder];
    NSLog(@"tapview");
    
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    NSLog(@"textShoulrreturn");
    return YES;
}
#pragma mark - getters & setters
-(void)initByDuke{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *name = [defaults objectForKey:@"accountUsername"];
    NSString *gender = [defaults objectForKey:@"accountGender"];
    NSString *age = [defaults objectForKey:@"accountAge"];
    NSString *photo = [defaults objectForKey:@"accountPhoto"];
    NSLog(@"%@ %@ %@ %@",name ,gender,age,photo);
    CGFloat y = 0;
    
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT-64)];
    scrollView.backgroundColor = LIGHT_BLUE;
    
    UILabel *headBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 90*RATIO)];
    headBg.backgroundColor = LIGHTG_COLOR;
    
    popupCleanerIcon = [[UIImageView alloc] initWithFrame:CGRectMake(20*RATIO, 10*RATIO, 70*RATIO, 70*RATIO)];
    popupCleanerIcon.contentMode = UIViewContentModeScaleAspectFill;
    [popupCleanerIcon.layer setCornerRadius:15*RATIO];
    popupCleanerIcon.clipsToBounds = YES;
    //icon
    [popupCleanerIcon sd_setImageWithURL:[NSURL URLWithString:photo] placeholderImage:[UIImage imageNamed:@"photo.png"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        popupCleanerIcon.alpha = 0.3;
        [UIView animateWithDuration:0.4f animations:^{
            popupCleanerIcon.alpha = 1.0f;
        }];
    }];
    
    changePhoto = [[UIButton alloc]initWithFrame:CGRectMake(100*RATIO, 20*RATIO, 90*RATIO, 30*RATIO)];
    changePhoto.backgroundColor = [UIColor clearColor];
    [changePhoto setTitle:@"更換頭像" forState:UIControlStateNormal];
    [changePhoto setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [changePhoto addTarget:self action:@selector(goToCropPage) forControlEvents:UIControlEventTouchUpInside];
    
    y += 90*RATIO;
    
    UILabel *sexBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    sexBg.backgroundColor = LIGHT_BLUE;
    
    UILabel *sexTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    sexTitle.backgroundColor = [UIColor clearColor];
    sexTitle.text = @"性別";
    sexTitle.textAlignment = NSTextAlignmentLeft;
    
    sex = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 70*RATIO, 20*RATIO)];
    sex.backgroundColor = [UIColor clearColor];
    if ([gender isEqualToString:@"0"]) {
        sex.text = @"女";
    }else{
        sex.text = @"男";
    }
    sex.textAlignment = NSTextAlignmentLeft;
    
    sexText = [[UIButton alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 70*RATIO, 20*RATIO)];
    if ([gender isEqualToString:@"0"]) {
        [sexText setTitle:@"女" forState:UIControlStateNormal];
    }else{
        [sexText setTitle:@"男" forState:UIControlStateNormal];
    }
    [sexText addTarget:self action:@selector(showPicker) forControlEvents:UIControlEventTouchUpInside];
    [sexText setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [sexText setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    y += 70*RATIO;
    
    UILabel *nickNameBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    nickNameBg.backgroundColor = LIGHTG_COLOR;
    
    UILabel *nickNameTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    nickNameTitle.backgroundColor = [UIColor clearColor];
    nickNameTitle.text = @"暱稱";
    nickNameTitle.textAlignment = NSTextAlignmentLeft;
    
    nickName = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 70*RATIO, 20*RATIO)];
    nickName.backgroundColor = [UIColor clearColor];
    nickName.text = name;
    nickName.textAlignment = NSTextAlignmentLeft;
    
    nickNameText  = [[UITextField alloc]initWithFrame:CGRectMake(80*RATIO, y+15*RATIO, 170*RATIO, 40*RATIO)];
    [nickNameText setDelegate:self];
    
    y += 70*RATIO;
    
    UILabel *ageBg = [[UILabel alloc]initWithFrame:CGRectMake(0, y, WIDTH, 70*RATIO)];
    ageBg.backgroundColor = LIGHT_BLUE;
    
    UILabel *ageTitle = [[UILabel alloc]initWithFrame:CGRectMake(20*RATIO, y+25*RATIO, 40*RATIO, 20*RATIO)];
    ageTitle.backgroundColor = [UIColor clearColor];
    ageTitle.text = @"年齡";
    ageTitle.textAlignment = NSTextAlignmentLeft;
    
    agetext = [[UILabel alloc]initWithFrame:CGRectMake(80*RATIO, y+25*RATIO, 70*RATIO, 20*RATIO)];
    agetext.backgroundColor = [UIColor clearColor];
    agetext.text = age;
    agetext.textAlignment = NSTextAlignmentLeft;
    
    ageText = [[UITextField alloc]initWithFrame:CGRectMake(80*RATIO, y+15*RATIO, 170*RATIO, 40*RATIO)];
    [ageText setDelegate:self];
    
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
        NSLog(@"star i = %d",i);
    }
    for (int i = 0; i <5-rating; i++) {
        UIImageView *starFull = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"star.png"]];
        starFull.frame = CGRectMake(xforStar , y+10*RATIO, 50*RATIO ,50*RATIO);
        [scrollView addSubview:starFull];
        xforStar = xforStar + 50*RATIO;
    }
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapView:)];
    
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
    [scrollView addSubview:sexText];
    [scrollView addSubview:nickNameText];
    [scrollView addSubview:ageText];
    [scrollView addSubview:starRatingView];
    [scrollView addSubview:changePhoto];
    
    [scrollView addGestureRecognizer:tap];
    
    
    [scrollView setContentSize:CGSizeMake(WIDTH, y)];
    [scrollView setShowsHorizontalScrollIndicator:NO];
    [scrollView setShowsVerticalScrollIndicator:NO];
    [scrollView setScrollEnabled:YES];
    [self.view addSubview:scrollView];
    
    
    
    
}

#pragma mark - ZHPick Delegate

-(void)toobarDonBtnHaveClick:(ZHPickView *)pickView resultString:(NSString *)resultString{
    if (pickView == sexPicker) {
        [self sexSelectFinished:resultString];
    }
}

-(void)pickerCancel{
    
}

-(void)sexSelectFinished:(NSString*)sexselect{
    [sexText setTitle:sexselect forState:UIControlStateNormal];
    sex.text = sexselect;
    
}
#pragma mark - TextField Delegate
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    CGRect frame = textField.frame;
    UIView *superView = [textField superview];
    int offset = frame.origin.y + frame.size.height + superView.frame.origin.y - (HEIGHT - 240.0);//键盘高度216
    NSTimeInterval animationDuration = 0.20f;
    [UIView beginAnimations:@"ResizeForKeyBoard" context:nil];
    [UIView setAnimationDuration:animationDuration];
    float width = WIDTH;
    float height = HEIGHT;
    if(offset > 0)
    {
        CGRect rect = CGRectMake(0.0f, -offset,width,height);
        self.view.frame = rect;
    }
    [UIView commitAnimations];
}

-(void)textFieldDidEndEditing:(UITextField *)textField{
    [self recoverView];
}

-(void) recoverView{
    NSTimeInterval animationDuration = 0.10f;
    [UIView beginAnimations:@"ResizeForKeyboard" context:nil];
    [UIView setAnimationDuration:animationDuration];
    float y=0.0f;
    CGRect rect = CGRectMake(0.0f, y, self.view.frame.size.width, self.view.frame.size.height);
    self.view.frame = rect;
    [UIView commitAnimations];
}

@end
