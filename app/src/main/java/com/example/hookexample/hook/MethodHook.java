package com.example.hookexample.hook;

import com.xmotion.sui.home.hook.compat.Memory;
import com.xmotion.sui.home.hook.compat.MethodCompat;

import java.lang.reflect.Method;

public class MethodHook {

    public static void hook(Method origin, Method replace) {
        Memory.memcpy(MethodCompat.getArtMethodAddress(origin),
                MethodCompat.getArtMethodAddress(replace),
                MethodCompat.getArtMethodSize());
    }
}
