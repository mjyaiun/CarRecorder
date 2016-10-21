package com.mitu.carrecorder.net;

import com.google.gson.Gson;
import com.guotiny.httputils.callback.Callback;
import com.mitu.carrecorder.entiy.User;

import java.io.IOException;

import okhttp3.Response;

/**
 * 说明：
 * 2016/6/14 0014
 */
public abstract class UserCallBack extends Callback<User>
{
    @Override
    public User parseNetworkResponse(Response response, int id) throws IOException
    {

        String string = response.body().string();
        User user = new Gson().fromJson(string, User.class);
        return user;
    }


}
