package com.example.hookexample.hook;

import android.os.IBinder;

import com.xmotion.sui.home.hook.compat.ServiceManagerCompat;

import java.util.HashMap;


public class ServiceManagerHook {

    private static BinderHook mHookedBinder;

    private static HashMap<String, BinderHook> mCache = new HashMap<>();

    private static HashMap<String, IBinder> sCache;

    public static void hook() {
        Object sServiceManager = ServiceManagerCompat.getsServiceManager();
        sCache = (HashMap<String, IBinder>) ServiceManagerCompat.getsCache();

//        mHookedBinder = new BinderHook(sServiceManager, new BinderHook.BinderHookInvoker() {
//            @Override
//            public Object onInvoke(Object original, Method method, Object[] args) throws Throwable {
//                if (method.getName().equals("getService")) {
//                    String name = (String) args[0];
//                    BinderHook hookBinder = mCache.get(name);
//                    if (hookBinder != null) {
//                        return hookBinder.proxyBinder;
//                    }
//                }
//                return method.invoke(original, args);
//            }
//        });

        try {
            ServiceManagerCompat.getsServiceManagerField().set(null, mHookedBinder.proxyInterface);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
