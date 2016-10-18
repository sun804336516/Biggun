package com.biggun.drawbitmapmesh.Util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 孙贤武 on 2016/1/8.
 */
public class Utils
{
    private static final String TAG = "==";
    private static long onece = System.currentTimeMillis();
    private static final long TIME = 200;

    public static void LogE(String msg)
    {
        Log.e(TAG, msg);
    }

    public static void CloseIo(Closeable closeable)
    {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isDoubleClick()
    {
        boolean b = false;
        if (System.currentTimeMillis() - onece < 200) {
            b = true;
        }
        onece = System.currentTimeMillis();
        return b;
    }

    public static boolean secondInstall(String apkPath)
    {
        LogE("exits:" + apkPath);
        DataOutputStream dos = null;
        BufferedReader bufferedReader = null;
        try {
            String command = "pm install -r " + apkPath + "\n";
            Process process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.write(command.getBytes(Charset.forName("UTF-8")));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();

            bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String info = "";
            StringBuilder builder = new StringBuilder();
            while ((info = bufferedReader.readLine()) != null) {
                builder.append(info);
            }
            LogE("Builder----:" + builder.toString());

            if (!InstallFailure(builder.toString())) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogE("IOException:" + e.getMessage());
        } catch (InterruptedException e) {
            LogE("InterruptedException:" + e.getMessage());
            e.printStackTrace();
        } finally {
            CloseIo(dos);
            CloseIo(bufferedReader);
        }
        return false;
    }

    /**
     * 秒装失败的提示信息
     *
     * @param message
     * @return
     */
    private static boolean InstallFailure(String message)
    {
        return (message.contains("Failure") || message.contains("Error")) || message.contains("denied");
    }

    public static boolean isRoot()
    {
        return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
    }

    /**
     * 获取文件的mimeTyoe
     *
     * @param file
     * @return
     */
    public static String getMimeType(File file)
    {
        return getMimeType(file.getAbsolutePath());
    }

    /**
     * 获取文件的mimeType
     *
     * @param fileString
     * @return
     */
    public static String getMimeType(String fileString)
    {
        String mimeType = "";
        String extensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(fileString);
        if (extensionFromUrl != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensionFromUrl);
        }
        return mimeType;
    }

    /**
     * 根据类名获取类的属性
     */
    public static void getClassmode(Object obj)
    {
        Field[] declaredFields = obj.getClass().getDeclaredFields();

        for (int i = 0, len = declaredFields.length; i < len; i++) {
            String name = declaredFields[i].getName();
            String type = declaredFields[i].getGenericType().toString();
            boolean access = declaredFields[i].isAccessible();
            declaredFields[i].setAccessible(true);
            try {
                Object o = declaredFields[i].get(obj);
                Utils.LogE("类中包含的属性：" + type + "--" + name + "=" + o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            declaredFields[i].setAccessible(access);
        }
    }

    /**
     * 首字母取大写
     *
     * @param str
     * @return
     */
    public static String setSet(String str)
    {
        return "set" + str.toUpperCase().substring(0, 1) + str.substring(1);
    }

    /**
     * 字符串是否是数字1
     *
     * @param str
     * @return
     */
    public static boolean isStringisNum1(String str)
    {
        Pattern compile = Pattern.compile("[0-9]*");
        Matcher matcher = compile.matcher(str);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 字符串是否是数字2
     *
     * @param str
     * @return
     */
    public static boolean isStringisNum2(String str)
    {
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串是否是boolean
     *
     * @param str
     * @return
     */
    public static boolean isStringisBoolean(String str)
    {
        return str.equals("true") || str.equals("false");
    }

    public static boolean isTrue(String str)
    {
        return "true".equalsIgnoreCase(str);
    }

    /**
     * 背景虚化   api17
     *
     * @param context
     * @param bkg
     * @param radius
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap bkg, float radius)
    {
        Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, 0, 0, null);
        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        rs.destroy();
        return overlay;
//        return new BitmapDrawable(context.getResources(),overlay);
    }

    public static boolean isOverMarshmallow()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isOverLollipop()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void CallPhone(Activity activity, int phoneNum)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        activity.startActivity(intent);
    }

    /**
     * 固定文本高亮
     *
     * @param color
     * @param text
     * @param keyWord
     * @return
     */
    public static SpannableString setkeyWordColor(int color, String text, String keyWord)
    {
        SpannableString spannableString = new SpannableString(text);
        Pattern p = Pattern.compile(keyWord);
        Matcher m = p.matcher(text);
        if (m.find()) {
            int start = m.start();
            int end = m.end();
            spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    /**
     * 多个固定文本高亮
     *
     * @param color
     * @param text
     * @param keywords
     * @return
     */
    public static SpannableString setSkeyWordColor(int color, String text, String[] keywords)
    {
        SpannableString spannableString = new SpannableString(text);
        Pattern p = null;
        Matcher m = null;
        for (String keyword : keywords) {
            p = Pattern.compile(keyword);
            m = p.matcher(text);
            while (m.find()) {
                spannableString.setSpan(new ForegroundColorSpan(color), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    /**
     * 解析二维码图片
     *
     * @param path
     * @return
     * @throws FormatException
     * @throws ChecksumException
     * @throws NotFoundException
     */
    public static Result getQrCodeFromBitmap(String path)
    {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op);
        int scale = (int) (op.outHeight / (float) 200);
        if (scale <= 1) {
            scale = 1;
        }
        op.inJustDecodeBounds = false;
        op.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(path, op);

        Hashtable<DecodeHintType, String> table = new Hashtable<>();
        table.put(DecodeHintType.CHARACTER_SET, "utf-8");

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                pixels[j * bitmap.getWidth() + i] = bitmap.getPixel(i, j);
            }
        }
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(binaryBitmap, table);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重新解码字符
     *
     * @param str
     * @return
     */
    public static String recode(String str)
    {
        String format = "";
        boolean iso = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
        try {
            if (iso) {
                format = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            } else {
                format = str;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 路径对应的File是否是图片
     *
     * @param path
     * @return
     */
    public static boolean isImg(String path)
    {
        return path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg");
    }

    public static boolean isTxt(String txt)
    {
        return txt.toLowerCase().endsWith(".txt");
    }

    /**
     * 获取返回桌面的Intent
     *
     * @return
     */
    public static Intent getHomeIntent()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        return intent;
    }

    /**
     * 截图  整个屏幕--除掉状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap screenShot(Activity activity)
    {
        int width = 0;
        int height = 0;
        int statesBarHeight = 0;
        Bitmap bitmap = null;
        Rect rect = new Rect();

        View decorView = activity.getWindow().getDecorView();
        decorView.getWindowVisibleDisplayFrame(rect);
        statesBarHeight = rect.top;//状态栏高度
        DisplayMetrics displayMetrics =
                activity.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        boolean drawingCacheEnabled = decorView.isDrawingCacheEnabled();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        bitmap = decorView.getDrawingCache();

        bitmap = Bitmap.createBitmap(bitmap, 0, statesBarHeight, width, height - statesBarHeight);
        decorView.destroyDrawingCache();
        decorView.setDrawingCacheEnabled(drawingCacheEnabled);
        return bitmap;
    }

    /**
     * 截图某个View
     *
     * @param view
     * @return
     */
    public static Bitmap screenShot(View view)
    {
        Bitmap bitmap = null;
        boolean cacheEnabled = view.isDrawingCacheEnabled();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();

        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(cacheEnabled);
        return bitmap;
    }


    /**
     * 截屏 命令
     */
    public static final String SCREENCAP = "system/bin/screencap -p ";
    public static final int NEED_ROOT = 0;
    public static final int PATH_NULL = -1;
    public static final int SHOT_OK = 1;
    public static final int NOT_SUPPORT = 2;
    public static final int SHOT_ERROR = 3;

    /**
     * 截屏 需要root
     *
     * @return
     */
    public static int screenShot(String path)
    {
        if (TextUtils.isEmpty(path)) {
            return PATH_NULL;
        }
        if (!isRoot()) {
            return NEED_ROOT;
        }
        Process process = null;
        PrintStream printStream = null;
        int returncode = SHOT_OK;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("su");
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
//            process = Runtime.getRuntime().exec("su");
            printStream = new PrintStream(new BufferedOutputStream(process.getOutputStream(), 8192));

            if (Build.VERSION.SDK_INT >= 9) {
                printStream.println(SCREENCAP + path);
                returncode = SHOT_OK;
            } else {
                returncode = NOT_SUPPORT;
            }
            new ClearThread(process.getErrorStream(), "error").start();
            new ClearThread(process.getInputStream(), "input").start();
            printStream.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            returncode = SHOT_ERROR;
        } finally {
            CloseIo(printStream);
            if (process != null) {
                process.destroy();
            }
        }
        return returncode;
    }

    private static class ClearThread extends Thread
    {
        private InputStream is;
        private String type;

        public ClearThread(InputStream is, String type)
        {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run()
        {
            try {
                super.run();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    Utils.LogE(type + "------" + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseIo(is);
            }
        }
    }

    /**
     * 格式化数据
     *
     * @param data
     * @return B, KB,MB, GB
     */
    public static String Formate(long data)
    {
        if (data <= 1024)//b
        {
            return String.format("%dB", data);
        } else if (data < 1024 * 1024)//kb
        {
            return String.format("%.2fKB", data / 1024f);
        } else if (data < 1024 * 1024 * 1024)//mb
        {
            return String.format("%.2fMB", data / (1024f * 1024f));
        }
        return String.format("%.2fGB", data / (1024f * 1024f * 1024f));//gb
    }

    public static boolean isMobileNum(String str)
    {
//        Pattern pattern = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
        Pattern pattern = Pattern.compile("^[1][358][0-9]{9}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 从drawable中获取图片
     *
     * @param drawable
     * @return
     */
    public static Bitmap getBitmap(Drawable drawable)
    {
        Bitmap bitmap = null;
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } else {
                int wid = drawable.getIntrinsicWidth();
                int hei = drawable.getIntrinsicHeight();
                bitmap = Bitmap.createBitmap(wid, hei, drawable.getOpacity() == PixelFormat.OPAQUE ?
                        Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, wid, hei);
                drawable.draw(canvas);
            }
        }
        return bitmap;
    }


    public static Bitmap compressBitmap(String url, Context context)
    {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int wid = options.outWidth;
        int hei = options.outHeight;
        int samplesize = 1;
        while (wid > metrics.widthPixels || hei > metrics.heightPixels) {
            samplesize++;
            wid /= samplesize;
            hei /= samplesize;
        }
        LogE("samplesize:"+samplesize);
        options.inJustDecodeBounds = false;
        options.inSampleSize = samplesize;
        bitmap = BitmapFactory.decodeFile(url,options);
        return bitmap;
    }
}
