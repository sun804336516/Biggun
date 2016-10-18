package biggun.yanshuo.picture.Adpter;

import android.content.Context;
import android.widget.ImageView;

import com.biggun.yslibrary.Base.BIGGUN_Adapter;
import com.biggun.yslibrary.Base.BIGGUN_HolderHandle;

import java.util.List;

import biggun.yanshuo.picture.Bean.ImageDirectory;
import biggun.yanshuo.picture.R;

/**
 * 作者：Administrator on 2016/6/12 15:24
 * 邮箱：sun91985415@163.com
 */
public class ImageDireAdapter extends BIGGUN_Adapter<ImageDirectory>
{
    public ImageDireAdapter(List<ImageDirectory> mList, Context mContext)
    {
        super(mList, mContext);
    }

    @Override
    protected int getViewId(int viewType)
    {
        return R.layout.imagedir_adapter_layout;
    }

    @Override
    protected void Convert(BIGGUN_HolderHandle BIGGUN_HolderHandle, int position, ImageDirectory imageDirectory)
    {
        ImageView imageView = BIGGUN_HolderHandle.getImageView(R.id.imagedir_adapter_layout_img);
        imageView.setImageResource(R.drawable.load_image);
        mImageLoader.loadBitmap(imageDirectory.getRepresentPath(), imageView);
        BIGGUN_HolderHandle.setText(R.id.imagedir_adapter_layout_tv, imageDirectory.getName());
    }
}
