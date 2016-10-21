package com.mitu.carrecorder.net;

import com.google.gson.Gson;
import com.guotiny.httputils.callback.Callback;
import com.mitu.carrecorder.entiy.Banner;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * 说明：
 * 2016/6/24 0024
 */
public abstract class BannerListCallBack extends Callback<ArrayList<Banner>> {

    ArrayList<Banner> banners = new ArrayList<>();

    @Override
    public ArrayList<Banner> parseNetworkResponse(Response response, int i) throws Exception {

        String string = response.body().string();
        banners = new Gson().fromJson(string, ArrayList.class);
        return banners;
    }



}
