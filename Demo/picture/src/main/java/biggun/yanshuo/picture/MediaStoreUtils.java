package biggun.yanshuo.picture;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.biggun.yslibrary.Common;

import java.util.List;

import biggun.yanshuo.picture.Bean.FileBean;
import biggun.yanshuo.picture.Bean.ImageDirectory;

/**
 * 作者：王大枪 on 2016/6/13 13:56
 * 邮箱：sun91985415@163.com
 */
public class MediaStoreUtils
{
    /**
     * 通过图片的mime类型获取图片地址父容器，筛选地址用的
     *
     * @param context
     * @return
     */
    public static void getImageList(Context context, String[] selectionArgs, List<ImageDirectory> list)
    {
        //查找图片
        Uri imageuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projections = new String[]
                {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, len = selectionArgs.length; i < len; i++) {
            buffer.append(MIMETYPE_EQUAL);
            if (i != len - 1) {
                buffer.append(OR);
            }
        }
        Cursor Image_cursor = context.getContentResolver().query(imageuri, projections, buffer.toString()
                , selectionArgs, FILE_ADDED_SORDER_DESC);
        if (Image_cursor != null && !Image_cursor.isClosed()) {
            ImageDirectory directory = null;
            String parentPath = null;
            String name = null;
            String representPath = null;
            while (Image_cursor.moveToNext()) {
                long size = Image_cursor.getLong(Image_cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                if (size <= 0) {
                    continue;
                }
                representPath = Image_cursor.getString(Image_cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                parentPath = representPath.substring(0, representPath.lastIndexOf("/"));
                name = parentPath.substring(parentPath.lastIndexOf("/") + 1);
                directory = new ImageDirectory(name, parentPath, representPath);

                if (!list.contains(directory)) {
                    list.add(directory);
                }
            }
            Image_cursor.close();
        }
    }

    /**
     * 根据指定的DATA类型来获取文件地址
     *
     * @param mcontext
     * @param list
     * @param args
     */
    public static void getFileList(Context mcontext, List<FileBean> list, String[] args)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, len = args.length; i < len; i++) {
            buffer.append(DATA_LIKE);
            if (i != len - 1) {
                buffer.append(OR);
            }
        }
        String[] projections =
                {MediaStore.Files.FileColumns.TITLE
                        , MediaStore.Files.FileColumns.DATA
                        , MediaStore.Files.FileColumns.DISPLAY_NAME
                        , MediaStore.Files.FileColumns.SIZE};
        Cursor cursor = mcontext.getContentResolver().query(Common.FILES_URI, projections
                , buffer.toString(), args, FILE_SIZE_SORDER_DESC);
        if (cursor != null && !cursor.isClosed()) {
            cursor.moveToFirst();
            FileBean bean = null;
            String path;
            String name;
            String title;
            long size;
            while (cursor.moveToNext()) {
                size = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                if (size <= 0) {
                    continue;
                }
                name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                bean = new FileBean.Builder().setName(name)
                        .setPath(path).setSize(size).setTitle(title).Build();
                list.add(bean);
            }
            cursor.close();
        }
    }

    //========================================排序========================================//
    static final String ASC = " ASC";
    static final String DESC = " DESC";
    static final String OR = " OR ";
    /**
     * 文件大小降序
     */
    static final String FILE_SIZE_SORDER_DESC = MediaStore.Files.FileColumns.SIZE + DESC;
    static final String FILE_SIZE_SORDER_ASC = MediaStore.Files.FileColumns.SIZE + ASC;
    static final String FILE_ADDED_SORDER_DESC = MediaStore.Files.FileColumns.DATE_ADDED + DESC;
    static final String FILE_ADDED_SORDER_ASC = MediaStore.Files.FileColumns.DATE_ADDED + ASC;

    //========================================LIKE========================================//
    static final String DATA_LIKE = MediaStore.Files.FileColumns.DATA + " LIKE ? ";
    static final String MIMETYPE_EQUAL = MediaStore.Images.Media.MIME_TYPE + " = ? ";
}
