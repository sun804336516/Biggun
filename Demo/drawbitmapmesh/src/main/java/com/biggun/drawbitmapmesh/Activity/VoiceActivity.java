package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biggun.drawbitmapmesh.Adapter.VoiceAdapter;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.HorizontextView;
import com.biggun.drawbitmapmesh.View.SmothhRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VoiceActivity extends BaseActivity implements View.OnClickListener
{
    private Button voiceStart;
    private SmothhRecyclerView voiceRcv;
    private List<ResolveInfo> list;
    private VoiceAdapter adapter;
    private HorizontextView htextview;
    private TextView testTv;
    private String text = "文／超低空（简书作者）\n" +
            "原文链接：http://www.jianshu.com/p/326ea728e5d3\n" +
            "著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。\n" +
            "\n" +
            "PackageInstallObserver obs = new PackageInstallObserver();\n" +
            "try {\n" +
            "        mPm.installPackage(Uri.fromFile(new File(apkFilePath)), obs, installFlags,installerPackageName);\n" +
            "        synchronized (obs) \n" +
            "        {\n" +
            "            while (!obs.finished) {\n" +
            "            try {\n" +
            "                   obs.wait();\n" +
            "                 } catch (InterruptedException e) {\n" +
            "                 }\n" +
            "            }\n" +
            "           if (obs.result == PackageManager.INSTALL_SUCCEEDED)\n" +
            "           {\n" +
            "                System.out.println(\"Success\");\n" +
            "           } else\n" +
            "           {\n" +
            "                System.err.println(\"Failure [\"+ installFailureToString(obs.result) + \"]\");\n" +
            "           }\n" +
            "       }\n" +
            "} catch (RemoteException e) {\n" +
            "System.err.println(e.toString());\n" +
            "System.err.println(PM_NOT_RUNNING_ERR);";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        InitViews();
        InitDatas();
        InitListeners();
        initPms();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        useSlide = false;
        return super.onCreateView(parent, name, context, attrs);
    }

    private void initPms()
    {
        PackageManager pm = getPackageManager();
        list = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
    }

    @Override
    protected void InitViews()
    {
        voiceStart = (Button) findViewById(R.id.voice_start);
        voiceRcv = (SmothhRecyclerView) findViewById(R.id.voice_rcv);
        voiceRcv.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        htextview = findView(R.id.voice_horizontaltextview);
        testTv = findTextView(R.id.voice_testTv);
    }

    @Override
    protected void InitDatas()
    {
        htextview.setText(text);
//        testTv.setText(text);
    }

    @Override
    protected void InitListeners()
    {
        voiceStart.setOnClickListener(this);
    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }

    @Override
    public void onClick(View v)
    {
        if (list == null || list.size() == 0) {
            Toast.makeText(this, "没有相关的语音识别软件", Toast.LENGTH_SHORT).show();
            return;
        }
        statVoice();
    }

    private void statVoice()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "现在开始讲话...");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            adapter = new VoiceAdapter(stringArrayListExtra, this, R.layout.voidce_layot);
            voiceRcv.setAdapter(adapter);
        }
    }
}
