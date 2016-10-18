package com.biggun.drawbitmapmesh.Service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.biggun.drawbitmapmesh.Util.Utils;

import java.util.HashMap;

/**o
 * Created by 孙贤武 on 2016/1/13.
 *
 * 静默安装的Service
 */
public class MyAccessService extends AccessibilityService
{
    HashMap<Integer, Boolean> map = new HashMap<>();

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            int eventType = event.getEventType();
            Utils.LogE("eventType:" + eventType);
            if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                    eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                Utils.LogE("===================================");
                if (map.get(event.getWindowId()) == null) {
                    boolean handled = handleNodeInfo(nodeInfo);
                    if (handled) {
                        map.put(event.getWindowId(), true);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private boolean handleNodeInfo(AccessibilityNodeInfo nodeInfo)
    {
        if (nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();

            CharSequence className = nodeInfo.getClassName();
            Utils.LogE("className:" + className);
            if (className.equals("android.widget.Button")) {
                String string = nodeInfo.getText().toString();
                Utils.LogE("智能安装服务：" + string);
                if (isok(string)) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            } else if (className.equals("android.widget.ScrollView")) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo info = nodeInfo.getChild(i);
                if (handleNodeInfo(info)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isok(String string)
    {
        return string.equals("确定") || string.equals("安装") || string.equals("完成");
    }

    //"application/vnd.android.package-archive"
    @Override
    public void onInterrupt()
    {

    }
}
