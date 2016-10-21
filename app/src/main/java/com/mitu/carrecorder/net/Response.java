package com.mitu.carrecorder.net;

import com.mitu.carrecorder.bean.BannerListBean;
import com.mitu.carrecorder.bean.UserBean;
import com.mitu.carrecorder.entiy.Banner;
import com.mitu.carrecorder.entiy.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 响应解析类
 * @author
 */
public class Response {


    /**
     * 解析登录反馈
     * @param response 反馈字符串
     * @return 解析结果对象
     */
    public static UserBean parseLoginResult(String response){
        UserBean userBean = new UserBean();
        try {
            JSONObject object = new JSONObject(response);
            if (object.getInt("resultscode") == 1){
                userBean.result = true;
                JSONObject child = object.getJSONObject("vipuser");
                userBean.user = parseUserInfo(child);

            }else {
                userBean.result = false;
                userBean.msg = object.getString("errorMessage");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userBean;
    }

    /**
     *
     * @param response
     * @return
     */
    public static UserBean parseDefaultLoginResult(String response){

        UserBean bean = new UserBean();
        bean.jsonString = response;
        String result = response.trim();
        if (result == null || result.length() == 0 || result.equals("{}")){
            bean.result = false;
            bean.msg = "登陆失败";
        }else {
            try {
                JSONObject object = new JSONObject(response);
                if (object != null){
                    bean.result = true;
                    bean.jsonString = response;
                    User user = parseUserInfo(object);
                    bean.user = user;

                }else{
                    bean.result = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                bean.result = false;
                bean.msg = "数据解析失败！";
            }
        }
        return bean;
    }

    /***
     * 注册结果解析
     * @param response
     * @return
     */
    public static UserBean parseUserRegisteResult(String response){
        UserBean userBean = new UserBean();
        try {
            JSONObject object = new JSONObject(response);
            if (object.getInt("resultscode") == 1){
                userBean.result = true;
                JSONObject child = object.getJSONObject("vipuser");
                userBean.user = parseUserInfo(child);

            }else {
                userBean.result = false;
                userBean.msg = object.getString("errorMessage");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userBean;
    }

    private static User parseUserInfo(JSONObject object){
        User user = new User();

        try {
            user.setUsername(object.getString("username"));
            user.setId(object.getInt("id"));
            user.setChannelid(object.getString("channelid"));
            user.setDate(object.getString("date"));
            user.setDeviceid(object.getString("deviceid"));
            user.setDevicename(object.getString("devicename"));
            user.setImage(object.getString("image"));
            user.setIsdel(object.getInt("isdel"));
            user.setLevelname(object.getString("levelname"));
            user.setNickname(object.getString("nickname"));
            user.setOsType(object.getInt("osType"));
            user.setQquid(object.getString("qquid"));
            user.setSpeed(object.getString("speed"));
            user.setTotalmileage(object.getString("totalmileage"));
            user.setTotaltime(object.getString("totaltime"));
            user.setUsername(object.getString("username"));
            user.setWeibouid(object.getString("weibouid"));
            user.setWeixinuid(object.getString("weixinuid"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }


    public static User parseUserInfo(String jsonString){
        User user = new User();
        try {
            user = parseUserInfo(new JSONObject(jsonString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }


    public static BannerListBean parseADBannerList(String response){
        BannerListBean bannerListBean = new BannerListBean();
        try {
            JSONObject object = new JSONObject(response);
            if (object != null){
                int resultCode = object.getInt("resultscode");
                if (resultCode == 1){
                    bannerListBean.result = true;
                    JSONArray array = object.getJSONArray("list");
                    if (array != null && array.length() != 0){
                        for (int i = 0; i < array.length(); i++) {
                            Banner banner = new Banner();
                            JSONObject child = array.getJSONObject(i);
                            banner.setTitle(child.getString("title"));
                            banner.setImgUrl(child.getString("url"));
                            banner.setCnUrl(child.getString("cn_url"));
                            banner.setEnUrl(child.getString("en_url"));
                            bannerListBean.arrayList.add(banner);
                        }
                    }else {
                        bannerListBean.result = false;
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bannerListBean;

    }









}
