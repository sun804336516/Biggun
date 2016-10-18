package com.biggun.drawbitmapmesh.Util;

import android.util.Log;

/**
 * Created by 王大枪 on 2015/10/10.
 * <p/>
 * Log v d i w e
 */
public class LogUtils
{
    private static String className;
    private static String methodName;
    private static int lineNumber;
    private static String fileName;
    private static boolean isDebuge = true;


    private static final int VERBISE = 1;
    private static final int DEBUGE = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;
    private static int LEVEL = VERBISE;

    private LogUtils()
    {
    }

    private static void init(StackTraceElement[] traceElements)
    {
        className = traceElements[1].getClassName();
        methodName = traceElements[1].getMethodName();
        lineNumber = traceElements[1].getLineNumber();
        fileName = traceElements[1].getFileName();
    }

    private static String showString(String message)
    {
        StringBuffer result = new StringBuffer();
//        result.append(className);
//        result.append(".");
//        result.append(methodName);
        result.append("(");
        result.append(fileName);
        result.append(":");
        result.append(lineNumber);
        result.append("):");
        result.append(message);
        return result.toString();
    }

    public static void v(String message)
    {
        if (!isDebuge)
        {
            return;
        }
        if (LEVEL <= VERBISE)
        {
            init(new Throwable().getStackTrace());
            Log.v(className, showString(message));
        }
    }

    public static void d(String message)
    {
        if (!isDebuge)
        {
            return;
        }
        if (LEVEL <= DEBUGE)
        {
            init(new Throwable().getStackTrace());
            Log.d(className, showString(message));
        }
    }

    public static void i(String message)
    {
        if (!isDebuge)
        {
            return;
        }
        if (LEVEL <= INFO)
        {
            init(new Throwable().getStackTrace());
            Log.i(className, showString(message));
        }
    }

    public static void w(String message)
    {
        if (!isDebuge)
        {
            return;
        }
        if (LEVEL <= WARN)
        {
            init(new Throwable().getStackTrace());
            Log.w(className, showString(message));
        }
    }

    public static void e(String message)
    {
        if (!isDebuge)
        {
            return;
        }
        if (LEVEL <= ERROR)
        {
            init(new Throwable().getStackTrace());
            Log.e(className, showString(message));
        }
    }
}
