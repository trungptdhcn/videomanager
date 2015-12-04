package com.trungpt.downloadmaster.sync.common;


import com.trungpt.downloadmaster.sync.Configs;
import com.trungpt.downloadmaster.ui.utils.Constant;

/**
 * Created by Trung on 7/22/2015.
 */
public class RestfulService
{
    private static RestfulServiceIn restfulServiceIn;

    public static RestfulServiceIn getInstance(Constant.HOST_NAME host_name)
    {
        restfulServiceIn = RestfulAdapter.getRestAdapter(host_name).create(RestfulServiceIn.class);
        return restfulServiceIn;
    }
}
