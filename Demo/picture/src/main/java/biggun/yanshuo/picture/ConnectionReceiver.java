package biggun.yanshuo.picture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.biggun.yslibrary.Utils.LogUtils;

/**
 * 作者：孙贤武 on 2016/6/29 14:45
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 */
public class ConnectionReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                LogUtils.LogE("当前有网络");
                int networkInfoType = networkInfo.getType();
                if (networkInfoType == connectivityManager.TYPE_MOBILE) {
                    LogUtils.LogE("当前有移动信号");
                } else if (networkInfoType == connectivityManager.TYPE_WIFI) {
                    LogUtils.LogE("当前有网络WFIF信号");
                }
            } else {
                LogUtils.LogE("当前没有网络");
            }
        }
    }
}
