package com.guotiny.httputils.builder;


import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.request.OtherRequest;
import com.guotiny.httputils.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
