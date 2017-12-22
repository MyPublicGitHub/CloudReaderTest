package com.example.cloudreadertest.http.rx;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class RxBusBaseMessage {

    private int mCode;
    private Object mObject;

    public RxBusBaseMessage(int code, Object object) {
        mCode = code;
        mObject = object;
    }

    public RxBusBaseMessage(){}

    public int getCode(){
        return mCode;
    }

    public Object geObject(){
        return mObject;
    }
}
