package com.example.hookexample.hook;

import com.xmotion.sui.home.hook.compat.AMSCompat;

public class AMSHook {

    private static BinderHook mHook;

    public static void hook(BinderHook.BinderHookInvoker invoker) {
        Object object = AMSCompat.getIActivityManager();
        mHook = new BinderHook(object, invoker);
        AMSCompat.setIActivityManager(mHook.getProxyInterface());
    }

    public static void restore() {
        AMSCompat.setIActivityManager(mHook.getOriginalInterface());
    }
}
