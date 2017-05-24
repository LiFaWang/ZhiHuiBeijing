package com.it.cn.zhihuibeijing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 新闻页面
 * Created by Administrator on 2017/2/16.
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.ll_control)
    private LinearLayout llControl;
    @ViewInject(R.id.btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.btn_textsize)
    private ImageButton btnTextSize;
    @ViewInject(R.id.btn_share)
    private ImageButton btnShare;
    @ViewInject(R.id.btn_menu)
    private ImageButton btnMenu;
    @ViewInject(R.id.wv_news_detail)
    private WebView mWebView;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;
    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);
        llControl.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        mUrl = getIntent().getStringExtra("url");
        mWebView.loadUrl(mUrl);
        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);//显示缩放点击按钮
        settings.setUseWideViewPort(true);//支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true);//支持js功能(wap网页不支持)
        mWebView.setWebViewClient(new WebViewClient(){
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("开始加载网页了");
                pbLoading.setVisibility(View.VISIBLE);
            }
            //网页加载结束

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.INVISIBLE);
                System.out.println("结束加载网页了");


            }
//            所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(request.toString());
                System.out.println("url"+request.toString());
//                return super.shouldOverrideUrlLoading(view, request);
                return true;
            }
        });
//        mWebView.goBack();
//        mWebView.goForward();
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //进度发生变化
                System.out.println("进度变化:"+newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //网页标题
                System.out.println("网页标题"+title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        System.out.println("456");
        switch (v.getId()) {
            case R.id.btn_back:
                finish();

                break;
            case R.id.btn_textsize:
                //显示选择弹窗
               showChooseDialog();

                break;
            case R.id.btn_share:

                break;

            default:
                break;
        }
    }
    private int mTempWhich;//记录临时的字体大小（点击确定之前）
    private int mCurrentWhich=2;//记录当前选中的字体（点击确定之后）

    /**
     * 展示字体大小
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        String[] items= new String[]{"超大号字体","大号字体","正常字体","小号" +
                "字体","超小号字体"};
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich=which;

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWebView.getSettings();

                switch (mTempWhich) {
                    case 0://超大字体
                        settings.setTextSize(WebSettings.TextSize.LARGEST);


                        break;
                    case 1://大字体
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2://正常字体
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3://小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4://超小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }
//                mTempWhich=mCurrentWhich;
               mCurrentWhich=mTempWhich;

            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();

    }
}
