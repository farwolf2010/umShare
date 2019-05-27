//
//  UMShareManager.m
//  AFNetworking
//
//  Created by 郑江荣 on 2019/2/2.
//

#import "UMShareManager.h"
#import "farwolf.h"
#import "farwolf_weex.h"
#import <UMShare/UMSocialManager.h>
WX_PLUGIN_Entry(UMShareManager)
@implementation UMShareManager
+(instancetype)sharedManager {
    static dispatch_once_t onceToken;
    static UMShareManager *instance;
    dispatch_once(&onceToken, ^{
        instance = [[UMShareManager alloc] init];
    });
    return instance;
}


-(void)initEntry:(NSMutableDictionary*)lanchOption
{
    [[UMShareManager sharedManager] initHanler];
}


-(void)initHanler
{
    [self regist:APP_openURL method:@selector(handleOpenUrl:)];
    
}


-(void)handleOpenUrl:(NSNotification*)notify
{
    NSURL *url=notify.userInfo[@"url"];
   [[UMSocialManager defaultManager] handleOpenURL:url];
}
@end
