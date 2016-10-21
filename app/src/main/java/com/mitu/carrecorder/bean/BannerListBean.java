package com.mitu.carrecorder.bean;

import com.mitu.carrecorder.entiy.Banner;

import java.util.ArrayList;

/**
 * 说明：
 * 2016/5/13 0013
 */
public class BannerListBean extends ResultBean {

    public ArrayList<Banner> arrayList;
    //
    public BannerListBean(){
        arrayList = new ArrayList<>();
    }
}
