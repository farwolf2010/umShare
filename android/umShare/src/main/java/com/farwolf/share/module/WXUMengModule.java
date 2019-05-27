package com.farwolf.share.module;

import com.farwolf.weex.annotation.WeexModule;
import com.farwolf.weex.base.WXModuleBase;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhengjiangrong on 2017/8/9.
 */

@WeexModule(name="umshare")
public class WXUMengModule extends WXModuleBase {


    @JSMethod
    public void initUM(HashMap param) {
        String appkey=param.get("appkey")+"";
        String channel=param.get("channel")+"";
//        UMConfigure.init(getContext(), UMConfigure.DEVICE_TYPE_PHONE,appkey);
        UMConfigure.init(getContext(),   appkey,   channel, UMConfigure.DEVICE_TYPE_PHONE, null);
    }
    @JSMethod
    public void initWechat(HashMap param){
            String appkey=param.get("appkey")+"";
            String appSecret=param.get("appSecret")+"";
            String redirectUrl=param.get("redirectUrl")+"";
        PlatformConfig.setWeixin(appkey, appSecret);
    }
    @JSMethod
    public void initQQ(HashMap param){
        String appkey=param.get("appkey")+"";
        String appSecret=param.get("appSecret")+"";
        String redirectUrl=param.get("redirectUrl")+"";
        PlatformConfig.setQQZone(appkey, appSecret);
    }
    @JSMethod
    public void initSina(HashMap param){
        String appkey=param.get("appkey")+"";
        String appSecret=param.get("appSecret")+"";
        String redirectUrl=param.get("redirectUrl")+"";
        PlatformConfig.setSinaWeibo(appkey, appSecret,redirectUrl);
    }

    @JSMethod
    public void openShareBox(final HashMap info, final JSCallback callback){
        ShareBoardConfig config = new ShareBoardConfig();//新建ShareBoardConfig
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);//设置位置
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
        config.setCancelButtonVisibility(true);
        config.setTitleVisibility(false);
        config.setCancelButtonVisibility(false);
        config.setIndicatorVisibility(false);
        ArrayList l= (ArrayList)info.get("platforms");
        new ShareAction(getActivity())
                .setDisplayList(getPlatformList(l))
                .setShareboardclickCallback(new ShareBoardlistener() {

                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                       share(info,callback);
                    }
                })
                .open(config);
    }

    public SHARE_MEDIA[] getPlatformList(ArrayList l){
//        ArrayList<SHARE_MEDIA> lx=new ArrayList<SHARE_MEDIA>();
        SHARE_MEDIA lx[]=new SHARE_MEDIA[l.size()];
        for(Object o:l){
            SHARE_MEDIA me= getPlatform(o+"");
            lx[l.indexOf(o)]=me;
        }
        return lx;
    }



    @JSMethod
    public void share(HashMap info, JSCallback callback){
        String  shareTitle =info.get("title")+"";
        String  shareText =info.get("content")+"";
        String  shareUrl =info.get("url")+"";
        String  platform =info.get("platform")+"";
        String  shareType =info.get("shareType")+"";
        String  image =info.get("image")+"";
        String  path =info.get("path")+"";
        String  userName =info.get("userName")+"";
//        BaseMediaObject media=null;
        ShareAction shareAction=null;
        if("image".equals(shareType)){
            UMImage img=new UMImage(getContext(),image);
            img.setTitle(shareTitle);//标题
            img.setThumb(new UMImage(getActivity(), image));  //缩略图
            img.setDescription(shareText);//描述
            shareAction= new ShareAction(getActivity())
                    .withMedia(img);
        }else if("textimage".equals(shareType)){

        }else if("music".equals(shareType)){
            UMusic music=new UMusic(shareUrl);
            music.setTitle(shareTitle);//标题
            music.setThumb(new UMImage(getActivity(), image));  //缩略图
            music.setDescription(shareText);//描述
            shareAction= new ShareAction(getActivity())
                    .withMedia(music);
        }else if("video".equals(shareType)){
            UMVideo video=new UMVideo(shareUrl);
            video.setTitle(shareTitle);//标题
            video.setThumb(new UMImage(getActivity(), image));  //缩略图
            video.setDescription(shareText);//描述
            shareAction= new ShareAction(getActivity())
                    .withMedia(video);

        }else if("miniProgram".equals(shareType)){
            UMMin program=new UMMin(shareUrl);
            program.setPath(path);
            program.setTitle(shareTitle);//标题
            program.setThumb(new UMImage(getActivity(), image));  //缩略图
            program.setDescription(shareText);//描述
            program.setUserName(userName);
            shareAction= new ShareAction(getActivity())
                    .withMedia(program);
        }else {
            UMWeb  media = new UMWeb(shareUrl);
            media.setTitle(shareTitle);//标题
            media.setThumb(new UMImage(getActivity(), image));  //缩略图
            media.setDescription(shareText);//描述
            shareAction= new ShareAction(getActivity())
                    .withMedia(media);
        }

        shareAction.setPlatform(getPlatform(platform))
                .setCallback(getUMShareListener(callback))
                .share();


    }
//    public static void initUM(Context c) {
//        JSONObject j = Weex.config(c);
//        JSONObject st = j.optJSONObject("static");
//        JSONObject um = st.optJSONObject("umeng");
//        JSONObject android = um.optJSONObject("android");
//        String appkey = android.optString("appkey");
//        String channel = android.optString("channel");
//        UMConfigure.init(c, UMConfigure.DEVICE_TYPE_PHONE,appkey);
//        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(c, appkey, channel);
//        MobclickAgent.startWithConfigure(config);
//        MobclickAgent.setScenarioType(c, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.openActivityDurationTrack(false);
////        Config.DEBUG = true;
//        initPlatforms(j.optJSONObject("platform"));
//        try {
//            WXSDKEngine.registerModule("umeng", WXUMengModule.class);
//        } catch (Exception e) {
//
//        }
//
//    }

//    public static void initPlatforms(JSONObject pl) {
//        if (pl == null)
//            return;
//        JSONObject wx = pl.optJSONObject("wx");
//        JSONObject qq = pl.optJSONObject("qq");
//        JSONObject weibo = pl.optJSONObject("weibo");
//
//        if (wx != null) {
//            PlatformConfig.setWeixin(wx.optString("appkey"), wx.optString("appsecret"));
//        }
//        if (qq != null) {
//            PlatformConfig.setQQZone(qq.optString("appkey"), qq.optString("appsecret"));
//        }
//        if (weibo != null) {
//            PlatformConfig.setSinaWeibo(weibo.optString("appkey"), weibo.optString("appsecret"), weibo.optString("url"));
//        }
//
//    }

//    @JSMethod
//    public void initUM(String appkey, String channel) {
//        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(getActivity(), appkey, channel);
//        MobclickAgent.startWithConfigure(config);
//
//
//    }


//    @JSMethod
//    public void openShareBox(final HashMap param, final JSCallback callback) {
//
//        ShareBoardConfig config = new ShareBoardConfig();//新建ShareBoardConfig
//        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);//设置位置
//        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
//        config.setCancelButtonVisibility(true);
//        config.setTitleVisibility(false);
//        config.setCancelButtonVisibility(false);
//        config.setIndicatorVisibility(false);
//        //,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA,SHARE_MEDIA.TENCENT
//        new ShareAction(getActivity())
//                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .setShareboardclickCallback(new ShareBoardlistener() {
//
//                    @Override
//                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//                        if (param == null) {
//                            HashMap m = new HashMap();
//                            m.put("err", 1);
//                            m.put("res", "参数错误");
//                            callback.invoke(m);
//                            return;
//                        }
//                        String name = platformToName(share_media);
//                        String imgUrl = (String) param.get("imgUrl");
//                        String title = (String) param.get("title");
//                        String des = (String) param.get("des");
//                        String toUrl = (String) param.get("toUrl"); //必须http开头
//                        shareWebPage(name, imgUrl, title, des, toUrl, callback);
//                    }
//                })
//                .open(config);
//    }

//    /**
//     * 自定义事件
//     *
//     * @param eventId
//     * @param param
//     */
//    @JSMethod
//    public void onEvent(String eventId, HashMap param) {
//        if (param == null) {
//            MobclickAgent.onEvent(getActivity(), eventId);
//        } else {
//            MobclickAgent.onEvent(getActivity(), eventId, param);
//        }
//    }


//    @JSMethod
//    public void shareText(String platform, String text, JSCallback callback) {
//        new ShareAction(getActivity())
//                .setPlatform(getPlatform(platform))
//                .withText(text)
//                .setCallback(getUMShareListener(callback))
//                .share();
//    }


//    @JSMethod
//    public void shareImage(String platform, String thumimg, String imgurl, String title, String text, JSCallback callback) {
//        UMImage image = new UMImage(getActivity(), imgurl);
//        UMImage thumb = new UMImage(getActivity(), thumimg);
//        image.setThumb(thumb);
//        image.setTitle(title);
//        image.setDescription(text);
//        new ShareAction(getActivity())
//                .withText(text)
//                .setPlatform(getPlatform(platform))
//                .withMedia(image)
//                .setCallback(getUMShareListener(callback))
//                .share();
//    }


//    -(void)shareWebPage:(NSString*)platform imgurl:(NSString*)imgurl  desc:(NSString*)desc  url:(NSString*)url callback:(WXModuleKeepAliveCallback)callback


//    @JSMethod
//    public void shareWebPage(String platform, String imgurl, String title, String desc, String url, JSCallback callback) {
//        UMWeb web = new UMWeb(url);
//        web.setTitle(title);//标题
//        web.setThumb(new UMImage(getActivity(), imgurl));  //缩略图
//        web.setDescription(desc);//描述
//        new ShareAction(getActivity())
//                .withMedia(web)
//                .setPlatform(getPlatform(platform))
//                .setCallback(getUMShareListener(callback))
//                .share();
//    }


    public UMShareListener getUMShareListener(final JSCallback callback) {
        final HashMap m = new HashMap();
        return new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
//                callback(@{@"res":resp.originalResponse,@"err":@(0)},false);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                m.put("err", 0);
                m.put("res", 0);
                callback.invoke(m);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                m.put("err", 1);
                m.put("res", throwable.getMessage());
                callback.invoke(m);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                m.put("err", 2);
                m.put("res", "分享取消");
                callback.invoke(m);
            }
        };
    }

    public SHARE_MEDIA getPlatform(String name) {
        if ("wechat-timeline".equals(name)) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        } else if ("wechat-chat".equals(name)) {
            return SHARE_MEDIA.WEIXIN;
        } else if ("qzone".equals(name)) {
            return SHARE_MEDIA.QZONE;
        } else if ("qq".equals(name)) {
            return SHARE_MEDIA.QQ;
        } else if ("sina".equals(name)) {
            return SHARE_MEDIA.SINA;
        } else if ("tencentwb".equals(name)) {
            return SHARE_MEDIA.TENCENT;
        }
        return SHARE_MEDIA.WEIXIN_CIRCLE;

    }

    public String platformToName(SHARE_MEDIA p) {
        if (p == SHARE_MEDIA.WEIXIN_CIRCLE) {
            return "wechat-timeline";
        } else if (p == SHARE_MEDIA.WEIXIN) {
            return "wechat-chat";
        } else if (p == SHARE_MEDIA.QZONE) {
            return "qzone";
        } else if (p == SHARE_MEDIA.QQ) {
            return "qq";
        } else if (p == SHARE_MEDIA.SINA) {
            return "sina";
        } else if (p == SHARE_MEDIA.TENCENT) {
            return "tencentwb";
        }
        return "wx_timeline";
    }

    @JSMethod
    public void onPageStart(String pageName){
        MobclickAgent.onPageStart(pageName);
    }

    @JSMethod
    public void onPageEnd(String pageName){
        MobclickAgent.onPageEnd(pageName);
    }

}
