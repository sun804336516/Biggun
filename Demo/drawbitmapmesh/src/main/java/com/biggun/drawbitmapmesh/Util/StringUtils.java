package com.biggun.drawbitmapmesh.Util;


import android.content.Context;
import android.content.pm.PackageManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 王大枪 on 2015/10/16.
 */
public class StringUtils
{
    public static String FormatString(int time)
    {
        StringBuffer buffer = new StringBuffer();
        time /= 1000;
        int hh = 00, mm = 00, ss = 00;
        hh = time / 60 / 60;
        buffer.append(hh == 0 ? "00" : hh < 10 ? decemail(hh) : hh).append(":");
        mm = time % 3600 / 60;
        buffer.append(mm == 0 ? "00" : mm < 10 ? decemail(mm) : mm).append(":");
        ss = time % 60;
        buffer.append(ss < 10 ? decemail(ss) : ss);
        return buffer.toString();
    }

    private static String decemail(int ss)
    {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(ss);
    }

    public static String FormatString2(int time, boolean issecond)
    {
        if (!issecond) {
            time /= 1000;
        }
        int hh = time / 3600;
        int mm = time % 3600 / 60;
        int ss = time % 60;
//        if( hh != 0)
//        {
//            return String.format("%02d:%02d:%02d",hh,mm,ss);
//        }else{
//            return String.format("%02d:%02d",mm,ss);
//        }
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    public static String DecimalTwo(float size)
    {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(size);
    }


    public static String MD5Format(String path)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(path.getBytes());
            return FormateByte(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return path.hashCode() + "";
        }
    }

    private static String FormateByte(byte[] bytes)
    {
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            String str = Integer.toHexString(b & 0xff);
            if (str.length() <= 1) {
                buffer.append("0");
            }
            buffer.append(str);
        }
        return buffer.toString();
    }


    public static boolean isAppInstalled(String appPackageName, Context context)
    {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appPackageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return false;
    }


    public static void CopyFileFromAssest(InputStream is, File newFile) throws IOException
    {
        BufferedInputStream bis = new BufferedInputStream(is, 8 * 1024);
        if (newFile.exists()) {
            newFile.delete();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile), 8 * 1024);
        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, length);
        }
        bos.flush();
        if (is != null) {
            is.close();
        }
        if (bis != null) {
            bis.close();
        }
        if (bos != null) {
            bos.close();
        }
    }

    public static String RandomRead(String fileName) throws IOException
    {
        return RandomRead(new File(fileName));
    }

    public static String RandomRead(File file) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        String line = null;
        while ((line = raf.readLine())!= null)
        {
            builder.append(line);
        }
        raf.close();
        return new String(builder.toString().getBytes("iso8859-1"),"gbk");
    }

    public static void Readzhangjie(String text)
    {
        Pattern pattern = Pattern.compile("第(.*?)章\\s+(.*?)\\s");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            LogUtils.e("章节：" + matcher.group(0));
        }
    }
}
