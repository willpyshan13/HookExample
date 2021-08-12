package com.example.hookexample.hook;

import android.os.IBinder;

import com.xmotion.sui.home.hook.utils.BinderUtils;
import com.xmotion.sui.home.hook.utils.LogUtils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BinderHook<T> {

    public Class<?> binderIntfClazz;

    public T originalInterface;

    public IBinder originalBinder;

    public T proxyInterface;

    public IBinder proxyBinder;

    public BinderHook(T originalInterface, BinderHookInvoker invoker) {
        this.originalInterface = originalInterface;
        LogUtils.v(String.format("originalInterface: %s", originalInterface));

        this.binderIntfClazz = BinderUtils.getBinderInterface(originalInterface);
        LogUtils.v(String.format("binderIntfClazz: %s", binderIntfClazz));

        this.originalBinder = getOriginalBinder();
        LogUtils.v(String.format("originalBinder: %s", originalBinder));

        this.proxyInterface = getProxyInterface(invoker);
        LogUtils.v(String.format("proxyInterface = %s", proxyInterface));

        this.proxyBinder = getProxyBinder();
        LogUtils.v(String.format("proxyBinder = %s", proxyBinder));
    }

    private IBinder getOriginalBinder() {
        Method method = MethodUtils.getAccessibleMethod(binderIntfClazz, "asBinder");
        try {
            return (IBinder) method.invoke(originalInterface);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private IBinder getProxyBinder() {
        return (IBinder) Proxy.newProxyInstance(IBinder.class.getClassLoader(),
                new Class<?>[]{IBinder.class}, new BinderHookHandler(originalBinder, "Binder") {
                    @Override
                    public Object onInvoke(Object original, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("queryLocalInterface")) {
                            return proxyInterface;
                        }
                        return method.invoke(original, args);
                    }
                });
    }

    private T getProxyInterface(final BinderHookInvoker invoker) {
        return (T) Proxy.newProxyInstance(binderIntfClazz.getClassLoader(),
                ClassUtils.getAllInterfaces(originalInterface.getClass()).toArray(new Class<?>[0]),
                new BinderHookHandler(originalInterface, "Interface") {
                    @Override
                    public Object onInvoke(Object original, Method method, Object[] args) throws Throwable {
                        return invoker.onInvoke(original, method, args);
                    }
                });
    }

    public interface BinderHookInvoker {
        Object onInvoke(Object original, Method method, Object[] args) throws Throwable;
    }

    abstract static class BinderHookHandler implements InvocationHandler, BinderHookInvoker {

        Object originalObject;

        String tag;

        public BinderHookHandler(Object originalObject, String tag) {
            this.tag = tag;
            this.originalObject = originalObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class == method.getDeclaringClass()) {
                String name = method.getName();
                if ("equals".equals(name)) {
                    return proxy == args[0];
                } else if ("hashCode".equals(name)) {
                    return System.identityHashCode(proxy);
                } else if ("toString".equals(name)) {
                    return proxy.getClass().getName() + "@" +
                            Integer.toHexString(System.identityHashCode(proxy));
                } else {
                    throw new IllegalStateException(String.valueOf(method));
                }
            }

            LogUtils.v(String.format("Intercepted %s >>> %s", tag, method));
            return onInvoke(originalObject, method, args);
        }
    }

    public T getProxyInterface() {
        return proxyInterface;
    }

    public T getOriginalInterface() {
        return originalInterface;
    }
}
