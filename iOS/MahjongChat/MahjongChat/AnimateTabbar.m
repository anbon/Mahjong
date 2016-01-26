//  AppDelegate.h
//  AnimatTabbarSample
//
//  Created by chenyanming on 14-4-9.
//  Copyright (c) 2014年 chenyanming. All rights reserved.
//

#import "AnimateTabbar.h"
#import "AppDelegate.h"
//#import "M13BadgeView.h"

@implementation AnimateTabbarView
{
    int g_selectedTag;
}
@synthesize  firstBtn,secondBtn,thirdBtn,fourthBtn,delegate,backBtn,shadeBtn;

enum barsize{
    other_offtop=0
};
float tab_width, tab_hight, tabitem_width, tabitem_hight, img_x, img_y, img_width, img_hight;
- (id)initWithFrame:(CGRect)frame
{
    g_selectedTag=1;
    tab_width = WIDTH;
    tab_hight = WIDTH * (46.0f/320.0f);
    tabitem_width = WIDTH / 3.0f;
    tabitem_hight = WIDTH * (46.0f/320.0f);
    img_width = 35*RATIO; //414 75 138
    img_hight = 35*RATIO;//tab_hight - 16;76
    img_x = tabitem_width/2 - img_width/2;
    img_y = tabitem_hight/2 - img_hight/2;
    CGRect frame1=CGRectMake(frame.origin.x, frame.size.height-tab_hight, tab_width, tab_hight);
    
    tabitem_width = WIDTH / 3;
    self = [super initWithFrame:frame1];
    if (self) {
        
        
        [self setBackgroundColor:[UIColor whiteColor]];
        backBtn=[UIButton buttonWithType:UIButtonTypeCustom];
        [backBtn setFrame:CGRectMake(0, 0, tab_width, tab_hight)];
        [backBtn setBackgroundImage:[UIImage imageNamed:@"tabBar_back"] forState:UIControlStateNormal];
        [backBtn setBackgroundImage:[UIImage imageNamed:@"tabBar_back"] forState:UIControlStateSelected];
     
        
        
        shadeBtn=[UIButton buttonWithType:UIButtonTypeCustom];
        [shadeBtn setFrame:CGRectMake(0, other_offtop, tabitem_width, tabitem_hight)];
        [shadeBtn setBackgroundImage:[UIImage imageNamed:@"toolBar_shade"] forState:UIControlStateNormal];
        [shadeBtn setBackgroundImage:[UIImage imageNamed:@"toolBar_shade"] forState:UIControlStateSelected];
        
        

        
        UIImageView *btnImgView;
        
        //first
        btnImgView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"tablocation.png"] highlightedImage:[UIImage imageNamed:@"selected_tablocation.png"]];
        btnImgView.frame = CGRectMake(img_x, img_y, img_width, img_hight);
        firstBtn=[UIButton buttonWithType:UIButtonTypeCustom];
        [firstBtn setFrame:CGRectMake(0, other_offtop, tabitem_width, WIDTH * (46.0f/320.0f))];
        [firstBtn setTag:1];
        [firstBtn addTarget:self action:@selector(buttonClickAction:) forControlEvents:UIControlEventTouchUpInside];
        //[firstBtn.layer setBorderColor:[[UIColor whiteColor] CGColor]];
        //[firstBtn.layer setBorderWidth:1.0f];
        [firstBtn setBackgroundColor:MAIN_COLOR];
        [firstBtn addSubview:btnImgView];
        
        //second
        btnImgView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"tabsearch.png"] highlightedImage:[UIImage imageNamed:@"selected_tabsearch.png"]];
        btnImgView.frame = CGRectMake(img_x, img_y, img_width, img_hight);
        secondBtn=[UIButton buttonWithType:UIButtonTypeCustom];
        [secondBtn setFrame:CGRectMake(tabitem_width, other_offtop, tabitem_width, WIDTH * (46.0f/320.0f))];
        [secondBtn setTag:2];
        [secondBtn addTarget:self action:@selector(buttonClickAction:) forControlEvents:UIControlEventTouchUpInside];
        //[secondBtn.layer setBorderColor:[[UIColor whiteColor] CGColor]];
        //[secondBtn.layer setBorderWidth:1.0f];
        [secondBtn setBackgroundColor:MAIN_COLOR];
        [secondBtn addSubview:btnImgView];
        
        //third
        btnImgView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"tabprofile.png"] highlightedImage:[UIImage imageNamed:@"selected_tabprofile.png"]];
        btnImgView.frame = CGRectMake(img_x, img_y, img_width, img_hight);
        thirdBtn=[UIButton buttonWithType:UIButtonTypeCustom];
        [thirdBtn setFrame:CGRectMake(tabitem_width*2, other_offtop, tabitem_width, WIDTH * (46.0f/320.0f))];
        [thirdBtn setTag:3];
        [thirdBtn addTarget:self action:@selector(buttonClickAction:) forControlEvents:UIControlEventTouchUpInside];
        //[thirdBtn.layer setBorderColor:[[UIColor whiteColor] CGColor]];
        //[thirdBtn.layer setBorderWidth:1.0f];
        [thirdBtn setBackgroundColor:MAIN_COLOR];
        [thirdBtn addSubview:btnImgView];
        
        _eventBadgeLabel = [[UILabel alloc] initWithFrame:CGRectMake(img_x+img_width-12, img_y-2, 20, 20)];
        [_eventBadgeLabel setBackgroundColor:[UIColor colorWithRed:1.0f green:0.25f blue:0.25f alpha:0.9f]];
        [_eventBadgeLabel setText:@"!"];
        [_eventBadgeLabel.layer setCornerRadius:10.0f];
        [_eventBadgeLabel setClipsToBounds:YES];
        [_eventBadgeLabel setTextAlignment:NSTextAlignmentCenter];
        [_eventBadgeLabel setTextColor:[UIColor whiteColor]];
        [thirdBtn addSubview:_eventBadgeLabel];
        [_eventBadgeLabel setHidden:YES];
        
        //fourth
        btnImgView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"tabBar_3"] highlightedImage:[UIImage imageNamed:@"tabBar_3_on"]];
        btnImgView.frame = CGRectMake(img_x, img_y, img_width, img_hight);
        fourthBtn=[UIButton buttonWithType:UIButtonTypeCustom];
        [fourthBtn setFrame:CGRectMake(tabitem_width*3, other_offtop, tabitem_width, tabitem_hight)];
        [fourthBtn setTag:4];
        [fourthBtn addTarget:self action:@selector(buttonClickAction:) forControlEvents:UIControlEventTouchUpInside];
        [fourthBtn addSubview:btnImgView];
        
        _memberBadgeLabel = [[UILabel alloc] initWithFrame:CGRectMake(img_x+img_width-12, img_y-2, 20, 20)];
        [_memberBadgeLabel setBackgroundColor:[UIColor colorWithRed:1.0f green:0.25f blue:0.25f alpha:0.9f]];
        [_memberBadgeLabel setText:@"!"];
        [_memberBadgeLabel.layer setCornerRadius:10.0f];
        [_memberBadgeLabel setClipsToBounds:YES];
        [_memberBadgeLabel setTextAlignment:NSTextAlignmentCenter];
        [_memberBadgeLabel setTextColor:[UIColor whiteColor]];
        [fourthBtn addSubview:_memberBadgeLabel];
        [_memberBadgeLabel setHidden:YES];
        [backBtn addSubview:shadeBtn];
        
        [backBtn addSubview:firstBtn];
        [backBtn addSubview:secondBtn];
        [backBtn addSubview:thirdBtn];
        
        
        
        [self addSubview:backBtn];
        
        
    }
    
    ((UIImageView *)firstBtn.subviews[0]).highlighted=YES;
    ((UIImageView *)firstBtn).backgroundColor = [UIColor whiteColor];
    return self;
    
    
}
-(void)eventBadge:(int)badge{
    if (badge<=0) {
        [_eventBadgeLabel setHidden:YES];
    }else{
        [_eventBadgeLabel setHidden:NO];
    }
    [[NSUserDefaults standardUserDefaults] setValue:[NSString stringWithFormat:@"%i", badge] forKey:@"new_event"];
    [[NSUserDefaults standardUserDefaults] synchronize];
}
-(void)memberBadge:(int)badge{
    if (badge<=0) {
        [_memberBadgeLabel setHidden:YES];
    }else{
        [_memberBadgeLabel setHidden:NO];
    }
    [[NSUserDefaults standardUserDefaults] setValue:[NSString stringWithFormat:@"%i", badge] forKey:@"new_member"];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

-(void)callButtonAction:(UIButton *)sender{
    int value=sender.tag;
    if (value==1) {
        [self.delegate FirstBtnClick];
    }
    if (value==2) {
        [self.delegate SecondBtnClick];
      }
    if (value==3) {
        [self.delegate ThirdBtnClick];
    }
//    if (value==4) {
//        [self.delegate FourthBtnClick];
//    }
    
}


-(void)buttonClickAction:(id)sender{
    UIButton *btn=(UIButton *)sender;
   // UIImageView *view=btn1.subviews[0];
    if(g_selectedTag==btn.tag)
        return;
    else
        g_selectedTag=btn.tag;
    
    
    if (firstBtn.tag!=btn.tag) {
        ((UIImageView *)firstBtn.subviews[0]).highlighted=NO;
        ((UIImageView *)firstBtn).backgroundColor = MAIN_COLOR;
    }
    
    if (secondBtn.tag!=btn.tag) {
        ((UIImageView *)secondBtn.subviews[0]).highlighted=NO;
        ((UIImageView *)secondBtn).backgroundColor = MAIN_COLOR;
    }
    
    if (thirdBtn.tag!=btn.tag) {
       
        ((UIImageView *)thirdBtn.subviews[0]).highlighted=NO;
        ((UIImageView *)thirdBtn).backgroundColor = MAIN_COLOR;
    }
    
//    if (fourthBtn.tag!=btn.tag) {
//        
//        ((UIImageView *)fourthBtn.subviews[0]).highlighted=NO;
//        ((UIImageView *)firstBtn.subviews[0]).backgroundColor = MAIN_COLOR;
//    }
    
    
//    [self moveShadeBtn:btn];
//    [self imgAnimate:btn];
    
    ((UIImageView *)btn.subviews[0]).highlighted=YES;
    ((UIImageView *)btn).backgroundColor = [UIColor whiteColor];
    [self callButtonAction:btn];
    
    return;
    
    
    

}

- (void)selectIndex:(int)index{
    if (index==0) {
        [self buttonClickAction:firstBtn];
    }else if (index==1) {
        [self buttonClickAction:secondBtn];
    }else if (index==2) {
        [self buttonClickAction:thirdBtn];
    }
}

- (void)moveShadeBtn:(UIButton*)btn{
    
    [UIView animateWithDuration:0.3 animations:
     ^(void){
         
         CGRect frame = shadeBtn.frame;
         frame.origin.x = btn.frame.origin.x;
        shadeBtn.frame = frame;
         
         
     } completion:^(BOOL finished){//do other thing
     }];
    
    
}

- (void)imgAnimate:(UIButton*)btn{
    
    UIView *view=btn.subviews[0];
    
    [UIView animateWithDuration:0.1 animations:
     ^(void){
         
          view.transform = CGAffineTransformScale(CGAffineTransformIdentity,0.5, 0.5);
         
         
     } completion:^(BOOL finished){//do other thing
         [UIView animateWithDuration:0.2 animations:
          ^(void){
              
              view.transform = CGAffineTransformScale(CGAffineTransformIdentity,1.2, 1.2);
              
          } completion:^(BOOL finished){//do other thing
              [UIView animateWithDuration:0.1 animations:
               ^(void){
                   
                   view.transform = CGAffineTransformScale(CGAffineTransformIdentity,1,1);
                   
                   
               } completion:^(BOOL finished){//do other thing
               }];
          }];
     }];
    
    
}



@end
