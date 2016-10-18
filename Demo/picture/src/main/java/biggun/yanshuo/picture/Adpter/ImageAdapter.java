package biggun.yanshuo.picture.Adpter;

import android.content.Context;
import android.text.TextUtils;

import com.biggun.yslibrary.Base.BIGGUN_Adapter;
import com.biggun.yslibrary.Base.BIGGUN_HolderHandle;
import com.biggun.yslibrary.View.BIGGUN_MaskImageView;

import java.util.List;

import biggun.yanshuo.picture.Bean.ImageBean;
import biggun.yanshuo.picture.R;

/**
 * 作者：Administrator on 2016/6/12 14:10
 * 邮箱：sun91985415@163.com
 * LIKE：YANSHUO
 */
public class ImageAdapter extends BIGGUN_Adapter<ImageBean>
{

    public ImageAdapter(List<ImageBean> mList, Context mContext)
    {
        super(mList, mContext);
    }

    @Override
    protected int getViewId(int viewType)
    {
        return R.layout.image_adapter_layout;
    }

    @Override
    protected void Convert(BIGGUN_HolderHandle BIGGUN_HolderHandle, int position, ImageBean imageBean)
    {
        BIGGUN_MaskImageView imageView = BIGGUN_HolderHandle.getView(R.id.image_adapter_layout_img);
        if (TextUtils.isEmpty(imageBean.getDirname())) {
            imageView.setImageResource(R.drawable.load_image_null);
        } else {
            imageView.setImageResource(R.drawable.load_image);
        }
        mImageLoader.loadBitmap(imageBean.getDirname() + imageBean.getName(), imageView);
        imageView.setMask(imageBean.islongClick());
    }
}
