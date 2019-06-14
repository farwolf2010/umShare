//
//  UMShareModule.m
//  AFNetworking
//
//  Created by 郑江荣 on 2019/2/2.
//

#import "UMShareModule.h"
#import <WeexPluginLoader/WeexPluginLoader.h>
#import <UMCommon/UMCommon.h>
#import <UMShare/UMShare.h>
#import <UMShare/UMSocialManager.h>
#import <UShareUI/UShareUI.h>
#import <WeexSDK/WXEventModuleProtocol.h>
#import <WeexSDK/WXModuleProtocol.h>
#import <UMShare/UMSocialMessageObject.h>
#import "farwolf.h"


WX_PlUGIN_EXPORT_MODULE(umshare, UMShareModule)
@implementation UMShareModule

WX_EXPORT_METHOD(@selector(initUM:))
//WX_EXPORT_METHOD(@selector(setPlatform:))
WX_EXPORT_METHOD(@selector(initWechat:appSecret:))
WX_EXPORT_METHOD(@selector(initQQ:appSecret:))
WX_EXPORT_METHOD(@selector(initSina:appSecret:))
WX_EXPORT_METHOD(@selector(share:callback:))
WX_EXPORT_METHOD(@selector(openShareBox:callback:))
/** 初始化友盟方法 */
- (void)initUM:(NSMutableDictionary*)param
{
    /* 初始化友盟组件 */
    NSString *appkey=param[@"appkey"];
     NSString *channel=param[@"channel"];
    [UMConfigure initWithAppkey:appkey channel:channel];
}

-(void)initWechat:(NSMutableDictionary*)dic{
     NSString  *appkey=dic[@"appkey"];
     NSString  *appSecret=dic[@"appSecret"];
     NSString  *redirectUrl=dic[@"redirectUrl"];
     [[UMSocialManager defaultManager] setPlaform:UMSocialPlatformType_WechatSession appKey:appkey appSecret:appSecret redirectURL:redirectUrl];
}


-(void)initQQ:(NSMutableDictionary*)dic{
    NSString  *appkey=dic[@"appkey"];
    NSString  *appSecret=dic[@"appSecret"];
    NSString  *url=dic[@"redirectUrl"];
    [[UMSocialManager defaultManager] setPlaform:UMSocialPlatformType_QQ appKey:appkey appSecret:appSecret redirectURL:url];
}


-(void)initSina:(NSMutableDictionary*)dic{
    NSString  *appkey=dic[@"appkey"];
    NSString  *appSecret=dic[@"appSecret"];
    NSString  *url=dic[@"redirectUrl"];
    [[UMSocialManager defaultManager] setPlaform:UMSocialPlatformType_Sina appKey:appkey appSecret:appSecret redirectURL:url];
}


-(void)setPlatform:(NSMutableArray*)param{
    
     NSMutableArray *ary=[NSMutableArray new];
    for(NSString *key in param){
        if([@"wechat-chat" isEqualToString:key]){
            [ary addObject:@(UMSocialPlatformType_WechatSession)];
        }
        
        if([@"wechat-timeline" isEqualToString:key]){
            [ary addObject:@(UMSocialPlatformType_WechatTimeLine)];
        }
        if([@"qq" isEqualToString:key]){
            [ary addObject:@(UMSocialPlatformType_QQ)];
        }
        if([@"sina" isEqualToString:key]){
            [ary addObject:@(UMSocialPlatformType_Sina)];
        }
    }
   
    
    [UMSocialUIManager setPreDefinePlatforms:ary];
}

-(void)openShareBox:(NSDictionary *)info callback:(WXModuleCallback)callback{
    
    [self setPlatform:info[@"platforms"]];
    [UMSocialUIManager showShareMenuViewInWindowWithPlatformSelectionBlock:^(UMSocialPlatformType platformType, NSDictionary *userInfo) {
        // 根据获取的platformType确定所选平台进行下一步操作
       NSString *platform= [self getTypeReverse:platformType];
        [info setValue:platform forKey:@"platform"];
        [self share:info callback:callback];
    }];
}
-(UMSocialPlatformType)getType:(NSString*)platform{
    if([@"wechat-chat" isEqualToString:platform]){
        return UMSocialPlatformType_WechatSession;
    }else if([@"wechat-timeline" isEqualToString:platform]){
        return UMSocialPlatformType_WechatTimeLine;
    }else if([@"qq" isEqualToString:platform]){
        return UMSocialPlatformType_QQ;
    }else if([@"sina" isEqualToString:platform]){
        return UMSocialPlatformType_Sina;
    }
    return UMSocialPlatformType_WechatTimeLine;
}
-(NSString*)getTypeReverse:(UMSocialPlatformType)platform{
    if(UMSocialPlatformType_WechatSession == platform){
        return @"wechat-chat";
    }else if(UMSocialPlatformType_WechatTimeLine == platform){
        return @"wechat-timeline";
    }else if(UMSocialPlatformType_QQ == platform){
        return @"qq";
    }else if(UMSocialPlatformType_Sina == platform){
        return @"sina";
    }
    return @"wechat-chat";
}
/** 分享 */
- (void)share:(NSDictionary *)info callback:(WXModuleCallback)callback
{
//    BMShareModel *model = [BMShareModel yy_modelWithJSON:info];
    
    /**
     分享功能
     */
    NSString *shareTitle =info[@"title"];
    NSString *shareText =info[@"content"];
    NSString *shareUrl =info[@"url"];
    NSString *platform =info[@"platform"];
    NSString *shareType =info[@"shareType"];
    NSString *image =info[@"image"];
    NSString *path =info[@"path"];
    NSString *userName =info[@"userName"];
    
//    id shareImage = model.image;
    
    UMSocialPlatformType platformType = [self getType:platform];
    UMSocialMessageObject *messageObject = [UMSocialMessageObject messageObject];
    /** 分享类型 */
    //文本
    if ([platform isEqualToString: @"text"]) {
        messageObject.text = shareText;
    }
    //图片
    else if ([shareType isEqualToString: @"image"])
    {
        UMShareImageObject *shareObject = [[UMShareImageObject alloc] init];
        shareObject.shareImage = image;
        messageObject.shareObject = shareObject;
    }
    //图文
    else if ([shareType isEqualToString: @"textimage"])
    {
        UMShareImageObject *shareObject = [UMShareImageObject shareObjectWithTitle:shareTitle descr:shareText thumImage:nil];
        shareObject.shareImage = image;
        messageObject.text = shareText;
        messageObject.shareObject = shareObject;
    }
    //音乐
    else if ([shareType isEqualToString: @"music"])
    {
//        UMShareMusicObject *img=[UMShareMusicObject shareObjectWithTitle:shareTitle descr:shareText thumImage:nil];
        UMShareMusicObject *shareObject = [UMShareMusicObject shareObjectWithTitle:shareTitle descr:shareText thumImage:image];
        shareObject.musicUrl = shareUrl;
        messageObject.shareObject = shareObject;
    }
    //视频
    else if ([shareType isEqualToString: @"video"])
    {
        UMShareVideoObject *shareObject = [UMShareVideoObject shareObjectWithTitle:shareTitle descr:shareText thumImage:image];
        shareObject.videoUrl = shareUrl;
        messageObject.shareObject = shareObject;
    }
    //小程序
    else if ([shareType isEqualToString: @"miniProgram"])
    {
        UMShareMiniProgramObject *shareObject = [UMShareMiniProgramObject shareObjectWithTitle:shareTitle descr:shareText thumImage:image];
        shareObject.webpageUrl = shareUrl;
        shareObject.path = path;
        shareObject.userName = userName;
        messageObject.shareObject = shareObject;
    }
    //网页
    else
    {
        UMShareWebpageObject *shareObject = [UMShareWebpageObject shareObjectWithTitle:shareTitle descr:shareText thumImage:image];
        shareObject.webpageUrl = shareUrl;
        messageObject.text = shareText;
        messageObject.shareObject = shareObject;
    }

    
    //设置分享内容
    [[UMSocialManager defaultManager] shareToPlatform:platformType
                                        messageObject:messageObject
                                currentViewController: self.weexInstance.viewController.TopViewController
                                           completion:^(id result, NSError *error) {
                                               if (error) {
                                                   WXLogError(@"%@",error);
                                            
                                                   /* 失败回调 */
                                                   if (callback) {
                                                       callback(@{@"err":@(1)});
                                                   }
                                                   
                                               } else {
                                                   
                                                   /* 成功回调 */
                                                   if (callback) {
 
                                                       callback(@{@"err":@(0)});
                                                   }
                                               }
                                           }];
}



@end
