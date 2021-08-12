package com.example.hookexample.hook;

import android.os.Handler;

import com.xmotion.sui.home.hook.compat.ActivityThreadCompat;

public class ActivityThreadHook {

    public static final int MSG_LAUNCH_ACTIVITY = 100;

    private static Handler mH;

    public static void hookMH(Handler.Callback callback) {
        mH = ActivityThreadCompat.getmH();
        HandlerHook.hook(mH, callback);
    }

    public static void restoreMH() {
        HandlerHook.restore(mH);
    }

}
