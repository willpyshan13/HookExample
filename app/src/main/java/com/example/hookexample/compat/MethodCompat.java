package com.example.hookexample.compat;

import com.xmotion.sui.home.hook.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MethodCompat {

    private static void test1() {

    }

    private static void test2() {

    }

    public static Object getArtMethod(Method method) {
        try {
            Field field = Method.class.getSuperclass().getDeclaredField("artMethod");
            field.setAccessible(true);
            return field.get(method);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getArtMethodAddress(Method method) {
        try {
            Object artMethod = getArtMethod(method);
            if (artMethod.getClass().equals(Long.class)) {
                return (Long) artMethod;
            }
            return Unsafe.getObjectAddress(artMethod);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getArtMethodSize() {
        try {
            Method f1 = MethodCompat.class.getDeclaredMethod("test1");
            Method f2 = MethodCompat.class.getDeclaredMethod("test2");
            long f2Addr = getArtMethodAddress(f2);
            long f1Addr = getArtMethodAddress(f1);
            LogUtils.v(String.format("0x%X - 0x%X = %d", f2Addr, f1Addr, f2Addr - f1Addr));
            return f2Addr - f1Addr;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }
}
