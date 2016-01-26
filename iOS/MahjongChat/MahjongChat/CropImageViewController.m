//
//  CropImageViewController.m
//  MahjongChat
//
//  Created by Duke on 2015/12/27.
//  Copyright © 2015年 Duke. All rights reserved.
//

#import "CropImageViewController.h"
#import "AppDelegate.h"
@interface CropImageViewController ()

@end

@implementation CropImageViewController
{
    NSMutableArray *photoData;
    UIImageView *selectPhoto;
    AppDelegate *appdelegate;
}
#pragma mark - life Cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    photoData = [NSMutableArray new];
    appdelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [self initByDuke];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
#pragma mark - API Delegate
-(void)networkError:(NSString *)err{
    [appdelegate dismissPauseView];
}
-(void)UpdateUserPhotoFinished:(NSDictionary *)dic{
    [appdelegate dismissPauseView];
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"相片上傳成功" message:@"" delegate:self cancelButtonTitle:@"確定" otherButtonTitles:nil];
        [alert show];
        
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *userID = [defaults objectForKey:@"accountID"];
        APIConn *conn = [APIConn new];
        conn.apiDelegate = self;
        [conn Search_ID:@{@"user_ID":userID}];
        
        
    }else{
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"相片上傳失敗" message:@"請檢查您的網路連線或重新上傳" delegate:self cancelButtonTitle:@"確定" otherButtonTitles:nil];
        [alert show];
        [appdelegate dismissPauseView];
        [self.navigationController popViewControllerAnimated:YES];
    }
}

-(void)Search_IDFinished:(NSDictionary *)dic{
    if ([[dic objectForKey:@"status"]isEqualToString:@"1"]) {
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"name"] forKey:@"accountName"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"email"] forKey:@"accountEmail"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"username"] forKey:@"accountUsername"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"password"] forKey:@"accountPassword"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"gender"] forKey:@"accountGender"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"age"] forKey:@"accountAge"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"photo"] forKey:@"accountPhoto"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"level"] forKey:@"accountLevel"];
        [defaults setObject:[[dic objectForKey:@"message"]objectForKey:@"rate"] forKey:@"accountRate"];
        
        [defaults synchronize];
        [appdelegate dismissPauseView];
        NSLog(@"search ID success");
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        [appdelegate dismissPauseView];
        NSLog(@"search ID fail");
        [self.navigationController popViewControllerAnimated:YES];
    }
}
#pragma mark - event response
-(void)getPhoto{
    UIImagePickerController *picker=[[UIImagePickerController alloc] init];
    picker.sourceType=UIImagePickerControllerSourceTypeSavedPhotosAlbum;
    picker.delegate=self;
    [picker setAllowsEditing:NO];
    [picker setNavigationBarHidden:NO];
    [[UIApplication sharedApplication] setStatusBarHidden:NO];
    [picker didMoveToParentViewController:self];
    [self presentViewController:picker animated:YES completion:nil];
}

-(void)updatePhoto{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [defaults objectForKey:@"accountID"];
    NSString *imagepath = [photoData objectAtIndex:photoData.count-1];
    NSLog(@"userID= %@ imagePath = %@",userID,imagepath);
    APIConn *conn = [APIConn new];
    conn.apiDelegate = self;
    [conn UpdateUserPhoto:@{@"user_ID":userID} andImage:imagepath];
    [appdelegate initPauseView:@"上傳中..請稍候.."];
}

#pragma mark - getters & setters
-(void)initByDuke{
    selectPhoto = [[UIImageView alloc]initWithFrame:CGRectMake(WIDTH/2-150*RATIO, 50*RATIO, 300*RATIO, 300*RATIO)];
    selectPhoto.contentMode = UIViewContentModeScaleToFill;
    [selectPhoto.layer setBorderColor:[MAIN_COLOR CGColor]];
    [selectPhoto.layer setBorderWidth:0.8f];
    [selectPhoto.layer setCornerRadius:30*RATIO];
    [selectPhoto setClipsToBounds:YES];
    [self.view addSubview:selectPhoto];
    
    UIButton *takePhotoBtn = [[UIButton alloc] initWithFrame:CGRectMake(WIDTH/2-60*RATIO, 400*RATIO, 120*RATIO , 36*RATIO)];
    [takePhotoBtn setTitle:@"選擇照片" forState:UIControlStateNormal];
    [takePhotoBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [takePhotoBtn.layer setCornerRadius:12*RATIO];
    takePhotoBtn.backgroundColor = MAIN_COLOR;
    [takePhotoBtn addTarget:self action:@selector(getPhoto) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:takePhotoBtn];
    
    UIButton *updatePhotoBtn = [[UIButton alloc] initWithFrame:CGRectMake(WIDTH/2-60*RATIO, 450*RATIO, 120*RATIO, 36*RATIO)];
    [updatePhotoBtn setTitle:@"上傳照片" forState:UIControlStateNormal];
    [updatePhotoBtn.layer setCornerRadius:12*RATIO];
    updatePhotoBtn.backgroundColor = MAIN_COLOR;
    [updatePhotoBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [updatePhotoBtn addTarget:self action:@selector(updatePhoto) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:updatePhotoBtn];
}


#pragma mark - PhotoPicker Delegate
-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [self dismissViewControllerAnimated:YES
                             completion:nil];
}
- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    UIImage *image = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
    selectPhoto.image = image;
    NSString *docDir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString *jpegFilePath = [NSString stringWithFormat:@"%i.jpeg", (int)[[NSDate date] timeIntervalSince1970]];//[NSString stringWithFormat:@"%@/%i.jpeg",docDir, (int)[[NSDate date] timeIntervalSince1970]];
    NSData *savedData = [NSData dataWithData:UIImageJPEGRepresentation(image, 0.1f)];//1.0f = 100% quality
    NSString *filePath = [NSString stringWithFormat:@"%@/%@",docDir, jpegFilePath];
    [savedData writeToFile:filePath atomically:YES];
    NSLog(@"path: %@", filePath);
    [photoData addObject:filePath];
    for (int i=0; i<photoData.count; i++) {
        NSLog(@"filepaht(%d) = %@",i,[photoData objectAtIndex:i]);
    }
    
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
    NSLog(@"imageselected");
    NSString *docDir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString *jpegFilePath = [NSString stringWithFormat:@"%i.jpeg", (int)[[NSDate date] timeIntervalSince1970]];//[NSString stringWithFormat:@"%@/%i.jpeg",docDir, (int)[[NSDate date] timeIntervalSince1970]];
    NSData *savedData = [NSData dataWithData:UIImageJPEGRepresentation(image, 1.0f)];//1.0f = 100% quality
    NSString *filePath = [NSString stringWithFormat:@"%@/%@",docDir, jpegFilePath];
    [savedData writeToFile:filePath atomically:YES];
    NSLog(@"path: %@", filePath);
    [photoData addObject:filePath];
    
    NSLog(@"filepath photo = %@",photoData);
   
}
@end
