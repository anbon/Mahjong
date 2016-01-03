//
//  APIConn.m
//  iReading
//
//  Created by Anbon on 2014/7/25.
//  Copyright (c) 2014年 anbon. All rights reserved.
//

#import "APIConn.h"
#import "AppDelegate.h"

#define VERIFY 1
#define GET_MEMBER 2
#define SEARCH_USER 3
#define REGISTER 4
#define CREAT_CHAT 5
#define CHAT_AMOUNT 6
#define WAIT_CHAT 7
#define RATING 9
#define BLOCK_LIST 10
#define BLOCK_ADD 11
#define BLOCK_DELETE 12
#define SEED 13
#define USER_INFO 14
#define UPDATE_PHOTO 15
#define SEARCH_ID 16
#define APPLY_CHAT 17
#define SEARCH_CHAT 18
#define VERIFY_CHAT 19

#define DOMAIN_MAIN @"http://www.anbon.tw/mj/index.php"
#define TOKEN [[[UIDevice currentDevice] identifierForVendor] UUIDString]

@implementation APIConn{
    
}

-(id)init{
    self=[super init];
    if (self) {
        
    }
    return self;
}

-(void)getProduct:(NSDictionary*)data{
    //action_type=GET_PRODUCT;
    NSString *post = [NSString stringWithFormat:@"user=ios&time=%@&checkCode=%@&userToken=%@&email=%@&name=%@&zipcode=%@&address=%@&phone=%@",data[@"test"],data[@"test123"],data[@"userToken"],data[@"email"],data[@"name"],data[@"zipcode"],data[@"address"],data[@"phone"]];
    NSString *url=[NSString stringWithFormat:@"%@api/product", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}

-(void)verify:(NSDictionary*)data{
    action_type = VERIFY;
    NSString *post = [NSString stringWithFormat:@"username=%@&password=%@",data[@"username"],data[@"password"]];
    NSString *url=[NSString stringWithFormat:@"%@/verify", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)getMember:(NSDictionary*)data{
    action_type = GET_MEMBER;
    NSString *post = [NSString stringWithFormat:@""];
    NSString *url=[NSString stringWithFormat:@"%@/getmember", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)searchUser:(NSDictionary*)data{
    action_type = SEARCH_USER;
    NSString *post = [NSString stringWithFormat:@"username=%@",data[@"username"]];
    NSString *url=[NSString stringWithFormat:@"%@/Search_User", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)registerMember:(NSDictionary*)data{
    action_type = REGISTER;
    NSString *post = [NSString stringWithFormat:@"registerName=%@&registerEmail=%@&registerUser=%@&registerPass=%@",data[@"registerName"],data[@"registerEmail"],data[@"registerUser"],data[@"registerPass"]];
    NSString *url=[NSString stringWithFormat:@"%@/register", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)creatChat:(NSDictionary*)data{
    action_type = CREAT_CHAT;
    NSString *post = [NSString stringWithFormat:@"userid=%@&name=%@&base=%@&unit=%@&circle=%@&time=%@&location=%@&people=%@&type=%@&cigarette=%@&rule=%@",data[@"user_Id"],data[@"name"],data[@"base"],data[@"unit"],data[@"circle"],data[@"time"],data[@"location"],data[@"people"],data[@"type"],data[@"cigarette"],data[@"rule"]];
    NSString *url=[NSString stringWithFormat:@"%@/createchat", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
//====
-(void)applyChat:(NSDictionary*)data{
    action_type = APPLY_CHAT;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&room_ID=%@",data[@"user_Id"],data[@"room_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/applyChat", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}

-(void)waitChat:(NSDictionary*)data{
    action_type = WAIT_CHAT;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&room_ID=%@",data[@"user_Id"],data[@"room_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/waitChat", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
//====
-(void)chatAmount:(NSDictionary*)data{
    action_type = CHAT_AMOUNT;
    NSString *post = [NSString stringWithFormat:@"num=%@",data[@"num"]];
    NSString *url=[NSString stringWithFormat:@"%@/chatamount", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}


-(void)rating:(NSDictionary*)data{
    action_type = RATING;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&rating=%@",data[@"user_ID"],data[@"rating"]];
    NSString *url=[NSString stringWithFormat:@"%@/rating", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)blocklist:(NSDictionary*)data{
    action_type = BLOCK_LIST;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@",data[@"user_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/blocklist", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)blockadd:(NSDictionary*)data{
    action_type = BLOCK_ADD;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&block_ID=%@",data[@"user_ID"],data[@"block_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/blockadd", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)blockdelete:(NSDictionary*)data{
    action_type = BLOCK_DELETE;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&block_ID=%@",data[@"user_ID"],data[@"block_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/blockdelete", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)seed:(NSDictionary*)data{
    action_type = SEED;
    NSLog(@"locationX= %@ locationY=%@",data[@"locationX"],data[@"locationY"]);
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&location_x=%@&location_y=%@",data[@"user_ID"],data[@"locationX"],data[@"locationY"]];
    NSString *url=[NSString stringWithFormat:@"%@/seed", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}
-(void)User_Info:(NSDictionary*)data{
    action_type = USER_INFO;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&gender=%@&name=%@&age=%@",data[@"user_ID"],data[@"gender"],data[@"name"],data[@"age"]];
    NSString *url=[NSString stringWithFormat:@"%@/User_Info", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}

-(void)UpdateUserPhoto:(NSDictionary*)data andImage:(NSString*)image{
    action_type = UPDATE_PHOTO;
    NSString *imagestrTemp = [NSString stringWithFormat:@"%@",image];
    NSLog(@"%@",imagestrTemp);
    UIImage *imageTemp = [[UIImage alloc]initWithContentsOfFile:imagestrTemp];
    NSData *imageData = UIImageJPEGRepresentation(imageTemp, 90);

    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:@"http://www.anbon.tw/mj/index.php/User_Photo"]];
    [request setHTTPMethod:@"POST"];
    
    NSString *boundary = @"---------------------------4f86f03be60f005119000000";
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary];
    [request addValue:contentType forHTTPHeaderField: @"Content-Type"];
    
    NSMutableData *body = [NSMutableData data];
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[@"Content-Disposition: form-data; name=\"file\"; filename=\"pic.jpg\"\r\n"dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[@"Content-Type: image/png\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[NSData dataWithData:imageData]];
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[@"Content-Disposition: form-data; name=\"user_ID\"\r\n\r\n"dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[data[@"user_ID"] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    
    [body appendData:[@"\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
    // setting the body of the post to the reqeust
    [request setHTTPBody:body];
    tempData=[NSMutableData alloc];
    connect = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    UIApplication *app = [UIApplication sharedApplication];
    app.networkActivityIndicatorVisible = YES;
    
    

}

-(void)Search_ID:(NSDictionary*)data{
    action_type = SEARCH_ID;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@",data[@"user_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/Search_ID", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}

-(void)Search_Chat:(NSDictionary*)data{
    action_type = SEARCH_ID;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@&room_ID=%@",data[@"user_ID"],data[@"room_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/SearchChat", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}

-(void)VerifyChat:(NSDictionary*)data{
    action_type = SEARCH_ID;
    NSString *post = [NSString stringWithFormat:@"user_ID=%@",data[@"user_ID"]];
    NSString *url=[NSString stringWithFormat:@"%@/VerifyChat", DOMAIN_MAIN];
    [self openConnect:post andURL:url andMethod:@"POST"];
}

#pragma mark - Image Connection URL

-(void)openPhotoConnect:(NSString*)urlString andImage:(UIImage*)image{
    NSData *imageData = UIImageJPEGRepresentation(image, 90);

    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:urlString]];
    [request setHTTPMethod:@"POST"];
    
    NSString *boundary = @"---------------------------4f86f03be60f005119000000";
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary];
    [request addValue:contentType forHTTPHeaderField: @"Content-Type"];
    
    NSMutableData *body = [NSMutableData data];
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[@"Content-Disposition: form-data; name=\"pic\"; filename=\"pic.jpg\"\r\n"dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[@"Content-Type: image/png\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[NSData dataWithData:imageData]];
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--3\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[@"\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
    // setting the body of the post to the reqeust
    [request setHTTPBody:body];

    tempData=[NSMutableData alloc];
    connect = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    UIApplication *app = [UIApplication sharedApplication];
    app.networkActivityIndicatorVisible = YES;
}

#pragma mark - connection URL
-(void)openConnect:(NSString*)post andURL:(NSString*)url andMethod:(NSString*)method{
    NSData *postData = [post dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
    NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[postData length]];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    
    [request setURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:method];
    [request setValue:@"application/x-www-form-urlencoded charset=utf-8" forHTTPHeaderField:@"Content-Type"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"Mobile Safari 1.1.3 (iPhone; U; CPU like Mac OS X; en)" forHTTPHeaderField:@"User-Agent"];
    [request setHTTPBody:postData]; //加上 post 的資料
    tempData=[NSMutableData alloc];
    connect = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    UIApplication *app = [UIApplication sharedApplication];
    app.networkActivityIndicatorVisible = YES;
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error{
    UIApplication *app = [UIApplication sharedApplication];
    app.networkActivityIndicatorVisible = NO;
    [self.apiDelegate networkError:[error description]];
}

- (void)connection: (NSURLConnection *)connection didReceiveResponse: (NSURLResponse *)aResponse{
    NSInteger status = (NSInteger)[(NSHTTPURLResponse *)aResponse statusCode];
    if (status!=200) {
        UIApplication *app = [UIApplication sharedApplication];
        app.networkActivityIndicatorVisible = NO;
        [self.apiDelegate networkError:[NSString stringWithFormat:@"Response Code: %ld", (long)status]];
    }
}
-(void) connection:(NSURLConnection *)connection didReceiveData: (NSData *) incomingData{
    [tempData appendData:incomingData];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection{
    UIApplication *app = [UIApplication sharedApplication];
    app.networkActivityIndicatorVisible = NO;
    NSString *loadData = [[NSMutableString alloc] initWithData:tempData encoding:NSUTF8StringEncoding];
//    NSLog(@"Ori result: %@", loadData);

    switch (action_type) {
        case VERIFY:
            [self.apiDelegate verifyFinished:[loadData JSONValueFromString]];
            break;
        case GET_MEMBER:
            [self.apiDelegate getMemberFinished:[loadData JSONValueFromString]];
            break;
        case SEARCH_USER:
            [self.apiDelegate searchUserFinished:[loadData JSONValueFromString]];
            break;
        case REGISTER:
            [self.apiDelegate registerMemberFinished:[loadData JSONValueFromString]];
            break;
        case CREAT_CHAT:
            [self.apiDelegate creatChatFinished:[loadData JSONValueFromString]];
            break;
        case CHAT_AMOUNT:
            [self.apiDelegate chatAmountFinished:[loadData JSONValueFromString]];
            break;
        case RATING:
            [self.apiDelegate ratingFinished:[loadData JSONValueFromString]];
            break;
        case BLOCK_LIST:
            [self.apiDelegate blocklistFinished:[loadData JSONValueFromString]];
            break;
        case BLOCK_ADD:
            [self.apiDelegate blockaddFinished:[loadData JSONValueFromString]];
            break;
        case BLOCK_DELETE:
            [self.apiDelegate blockdeleteFinished:[loadData JSONValueFromString]];
            break;
        case SEED:
            [self.apiDelegate seedFinished:[loadData JSONValueFromString]];
            break;
        case USER_INFO:
            [self.apiDelegate UserInfoFinished:[loadData JSONValueFromString]];
            break;
        case UPDATE_PHOTO:
            [self.apiDelegate UpdateUserPhotoFinished:[loadData JSONValueFromString]];
            break;
        case SEARCH_ID:
            [self.apiDelegate Search_IDFinished:[loadData JSONValueFromString]];
            break;
        case APPLY_CHAT:
            [self.apiDelegate applyChatFinished:[loadData JSONValueFromString]];
            break;
        case WAIT_CHAT:
            [self.apiDelegate waitChatFinished:[loadData JSONValueFromString]];
            break;
        case SEARCH_CHAT:
            [self.apiDelegate Search_ChatFinished:[loadData JSONValueFromString]];
            break;
        case VERIFY_CHAT:
            [self.apiDelegate VerifyChatFinished:[loadData JSONValueFromString]];
            break;
    }
}

@end
