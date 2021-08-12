package com.example.hookexample.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.xmotion.sui.home.hook.compat.ActivityThreadCompat;
import com.xmotion.sui.home.hook.compat.HandlerCompat;
import com.xmotion.sui.home.hook.utils.LogUtils;

public class ReceiverHook {

    public static void hook() {
        final Object mH = ActivityThreadCompat.getmH();

        HandlerCompat.setCallback(mH, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                LogUtils.e(String.format("handleMessage msg = %d", msg.what));

                switch (msg.what) {
                    case ActivityThreadCompat.RECEIVER:
                        Intent intent = ActivityThreadCompat.getReceiverDataIntent(msg.obj);
                        LogUtils.e(String.format("component = %s, action = %s",
                                intent.getComponent(), intent.getAction()));
                        break;
                }

                return false;
            }
        });
    }
}
