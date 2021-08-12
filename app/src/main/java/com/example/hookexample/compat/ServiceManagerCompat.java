package com.example.hookexample.compat;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

public class ServiceManagerCompat {

    private static Class<?> serviceManagerClazz;

    private static Class<?> iserviceManagerClazz;

    private static Field sServiceManagerField;

    public static Class<?> getServiceManagerClazz() {
        if (serviceManagerClazz == null) {
            try {
                serviceManagerClazz = ClassUtils.getClass("android.os.ServiceManager");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return serviceManagerClazz;
    }

    public static Class<?> getIServiceManagerClazz() {
        if (iserviceManagerClazz == null) {
            try {
                iserviceManagerClazz = ClassUtils.getClass("android.os.IServiceManager");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return iserviceManagerClazz;
    }

    public static Object getsServiceManager() {
        try {
            return getsServiceManagerField().get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getsServiceManagerField() {
        if (sServiceManagerField == null) {
            sServiceManagerField = FieldUtils.getField(getServiceManagerClazz(), "sServiceManager", true);
        }
        return sServiceManagerField;
    }

    public static Object getsCache() {
        try {
            return FieldUtils.getField(getServiceManagerClazz(), "sCache", true).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
