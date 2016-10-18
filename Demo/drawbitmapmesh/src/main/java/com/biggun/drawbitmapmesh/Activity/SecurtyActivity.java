package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.SecurityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class SecurtyActivity extends BaseActivity implements View.OnClickListener
{
    private EditText securityEt;
    private Button encryptbtn;
    private Button decryptbtn;
    private TextView securityTv;
    private SecurityUtils securityUtils;

    private String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1mOEsqL2BgB3cCwXmEIGp2eZS\n" +
            "mTyf2duFCLOfPQtUzWli1QTwUZfgquByunVMMqcWH0nibaCcgiEr1WCqnLZxoGMl\n" +
            "ri71otP01OoQBnzmMpmzHJ0Ifm+1JlkKGg3rsr319eAnJ8Xbb1UAWyLD72nZbEYx\n" +
            "Tz7DY5AytUXWXU8RcQIDAQAB";
    private String preKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALWY4SyovYGAHdwL\n" +
            "BeYQganZ5lKZPJ/Z24UIs589C1TNaWLVBPBRl+Cq4HK6dUwypxYfSeJtoJyCISvV\n" +
            "YKqctnGgYyWuLvWi0/TU6hAGfOYymbMcnQh+b7UmWQoaDeuyvfX14CcnxdtvVQBb\n" +
            "IsPvadlsRjFPPsNjkDK1RdZdTxFxAgMBAAECgYEAhpBRkxk6y+JWvf5BEbj7jBQW\n" +
            "UvCP9teljg/jokTGx8EqrXyyGy4q/+U/SmSWlK8YFxF6W1nm4PKgh+2mouZbXdjg\n" +
            "H/Sja0Kj9UQPbT1OPlwE4W9GuDICD8426rY6lEj4iO93d9xPfLRlIjZmmW1aiVaQ\n" +
            "Of0si6RJL1FaLM/fFVECQQDqChiXu2VDymDSbKpKJ3kw4lle5e/y8TFjzOttItT2\n" +
            "FC37TUo7il2wdiZucsftWInSX8HQl4a3RKx81ckluRDNAkEAxqMPA1caiCCsUQgV\n" +
            "c7y+ZxCa8Ycrj4aJ9TEv0U+tSyBj9mrolaK1B0k3aW7rTYzrNHJ0S4L4VDF8M6di\n" +
            "vzzzNQJAMdWyhOSnXHcEhOoUnLOpD6qzAw0NOfr33FtvKg8Hr9p+LAu6KdF9v6x4\n" +
            "5H3Waoi22DABsiwByJZ78B0JttmgZQJAHibKNJuYL2mmPnaXqwXPcR2YJ2l5N0QY\n" +
            "1NsugvsxNuInylPYEj6sc/qtKpfnp9HoMgHdnUi4IS+RxtV1q8m4sQJALbz6i9ng\n" +
            "FNCW2ICHNDcmoW/aLP1P1+28+uYr4V1qbYCqjwiOFeevFX3UOIiDwKw3nUIi5AZU\n" +
            "K9XFtlKi3WwSHQ==";

    private KeyPair keyPair;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securty);

        securityUtils = SecurityUtils.getInstance();
        keyPair = securityUtils.getKeyPair(1024);
        initToolBar("测试加密解密！");
        collapseToolBar();
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        securityEt = (EditText) findViewById(R.id.security_et);
        encryptbtn = (Button) findViewById(R.id.encryptbtn);
        decryptbtn = (Button) findViewById(R.id.decryptbtn);
        securityTv = (TextView) findViewById(R.id.security_tv);
    }

    @Override
    protected void InitDatas()
    {

    }

    @Override
    protected void InitListeners()
    {
        encryptbtn.setOnClickListener(this);
        decryptbtn.setOnClickListener(this);
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
        String source = securityEt.getText().toString().trim();
        String tv = securityTv.getText().toString().trim();

        switch (v.getId()) {
            case R.id.decryptbtn://解密
                if (TextUtils.isEmpty(tv)) {
                    Toast.makeText(this, "请先输入待加密数据并加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    InputStream is = getAssets().open("pkcs8_rsa_private_key.pem");
                    PrivateKey privateKey = securityUtils.loadPrivateKey(is);
//                    PrivateKey privateKey = securityUtils.loadPrivateKey(preKey);
                    byte[] bytes = securityUtils.deCrypt(Base64.decode(tv, Base64.DEFAULT), privateKey);
                    securityTv.setText(new String(bytes, "utf-8"));
                    securityUtils.printPrivateKeyInfo(privateKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.encryptbtn://加密
                if (TextUtils.isEmpty(source)) {
                    Toast.makeText(this, "请输入待加密数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    InputStream open = getAssets().open("rsa_publickey.pem");
                    PublicKey publicKey = securityUtils.loadPublicKey(open);
//                    PublicKey publicKey = securityUtils.loadPublicKey(pubKey);
                    byte[] bytes = securityUtils.enCrypt(source.getBytes("utf-8"), publicKey);
                    securityTv.setText(Base64.encodeToString(bytes, Base64.DEFAULT));
                    securityUtils.printPublicKeyInfo(publicKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
