package com.biggun.drawbitmapmesh.Util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.Interface.LoadBitmap;
import com.jakewharton.disklrucache.DiskLruCache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by 王大枪 on 2015/9/22.
 */
public class ImageLoader
{
    private String flag = "110";

    private static ImageLoader loader;
    private LruCache<String, Bitmap> lruCache;
    private DiskLruCache diskLruCache;
    private File cacheFile;
    private int appVersion = 1;
    private long MaxSize = 20 * 1024 * 1024;
    private OkHttpClient okHttpClient;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULTTHREAD_COUNT = 1;

    public static final int LIFO = 0;
    public static final int FIFO = 1;
    /**
     * 队列的调度方式
     */
    private static int mType = LIFO;

    private LinkedList<Runnable> taskList;//线程不安全 volatile来同步线程

    private List<Runnable> taskList2 = Collections.synchronizedList(new LinkedList<Runnable>());

    private LinkedBlockingDeque<Runnable> taskList3 = new LinkedBlockingDeque<>();
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Semaphore PoolSeamphore;
    private Handler mPoolThreadHandler;
    private Semaphore handlerSemaphore = new Semaphore(0);

    /**
     * UI线程中的Handler
     */
    private Handler mUiHandler;

    private ImageLoader(int threadCount, int type)
    {
        this.mType = type;
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);

        PoolSeamphore = new Semaphore(threadCount);
        int size = (int) Runtime.getRuntime().maxMemory();
        lruCache = new LruCache<String, Bitmap>(size / 8)
        {
            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                return value.getByteCount();
            }
        };
        taskList = new LinkedList<>();
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mPoolThread = new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                mPoolThreadHandler = new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        super.handleMessage(msg);
                        //去线程池取一个任务执行
                        try {
                            PoolSeamphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mThreadPool.execute(getTask());
                    }
                };
                handlerSemaphore.release();
                Looper.loop();
            }
        };
        mPoolThread.start();
    }

    private Runnable getTask()
    {
        synchronized (flag) {
            if (mType == LIFO) {
                return taskList.removeLast();
            } else {
                return taskList.removeFirst();
            }
        }
    }

    public static ImageLoader getInstance(int count, int type)
    {
        if (loader == null) {
            synchronized (ImageLoader.class) {
                if (loader == null) {
                    loader = new ImageLoader(count, type);
                }
            }
        }
        return loader;
    }

    public void loadBitmap(String url, final ImageView img)
    {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mUiHandler == null) {
            mUiHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    super.handleMessage(msg);
                    //获取到图片进行加载
                    ImageHolder holder = (ImageHolder) msg.obj;
                    String path = holder.path;
                    Bitmap bit = holder.bitmap;
                    ImageView imageView = holder.img;
                    if (mLoadBitmap != null) {
                        if (bit != null && path.equals(imageView.getTag().toString())) {
                            mLoadBitmap.LoadSuccess(path, imageView, bit);
                        } else {
                            mLoadBitmap.LoadError(path, imageView);
                        }
                    } else if (path.equals(imageView.getTag().toString())) {
                        imageView.setImageBitmap(bit);
                        imageView.setAlpha(0f);
                        imageView.setScaleX(1.1f);
                        imageView.setScaleY(1.1f);
                        imageView.animate().setDuration(200).alpha(1f).scaleX(1.0f).scaleY(1.0f).start();
                    }

                }
            };
        }
        img.setTag(url);
        if (url.startsWith("http:") || url.startsWith("https:")) {
            loadNetImag(url, img);
        } else {
            loadLocalImg(url, img);
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @param img
     */
    private void loadLocalImg(final String url, final ImageView img)
    {
        final Bitmap bitmap = getBitmapFromLrucache(url);
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {
            addTask(new Runnable()
            {
                @Override
                public void run()
                {
                    //根据imageview大小压缩图片
                    Images images = getImages(img);
                    Bitmap bitmap2 = compressionBitmap(images.width, images.height, url);

                    putBitmapInLrucache(url, bitmap2);
                    ImageHolder holder = new ImageHolder(img, url, bitmap2);
                    Message msg = mUiHandler.obtainMessage();
                    msg.obj = holder;
                    mUiHandler.sendMessage(msg);
                    PoolSeamphore.release();
                }
            });
        }
    }

    private void addTask(Runnable runnable)
    {
        synchronized (flag) {
            taskList.add(runnable);
        }
        if (mPoolThreadHandler == null) {
            try {
                handlerSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //通知后台轮询线程
        mPoolThreadHandler.sendEmptyMessage(0x000);
    }

    /**
     * 根据ImageView的大小压缩图片
     *
     * @param width
     * @param height
     * @param url
     * @return
     */
    private Bitmap compressionBitmap(int width, int height, String url)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        int sampleSize = 1;
        if (options.outWidth > width || options.outHeight > height) {
            sampleSize = Math.max((int) (options.outWidth * 1.0f / width), (int) (options.outHeight * 1.0f / height));
        }
        LogUtils.e(width + "," + height + "," + sampleSize);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
//        options.inBitmap
        return BitmapFactory.decodeFile(url, options);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Images getImages(ImageView img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        if (width <= 0) {
            ViewGroup.LayoutParams lp = img.getLayoutParams();
            width = lp.width;
        }
        if (width <= 0) {
            width = img.getMaxWidth();
        }
        if (width <= 0) {
            width = img.getContext().getResources().getDisplayMetrics().widthPixels;
        }
        if (height <= 0) {
            ViewGroup.LayoutParams lp = img.getLayoutParams();
            height = lp.height;
        }
        if (height <= 0) {
            height = img.getMaxHeight();
        }
        if (height <= 0) {
            height = img.getContext().getResources().getDisplayMetrics().heightPixels;
        }
        return new Images(width, height);
    }

    private Bitmap getBitmapFromLrucache(String url)
    {
        Bitmap bitmap = lruCache.get(url);
        return bitmap;
    }

    private void putBitmapInLrucache(String url, Bitmap bitmap)
    {
        if (url == null || bitmap == null) {
            return;
        }
        if (getBitmapFromLrucache(url) == null) {
            lruCache.put(url, bitmap);
        }
    }

    /**
     * 加载网络图片
     *
     * @param url
     */
    private void loadNetImag(final String url, final ImageView img)
    {
        if (cacheFile == null) {
            throw new IllegalStateException("请调用setCacheFile()方法添加缓存文件夹");
        }
        /**
         * 1 先在lrucache中获取图片
         * 2 1没有取到则去diskLrucache中取
         * 3 2没有取到则去网络请求
         */
        Bitmap bitmap = getBitmapFromLrucache(url);
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {
            addTask(new Runnable()
            {
                @Override
                public void run()
                {
                    InputStream is = null;
                    String key = StringUtils.MD5Format(url);
                    try {
                        DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
                        LogUtils.e("snapshot==" + key);
                        if (snapshot == null) {
                            DiskLruCache.Editor editor = diskLruCache.edit(key);
                            OutputStream outputStream = editor.newOutputStream(0);
                            if (getBitmapFromNet(outputStream, url)) {
                                editor.commit();
                            } else {
                                editor.abort();
                            }
                            snapshot = diskLruCache.get(key);
                        }
                        if (snapshot != null) {
                            is = snapshot.getInputStream(0);
                            Bitmap map = BitmapFactory.decodeStream(is);
                            if (map != null) {
                                putBitmapInLrucache(url, map);
                            }
                            ImageHolder holder = new ImageHolder(img, url, map);
                            Message msg = mUiHandler.obtainMessage();
                            msg.obj = holder;
                            mUiHandler.sendMessage(msg);
                            PoolSeamphore.release();
                        }
                    } catch (IOException e) {
                        LogUtils.e("IOException:" + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        Utils.CloseIo(is);
                    }
                }
            });
        }
    }

    private boolean getBitmapFromNet(final OutputStream outputStream, String url)
    {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            LogUtils.e("" + body.contentLength());
            bis = new BufferedInputStream(body.byteStream(), 10 * 1024);
            bos = new BufferedOutputStream(outputStream, 10 * 1024);
            byte[] b = new byte[1024];
            int lengh = 0;
            while ((lengh = bis.read(b)) != -1) {
                bos.write(b, 0, lengh);
            }
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.CloseIo(bis);
            Utils.CloseIo(bos);
        }
        return false;
    }

    private class ImageHolder
    {
        ImageView img;
        String path;
        Bitmap bitmap;

        public ImageHolder(ImageView img, String path, Bitmap bitmap)
        {
            this.img = img;
            this.path = path;
            this.bitmap = bitmap;
        }
    }

    private class Images
    {
        int width;
        int height;

        public Images(int width, int height)
        {
            this.width = width;
            this.height = height;
        }
    }

    public void setCacheFile(String cacheFile)
    {
        this.cacheFile = new File(cacheFile);
        try {
            diskLruCache = DiskLruCache.open(this.cacheFile, appVersion, 1, MaxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAppVersion(int appVersion)
    {
        this.appVersion = appVersion;
    }

    LoadBitmap mLoadBitmap;

    public void setLoadBitmapListener(LoadBitmap loadBitmap)
    {
        mLoadBitmap = loadBitmap;
    }

    /**
     * 清除lrucache缓存
     */
    public void clearCache()
    {
        if(lruCache == null)
        {
            return;
        }
        lruCache.evictAll();
    }

}
