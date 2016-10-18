package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.biggun.drawbitmapmesh.Adapter.BaseAdapter;
import com.biggun.drawbitmapmesh.Adapter.HolderHandle;
import com.biggun.drawbitmapmesh.Adapter.InitAdapter;
import com.biggun.drawbitmapmesh.Adapter.initDecoration;
import com.biggun.drawbitmapmesh.Bean.BrowserBean;
import com.biggun.drawbitmapmesh.Bean.InitBean;
import com.biggun.drawbitmapmesh.Bean.TestDatabaseBean;
import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Receiver.LockReceiver;
import com.biggun.drawbitmapmesh.Util.DbUtils2;
import com.biggun.drawbitmapmesh.Util.MyFileObserver;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.OvalBezierView;
import com.biggun.drawbitmapmesh.View.SmothhRecyclerView;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class InitActivity extends BaseActivity implements TencentLocationListener
{
    private SmothhRecyclerView initRecyclerview;
    private InitAdapter adapter;
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private Camera camera;
    private Camera.Parameters parameters;
    private OvalBezierView mOvalBezierView;
    //    DBUtils dbUtils;
    DbUtils2 dbUtils2;
    private ArrayList<BrowserBean> list = new ArrayList<>();

    private MyFileObserver myFileObserver;
    private String monitoredString = "";
    private Semaphore semaphore;//java 信号量

    private List<InitBean> initList = new ArrayList<>();
    private AlertDialog mAlertDialog;


    TencentLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        initToolBar("王大枪");
        collapseToolBar();
        InitViews();
        InitDatas();
        InitListeners();
//        getLocation();

        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(Common.ANIMATORTIME_10000);
        request.setAllowCache(true);
        request.setQQ("804336516");
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        locationManager = TencentLocationManager.getInstance(this);
        locationManager.requestLocationUpdates(request, this);
//        View view;
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        Bitmap cache = view.getDrawingCache();
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("http://www.baidu.com"));
//        startActivity(intent);
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        useSlide = false;
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_init, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Toast.makeText(this, "OnHome", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void InitViews()
    {
        initRecyclerview = findView(R.id.initrecyclerview);

        initRecyclerview.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        initRecyclerview.addItemDecoration(new initDecoration(getResources().getDrawable(R.drawable.initdrcoration)));

        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, LockReceiver.class);
        mOvalBezierView = findView(R.id.init_oval_bezier);
        mOvalBezierView.setStart(false);
    }

    @Override
    protected void InitDatas()
    {

//        initList.add(new InitBean("ListView的Adapter跟RecyclerView的Adapter", TestListViewActivity.class));
        initList.add(new InitBean("加密解密", SecurtyActivity.class));
        initList.add(new InitBean("Vp_Rcv", ViewPagerAndRecyclerViewActivity.class));
        initList.add(new InitBean("测试闫慧敏的问题", YanhuimingTestActivity.class));
        initList.add(new InitBean("JS", JSActivity.class));
        initList.add(new InitBean("Clip", ClipImgActivity.class));
        initList.add(new InitBean("Static", LoadeTestActivity.class));
        initList.add(new InitBean("贝塞尔曲线", BezierActivity.class));
        initList.add(new InitBean("翻译", TranslateActivity.class));
        initList.add(new InitBean("应用流量统计", InstalledActivity.class));
        initList.add(new InitBean("语音识别", VoiceActivity.class));
        initList.add(new InitBean("图片浏览", ImageBrowserActivity.class));
        initList.add(new InitBean("测试DbUtils", null));
        initList.add(new InitBean("测试截屏", null));
        initList.add(new InitBean("测试EdgeViewPager", EdgeActivity.class));
        initList.add(new InitBean("测试DrawMesh", MeshViewActivity.class));
        initList.add(new InitBean("测试ClipRect", ClipRectActivity.class));
        initList.add(new InitBean("测试锁屏", null));
        initList.add(new InitBean("测试手电筒", null));
//        initList.add(new InitBean("测试扫描二维码", CaptureActivity.class));
        initList.add(new InitBean("测试卡片集合", CardActivity.class));
        initList.add(new InitBean("测试静默安装和无障碍服务", SilentinstallActivity.class));
        initList.add(new InitBean("测试sd卡浏览", SdcardBrowserActivity.class));
        initList.add(new InitBean("测试画图Canvas", CanvasActivity.class));
        initList.add(new InitBean("测试ScrollLayout", ScrollLayoutActivity.class));
        initList.add(new InitBean("测试一些动画", CameraAniActivity.class));
        initList.add(new InitBean("测试Behavior", BehaviorActivty.class));
        initList.add(new InitBean("测试BottomSheets", BottomSheetsActivity.class));
        initList.add(new InitBean("测试VectorDrawable", VectorDrawableActivity.class));

        Utils.LogE("FastJson:" + JSON.toJSONString(initList));
        Utils.LogE("FastJson:" + JSON.toJSONString(new InitBean("王大枪", EdgeActivity.class)));

        adapter = new InitAdapter(initList, this, R.layout.init_recycler_layout);
        initRecyclerview.setAdapter(adapter);

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        boolean txt = mimeTypeMap.hasExtension("txt");
        boolean type = mimeTypeMap.hasMimeType("text/plain");
        Utils.LogE("txt:" + txt + "---type:" + type);

//        Utils.getClassmode(new BrowserBean("biggun", 1111, false));
//        dbUtils = DBUtils.getInstance(this);
        dbUtils2 = new DbUtils2(this);

        monitoredString = "/data/data/" + getPackageName();
        monitoredString = Environment.getExternalStorageDirectory() + "/新建文件夹";
        myFileObserver = new MyFileObserver(monitoredString);
        myFileObserver.startWatching();

        int pixelSize = getResources().getDimensionPixelSize(R.dimen.test);
        float density = getResources().getDisplayMetrics().density;
        int size = (int) (180 * density + 0.5);
        Utils.LogE("pixelSize:" + pixelSize + ",size:" + size);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Utils.LogE("onNewIntent");
    }

    @Override
    protected void InitListeners()
    {
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClick()
        {
            @Override
            public void onItemCkick(HolderHandle holderHandle, int position)
            {
                Class cls = initList.get(position).getCls();
                String name = initList.get(position).getName();
                if (cls != null) {
//                    ActivityManager.getInstance().startActivity(cls);
                    startActivity(cls);
                } else {
                    if (name.equals("测试截屏")) {
//                        Utils.screenShot("/storage/emulated/0/biggun.jpg");
                    } else if (name.equals("测试DbUtils")) {
                        add2Table();
                    } else if (name.equals("测试锁屏")) {
                        lockScreen();
                    } else if (name.equals("Actio_Pick")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 100);
                    } else if (name.equals("测试手电筒")) {
                        if (camera == null) {
                            camera = camera.open();
                        }
                        if (parameters != null && parameters.getFlashMode() == Camera.Parameters.FLASH_MODE_TORCH) {
                            closeLight();
                        } else {
                            openLight();
                        }
                    }
                }
            }
        });
        initRecyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });
        initRecyclerview.setRecyclerViewDownDis(initRecyclerview.new RecyclerViewDown()
        {
            @Override
            protected void down(float y, float height)
            {
                Utils.LogE(height + "RecyclerView：" + y);
                mOvalBezierView.seekTo(Math.abs(y) / height);
            }
        });
    }

    private void add2Table()
    {
//        dbUtils.addTAble(new TestDatabaseBean("王小枪", "朝阳区崔各庄"));
//                dbUtils.addTAble(new BrowserBean("刘新东",1223231,false));

        dbUtils2.addTAble(new TestDatabaseBean("王小枪", "朝阳区崔各庄"));
        dbUtils2.addTAble(new BrowserBean("刘新东", 1223231, false));
        try {
//                    dbUtils.readDataBase(new BrowserBean(),list);
            dbUtils2.readDataBase(new BrowserBean(), list);
            Utils.LogE(list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_add:
                initRecyclerview.smooth2position(3, getToolbarSize());
                Toast.makeText(InitActivity.this, "ADD", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_del:
                Toast.makeText(InitActivity.this, "DEL", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_xixi:
                Toast.makeText(InitActivity.this, "XIXI", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_sxw:
                Toast.makeText(InitActivity.this, "SXW", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_biggun:
                Toast.makeText(InitActivity.this, "BIGGUN", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_wdq:
                Toast.makeText(InitActivity.this, "WDQ", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home://监听无效   需要早onOptionsitemSelected中监听
                Toast.makeText(InitActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }


    private void openLight()
    {
        camera.startPreview();
        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }

    private void closeLight()
    {
        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case 0:
                if (data != null) {
//                    Bitmap bit = data.getParcelableExtra(QR_UTILS.RETURN_BITMAP);
//                    String str = data.getStringExtra(QR_UTILS.RETURN_CONTENT);
//
//                    ImageView img = new ImageView(this);
//                    img.setImageBitmap(bit);
//                    TextView tv = new TextView(this);
//                    tv.setText(str);
                }
                break;
            case 200:
                if (resultCode == Activity.RESULT_OK) {
                    policyManager.lockNow();
                    finish();
                } else {
                    getLockPermission();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 锁屏
     */
    private void lockScreen()
    {
        if (policyManager == null) {
            return;
        }
        if (policyManager.isAdminActive(componentName)) {
            policyManager.lockNow();
            finish();
        } else {
            getLockPermission();
        }
    }

    /**
     * 激活设备管理器
     */
    public void getLockPermission()
    {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活才能使用锁屏");
        startActivityForResult(intent, 200);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s)
    {
        Utils.LogE("location:" + tencentLocation);
        Utils.LogE("-location-error--" + i + "--reason--" + s);
        if (i == TencentLocation.ERROR_OK) {
            locationManager.removeUpdates(this);

        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1)
    {
        Utils.LogE("=====" + s + "====" + i + "===" + s1);
    }


    public enum TestEnum
    {
        sunxianwu, biggun;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        myFileObserver.stopWatching();
//        if (dbUtils != null) {
//            dbUtils.close();
//        }
        if (dbUtils2 != null) {
            dbUtils2.close();
        }
//        android.os.Process.killProcess(Process.myPid());
//        ActivityManager.getInstance().ExitAll();
        locationManager.removeUpdates(this);
    }

    private void getLocation()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
        if (cellLocation == null) {
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
        } else {
            String networkOperator = tm.getNetworkOperator();
            int cid = cellLocation.getCid();
            int lac = cellLocation.getLac();
            Utils.LogE("location:" + networkOperator + "--" + cid + "--" + lac);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
