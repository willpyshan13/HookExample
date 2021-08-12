package com.example.hookexample.compat;

import android.content.Intent;
import android.os.Handler;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ActivityThreadCompat {

    public static final int RECEIVER = 113;

    public static Class<?> getActivityThreadClazz() {
        try {
            return ClassUtils.getClass("android.app.ActivityThread");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getHClazz() {
        try {
            return ClassUtils.getClass("android.app.ActivityThread$H");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getmHField() {
        return FieldUtils.getField(getActivityThreadClazz(), "mH", true);
    }

    public static Handler getmH() {
        try {
            return (Handler) getmHField().get(getActivityThread());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent getActivityClientRecordIntent(Object record) {
        try {
            return (Intent) FieldUtils.getField(record.getClass(), "intent", true).get(record);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getActivityThread() {
        try {
            return MethodUtils.invokeStaticMethod(getActivityThreadClazz(), "currentActivityThread");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent getReceiverDataIntent(Object receiverData) {
        try {
            Class<?> clazz = ClassUtils.getClass("android.app.ActivityThread$ReceiverData");
            return (Intent) FieldUtils.getField(clazz, "intent", true).get(receiverData);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
