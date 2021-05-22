package com.sh.simpleeeg;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import androidx.constraintlayout.widget.Guideline;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.Map;

public class Test extends Activity {

//我是註解!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static boolean mbIsInitialised;
    // 用來記錄是否MediaPlayer物件需要執行prepareAsync()

    TextView txtList,txtData,tvAttention,tvMeditation;
    private static final String DB_FILE = "sample.db", DB_FILE2 = "sample2.db",
            DB_TABLE = "sample", DB_TABLE2 = "sample2" , DB_TABLE_ID = "sample3";
    private DBHelper mySampleDbOpenHelper,mySampleDbOpenHelper2,searchID;
    private LinearLayout mLinLay;
    private ImageView imgGhost;
    RadioGroup rgSaveMethod;
    RadioButton rbSQLite, rbExcel, rbCloud;


    public static int TestTime = 0;
    CLS_PARAM S = new CLS_PARAM();
    CLS_BrainWave clsBrainwave = new CLS_BrainWave();
    CLS_DATA clsData = new CLS_DATA();
    Music music = new Music();
    CLS_LineChart clsLineChart;

    Context mContext;
    LinearLayout layoutChart,LCFatigue;
    View viewLineChart,viewLineChart2;
    ImageView ivBG,ivStopTest;
    ImageView ivStartTest;
    Guideline guidelineTop,guidelineBottom;


    String sheetboo = "true";



    private ClockThread1000 m_clockThread1000;
    private Handler m_clockHandler = new Handler();
    boolean bThreadRun = false;
    int iCountDownTimer = 100;
    RequestOptions myGdiOptions = new RequestOptions().fitCenter();

    static double dX = 0;

    @SuppressLint({"SourceLockedOrientationActivity", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.test);



        ivStartTest = findViewById(R.id.ivStartTest);
        rbSQLite = findViewById(R.id.rbSQLite);
        rbExcel = findViewById(R.id.rbExcel);
        rbCloud = findViewById(R.id.rbCloud);

        mContext = this;



        ivBG = (ImageView)findViewById(R.id.ivBG);
        ivStopTest = (ImageView)findViewById(R.id.ivStopTest);
        layoutChart = (LinearLayout)findViewById(R.id.layoutChart);
        LCFatigue =(LinearLayout)findViewById(R.id.LCFatigue);
        tvAttention = (TextView) findViewById(R.id.tvAttention);
        tvMeditation = (TextView) findViewById(R.id.tvMeditation);
        guidelineTop = findViewById(R.id.guideline23);
        guidelineBottom = findViewById(R.id.guideline24);
        imgGhost = (ImageView)findViewById(R.id.imgGhost);



//建立資料表-----------------------------------------------------------------------------------------



        mContext = this;


        txtList = (TextView) findViewById(R.id.txtList);
        txtData = (TextView) findViewById(R.id.txtData);
        //chartPreMed = (PieChart) findViewById(R.id.pieChartMed);


        float fSize = 10f;




        clsData.DoRawCalculation();





        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                //Bitmap bmpScreenshot = clsData.TakeScreenShotFromView(layout0);
                //clsData.CreatePdf(context, bmpScreenshot);
            }
        }, 2000);
        //-----------------------------------------------------------------------------------------







        m_clockThread1000 = new ClockThread1000();
        m_clockHandler.post(m_clockThread1000);
        bThreadRun = true;

        clsData.ClearRawData();

        SetCallback();

    }
    @Override
    protected void onStart()
    {
        super.onStart();

        Glide.with(mContext).load(R.drawable.bg_test).apply(myGdiOptions).into(ivBG);
        Glide.with(mContext).load(R.drawable.stop_test).apply(myGdiOptions).into(ivStopTest);

        clsData.SetTestReady(true);
        dX = 0;
        clsLineChart = new CLS_LineChart(3);
        viewLineChart = clsLineChart.viewDrawAppearance(this, true, 30, 100, Color.RED, Color.GREEN);
        viewLineChart2 = clsLineChart.viewDrawAppearance2(this, true, 30, 10,Color.YELLOW,Color.BLUE);
        layoutChart.addView(viewLineChart,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT));
        LCFatigue.addView(viewLineChart2,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT));
        clsLineChart.AddPoint(1, 0, 0);//要有資料才有辦法調整大小
        clsLineChart.AddPoint(2, 0, 0);
        clsLineChart.AddPoint(3, 0, 0);




    }
    //動畫==================================================================

    @Override
    protected void onResume() {
        super.onResume();
        music.play(this, R.raw.classic04);

    }
    @Override
    protected void onPause() {
        super.onPause();
        music.stop(this);
    }

    @Override
    public void finish() {
        music.stop(this);
        super.finish();
    }

    //===========================================================================

    @Override
    protected void onStop()
    {
        super.onStop();

        Glide.with(this).clear(ivBG);
        Glide.with(this).clear(ivStopTest);

        bThreadRun = false;
        clsLineChart = null;
        clsData.SetTestReady(false);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        m_clockHandler.removeCallbacks(m_clockThread1000);
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
    }
    @Override
    public void onBackPressed() //不讓user按螢幕左下方的 "退回上一層"
    {
        Intent intent = new Intent();
        intent.setClass(this, Main.class);//input info
        startActivity(intent);
    }
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    public void SetCallback()
    {
        clsBrainwave.SetCallback_BWTest(new CLS_BrainWave.Brainwave_Callback_Test()
        {
            //@Override
            public void Do(int iCmd, int iVal)
            {
                switch(iCmd)
                {
                    case 0:
                        //System.out.println("work");
                        //ShowToast("!! data callback ok !!");
                        break;
                }
                //
                if(iCmd == S.BrainwaveValue)
                {
                    SendProcessMessage(S.BrainwaveValue);
                }
            }
        });
    }
    //==============================================================================================
    void StopTest()
    {
        Bundle bundle = getIntent().getExtras();
        String CheckRB = bundle.getString("CheckRB");
        float fCheckRB = Float.parseFloat(CheckRB);

        if (fCheckRB == 1){
            Main.addSql();
            Toast.makeText(Test.this, "資料已存至SQLite中", Toast.LENGTH_LONG).show();
        }
        if (fCheckRB == 2){
            clsData.WriteXLS(mContext);
            Toast.makeText(Test.this, "資料已存至Excel中", Toast.LENGTH_LONG).show();
        }
        if (fCheckRB == 3){
            Toast.makeText(Test.this, "資料已存至Google Sheets中", Toast.LENGTH_LONG).show();
        }

        try
        {
            bThreadRun = false;

            Intent intent = new Intent();
            intent.setClass(this, Report.class);
            startActivity(intent);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage().toString());
        }
    }
    //==============================================================================================
    public void ivStopTest_OnClick(View view) {
        StopTest();
    }

    //==============================================================================================
    private class ClockThread1000 extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                if (!bThreadRun)
                    return;
                m_clockHandler.postDelayed(m_clockThread1000, 1000);
                SendProcessMessage(1000);
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage().toString());
            }
        }
    }
    //==============================================================================================
    void SendProcessMessage(int iMsg)
    {
        Message _message = new Message();
        _message.what = iMsg;
        m_processMessageHandler.sendMessage(_message);
    }

    Handler m_processMessageHandler = new Handler()
    {

        float set1=1500;
        float set2=1500;


        @SuppressLint({"HandlerLeak", "ResourceType"})
        @Override
        public void handleMessage(Message msg)
        {
            //imgGhost.setImageResource(R.drawable.);   //------圖片交替位置[1]------
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 1:
                    try
                    {

                    }
                    catch (Exception ex)
                    {
                        System.out.println(ex.getMessage().toString());
                    }
                    break;
                case 1000:
                    //textViewCountDown.setText(String.valueOf(iCountDownTimer));
                    /*
                    iCountDownTimer--;
                    if(iCountDownTimer < 0)
                        StopTest();
                    */
                    break;
            }
            //
            if(msg.what == S.BrainwaveValue)
            {
                //imgGhost.setImageResource(R.drawable.);   //------圖片交替位置[2]------
                Bundle bundle = getIntent().getExtras();
                String CheckRB = bundle.getString("CheckRB");
                float fCheckRB = Float.parseFloat(CheckRB);

//動畫========================================================================================================
                float point1=set1, point2=set2;
                   //-----圖片重製-----
                ObjectAnimator animTxtAlpha = ObjectAnimator.ofFloat(imgGhost, "alpha", 1, (float) 0.3);
                animTxtAlpha.setDuration(500);
                animTxtAlpha.setRepeatCount(-1);
                animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                animTxtAlpha.setInterpolator(new LinearInterpolator());
                animTxtAlpha.start();

                ObjectAnimator animTxtMove1 =
                        ObjectAnimator.ofFloat(imgGhost, "y", point1, point2);
                animTxtMove1.setDuration(900);
                animTxtMove1.setInterpolator(new AccelerateDecelerateInterpolator());

                AnimatorSet animTxtMove = new AnimatorSet();
                animTxtMove.playSequentially(animTxtMove1);
                animTxtMove.start();
                //=============================================================================

                if (clsLineChart == null)
                    return;
                //專注值===============
                clsLineChart.AddPoint(1, dX, clsData.iGetAttention());
                tvAttention.setText(String.valueOf(clsData.iGetAttention()));

                set1 = set2;
                if(clsData.iGetAttention()<=30){    //-----關卡難度-----
                    set2 = 700;
                }
                else{
                    set2 = 700+clsData.iGetAttention()*9;
                }


                //放鬆值==============
                clsLineChart.AddPoint(2, dX, clsData.iGetMeditation());
                tvMeditation.setText(String.valueOf(clsData.iGetMeditation()));

                clsLineChart.AddPoint(3, dX, clsData.iGetFatigue());
                if (fCheckRB == 1){
                    Main.addSql2();
                }
                if (fCheckRB == 2){
                }
                if (fCheckRB == 3){
                    new addDataToSheet().execute("https://script.google.com/macros/s/AKfycbyQ0RLFNohoVVE4O4T_eROfEp0evCgdO3hy4ojKeHrkOQ4tDjyK76SE5b9g9O6tXhC1/exec");
                }
                viewLineChart.invalidate();
                viewLineChart2.invalidate();
                dX += 1;

            }
        }
    };


    //==============================================================================================
//使用非同步方式上傳雲端保持主畫面持續進行
    private class addDataToSheet extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //執行中 在背景做事情
            for (String urlStr : params) {
                try {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlStr,
                            new Response.Listener<String>() {   //跟網址上的Http提出請求
                                @Override
                                public void onResponse(String response) {
                                    //因多次上傳，如每次都回應可能會吃太多資源，所以是空的
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {    //錯誤時做
                                    Log.d("Error", String.valueOf(error));
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {         //取得參數
                            Map<String, String> parmas = new HashMap<>();
                            //傳遞參數至試算表上的SCRIPT檔
                            parmas.put("action", "addRec");
                            //建新SHEET
                            parmas.put("newSheet",sheetboo);
                            //以下為各種參數上傳
                            parmas.put("attention", String.valueOf(clsData.iGetAttention()));
                            parmas.put("meditation", String.valueOf(clsData.iGetMeditation()));
                            parmas.put("fatigue", String.valueOf(clsData.iGetFatigue()));
                            parmas.put("delta", String.valueOf(clsData.iGetDelta()));
                            parmas.put("theta", String.valueOf(clsData.iGetTheta()));
                            parmas.put("lowAlpha", String.valueOf(clsData.iGetLowAlpha()));
                            parmas.put("highAlpha", String.valueOf(clsData.iGetHighAlpha()));
                            parmas.put("lowBeta", String.valueOf(clsData.iGetLowBeta()));
                            parmas.put("highBeta", String.valueOf(clsData.iGetHighBeta()));
                            parmas.put("lowGamma", String.valueOf(clsData.iGetLowGamma()));
                            parmas.put("highGamma", String.valueOf(clsData.iGetHighGamma()));
                            return parmas;
                        }

                    };
                    //下面能保證資料不會遺失但會使資料表Rec編號亂掉
                    int socketTimeOut = 50000;// u can change this .. here it is 50 seconds //當回傳時間超過50秒會重新跟HTTP提出請求
                    RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(retryPolicy);
                    RequestQueue queue = Volley.newRequestQueue(Test.this);
                    queue.add(stringRequest);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}

