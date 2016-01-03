//
//  APIConn.h
//  iReading
//
//  Created by Anbon on 2014/7/25.
//  Copyright (c) 2014年 anbon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SBJson.h"

@class APIConn;

@protocol APIConnDelegate <NSObject>

@required
-(void)networkError:(NSString*)err;

@optional
-(void)verifyFinished:(NSDictionary*)dic;
-(void)getMemberFinished:(NSDictionary*)dic;
-(void)searchUserFinished:(NSDictionary*)dic;
-(void)registerMemberFinished:(NSDictionary*)dic;
-(void)creatChatFinished:(NSDictionary*)dic;
-(void)chatAmountFinished:(NSDictionary*)dic;
-(void)ratingFinished:(NSDictionary*)dic;
-(void)blocklistFinished:(NSDictionary*)dic;
-(void)blockaddFinished:(NSDictionary*)dic;
-(void)blockdeleteFinished:(NSDictionary*)dic;
-(void)seedFinished:(NSDictionary*)dic;
-(void)UserInfoFinished:(NSDictionary*)dic;
-(void)UpdateUserPhotoFinished:(NSDictionary*)dic;
-(void)Search_IDFinished:(NSDictionary*)dic;

-(void)applyChatFinished:(NSDictionary*)dic;
-(void)waitChatFinished:(NSDictionary*)dic;
-(void)Search_ChatFinished:(NSDictionary*)dic;
-(void)VerifyChatFinished:(NSDictionary*)dic;
@end

@interface APIConn : NSObject{
    
    NSMutableData *tempData;    //下載時暫存用的記憶體
    long expectedLength;        //檔案大小
    NSURLConnection *connect;
    
    int action_type;
}

@property (assign, nonatomic) id<APIConnDelegate>apiDelegate;

-(void)verify:(NSDictionary*)data;
-(void)getMember:(NSDictionary*)data;
-(void)searchUser:(NSDictionary*)data;
-(void)registerMember:(NSDictionary*)data;
-(void)creatChat:(NSDictionary*)data;
-(void)chatAmount:(NSDictionary*)data;
-(void)rating:(NSDictionary*)data;
-(void)blocklist:(NSDictionary*)data;
-(void)blockadd:(NSDictionary*)data;
-(void)blockdelete:(NSDictionary*)data;
-(void)seed:(NSDictionary*)data;
-(void)User_Info:(NSDictionary*)data;
-(void)UpdateUserPhoto:(NSDictionary*)data andImage:(NSString*)image;
-(void)Search_ID:(NSDictionary*)data;

-(void)waitChat:(NSDictionary*)data;
-(void)applyChat:(NSDictionary*)data;
-(void)VerifyChat:(NSDictionary*)data;
-(void)Search_Chat:(NSDictionary*)data;
@end
