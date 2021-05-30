package com.sh.simpleeeg;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Guideline;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.sh.simpleeeg.CLS_BrainWave.iAttention;
import static com.sh.simpleeeg.CLS_BrainWave.iDelta;
import static com.sh.simpleeeg.CLS_BrainWave.iFatigue;
import static com.sh.simpleeeg.CLS_BrainWave.iGoodSignal;
import static com.sh.simpleeeg.CLS_BrainWave.iHighAlpha;
import static com.sh.simpleeeg.CLS_BrainWave.iHighBeta;
import static com.sh.simpleeeg.CLS_BrainWave.iHighGamma;
import static com.sh.simpleeeg.CLS_BrainWave.iLowAlpha;
import static com.sh.simpleeeg.CLS_BrainWave.iLowBeta;
import static com.sh.simpleeeg.CLS_BrainWave.iLowGamma;
import static com.sh.simpleeeg.CLS_BrainWave.iMeditation;
import static com.sh.simpleeeg.CLS_BrainWave.iTheta;

//import com.bumptech.glide.request.Request;

public class Test extends Activity {


    public static boolean mbIsInitialised;
    // 用來記錄是否MediaPlayer物件需要執行prepareAsync()

    TextView txtList,txtData,tvAttention,tvMeditation,tvTime;
    private static final String DB_FILE = "sample.db", DB_FILE2 = "sample2.db",
            DB_TABLE = "sample", DB_TABLE2 = "sample2" , DB_TABLE_ID = "sample3";
    private DBHelper mySampleDbOpenHelper,mySampleDbOpenHelper2,searchID;
    private LinearLayout mLinLay;
    private ImageView imgGhost,imgAudience;
    private Dialog mDlgNext;
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
    static int progress = 11;//倒數計時及間
    private ProgressBar pb;
    private Timer timer;
    private TimerTask timerTask;
    static int level = 1;
    RequestOptions myGdiOptions = new RequestOptions().fitCenter();


    static double dX = 0;




    @RequiresApi(api = Build.VERSION_CODES.P)
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


        ivBG = (ImageView) findViewById(R.id.ivBG);
        ivStopTest = (ImageView) findViewById(R.id.ivStopTest);
        layoutChart = (LinearLayout) findViewById(R.id.layoutChart);
        LCFatigue = (LinearLayout) findViewById(R.id.LCFatigue);
        tvAttention = (TextView) findViewById(R.id.tvAttention);
        tvMeditation = (TextView) findViewById(R.id.tvMeditation);
        guidelineTop = findViewById(R.id.guideline23);
        guidelineBottom = findViewById(R.id.guideline24);
        imgGhost = (ImageView) findViewById(R.id.imgGhost);
        imgAudience = (ImageView) findViewById(R.id.imgAudience);

//建立資料表-----------------------------------------------------------------------------------------


        mContext = this;


        txtList = (TextView) findViewById(R.id.txtList);
        txtData = (TextView) findViewById(R.id.txtData);
        tvTime = (TextView) findViewById(R.id.tvTime);
        //chartPreMed = (PieChart) findViewById(R.id.pieChartMed);
        pb = (ProgressBar) findViewById(R.id.progressBar);


        float fSize = 10f;

        //設置進度條的最大數值
        pb.setMax(10);
        pb.setProgress(0);



        clsData.DoRawCalculation();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                }

            }, 1000);



        //-----------------------------------------------------------------------------------------


        m_clockThread1000 = new ClockThread1000();
        m_clockHandler.post(m_clockThread1000);
        bThreadRun = true;

        clsData.ClearRawData();

        SetCallback();

    }



    void setMusic(){
        switch (level) {
            case 2: //等級2=====
                music.play(this, R.raw.calm01);
                break;
            case 3: //等級3=====
                music.play(this, R.raw.passionate01);
                break;
            case 4: //等級4=====
                music.play(this, R.raw.pop01);
                break;
        }
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
        StartTimer();
        music.play(this, R.raw.classic04);

    }
    @Override
    protected void onPause() {
        super.onPause();
        level=1;
        EndTimer();
        music.stop(this);

    }

    @Override
    public void finish() {
        music.stop(this);
        level=1;
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
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0)
            {
                tvTime.setText(Integer.toString(progress));
            }
            if(msg.what==1)
            {
                tvTime.setText("時間到");
            }
        };
    };
    Handler handler2 = new Handler()
    {
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0)
            {
                mDlgNext = new Dialog(Test.this);
                mDlgNext.setCancelable(false);
                mDlgNext.setContentView(R.layout.dlg_next);
                Button loginBtnNext = mDlgNext.findViewById(R.id.btnNext);
                loginBtnNext.setOnClickListener(loginDlgBtnNextOnClick);
                mDlgNext.show();
            }
        };
    };


    public void StartTimer() {
        //如果timer和timerTask已經被置null了
        if (timer == null&&timerTask==null) {
            //新建timer和timerTask
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Bundle bundle = getIntent().getExtras();
                    String CheckRB = bundle.getString("CheckRB");
                    float fCheckRB = Float.parseFloat(CheckRB);
                    //每次progress減一
                    progress--;
                    handler.sendEmptyMessage(0);
                    if (progress <= 0) {
                        handler.sendEmptyMessage(1);
                        clsData.SetLD = "True";
                        clsData.SetBrainData(iGoodSignal, iAttention=-1, iMeditation=-1,iFatigue=-1, iDelta=-1, iTheta=-1,
                                iLowAlpha=-1, iHighAlpha=-1, iLowBeta=-1, iHighBeta=-1, iLowGamma=-1, iHighGamma=-1);
                        switch((int) fCheckRB){
                            case 1 : Main.addSql2();break;
                            case 2 : break;//CLS_DATA(Ctrl+F => LoadingData);
                            case 3 : AddDataToSheets();break;
                        }
                        if(progress==0) {
                            if(level<4)
                            handler2.sendEmptyMessage(0);

                        }
                    }
                    else {
                        clsData.SetLD = "False";
                        clsData.SetBrainData(iGoodSignal, iAttention, iMeditation,iFatigue, iDelta, iTheta,
                                iLowAlpha, iHighAlpha, iLowBeta, iHighBeta, iLowGamma, iHighGamma);
                        switch ((int) fCheckRB){
                            case 1 : Main.addSql2();break;
                            case 2 : break;//CLS_DATA(Ctrl+F => LoadingData);
                            case 3 : AddDataToSheets();break;
                        }


                    }
                    //設置進度條進度
                    pb.setProgress(progress);
                }
            };
            /*开始执行timer,第一个参数是要执行的任务，
            第二个参数是开始的延迟时间（单位毫秒）或者是Date类型的日期，代表开始执行时的系统时间
            第三个参数是计时器两次计时之间的间隔（单位毫秒）*/
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    private void AddDataToSheets() {
        new addDataToSheet().execute("https://script.google.com/macros/s/AKfycbyQ0RLFNohoVVE4O4T_eROfEp0evCgdO3hy4ojKeHrkOQ4tDjyK76SE5b9g9O6tXhC1/exec");
    }


    public void EndTimer()
    {
        timer.cancel();
        timerTask.cancel();
        timer=null;
        timerTask=null;

    }



    private final View.OnClickListener loginDlgBtnNextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(Test.this, "下一關", Toast.LENGTH_LONG).show();
            progress = 11;
            level+=1;
            setMusic();
            mDlgNext.dismiss();

        }
    };
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

        float set1=1600;
        float set2=1600;


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
                AnimatorSet animTxtMove = new AnimatorSet();
                float point1=set1, point2=set2;
                ObjectAnimator animTxtAlpha;
                ObjectAnimator animTxtAlpha2;

//動畫========================================================================================================
                switch (level) {
                    case 1 : //等級1=====
                        //-----圖片重製-----
                        animTxtAlpha = ObjectAnimator.ofFloat(imgGhost, "alpha", 1, (float)0.3);
                        animTxtAlpha2 = ObjectAnimator.ofFloat(imgAudience, "alpha", 0, 0);
                        animTxtAlpha.setDuration(500);
                        animTxtAlpha.setRepeatCount(-1);
                        animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha.setInterpolator(new LinearInterpolator());
                        animTxtAlpha2.setDuration(500);
                        animTxtAlpha2.setRepeatCount(-1);
                        animTxtAlpha2.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha2.setInterpolator(new LinearInterpolator());
                        animTxtAlpha.start();
                        animTxtAlpha2.start();


                        ObjectAnimator animTxtMove1 =
                                ObjectAnimator.ofFloat(imgGhost, "y", point1, point2);
                        animTxtMove1.setDuration(900);
                        animTxtMove1.setInterpolator(new AccelerateDecelerateInterpolator());


                        animTxtMove.playSequentially(animTxtMove1);
                        animTxtMove.start();
                        break;
                    case 2 :  //等級2=====
                        animTxtAlpha = ObjectAnimator.ofFloat(imgGhost, "alpha",0,0);
                        animTxtAlpha2 = ObjectAnimator.ofFloat(imgAudience, "alpha", 1, 1);
                        animTxtAlpha.setDuration(500);
                        animTxtAlpha.setRepeatCount(-1);
                        animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha.setInterpolator(new LinearInterpolator());
                        animTxtAlpha2.setDuration(500);
                        animTxtAlpha2.setRepeatCount(-1);
                        animTxtAlpha2.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha2.setInterpolator(new LinearInterpolator());
                        animTxtAlpha.start();
                        animTxtAlpha2.start();

                        ObjectAnimator animTxtMove2 =
                                ObjectAnimator.ofFloat(imgAudience, "y", point1, point2);
                        animTxtMove2.setDuration(900);
                        animTxtMove2.setInterpolator(new AccelerateDecelerateInterpolator());

                        animTxtMove.playSequentially(animTxtMove2);
                        animTxtMove.start();
                        break;
                    case 3 :  //等級3=====
                        animTxtAlpha = ObjectAnimator.ofFloat(imgGhost, "alpha",0,0);
                        animTxtAlpha2 = ObjectAnimator.ofFloat(imgAudience, "alpha", 1, 1);
                        animTxtAlpha.setDuration(500);
                        animTxtAlpha.setRepeatCount(-1);
                        animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha.setInterpolator(new LinearInterpolator());
                        animTxtAlpha2.setDuration(500);
                        animTxtAlpha2.setRepeatCount(-1);
                        animTxtAlpha2.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha2.setInterpolator(new LinearInterpolator());
                        animTxtAlpha.start();
                        animTxtAlpha2.start();

                        ObjectAnimator animTxtMove3 =
                                ObjectAnimator.ofFloat(imgAudience, "y", point1, point2);
                        animTxtMove3.setDuration(900);
                        animTxtMove3.setInterpolator(new AccelerateDecelerateInterpolator());

                        animTxtMove.playSequentially(animTxtMove3);
                        animTxtMove.start();
                        break;
                    case 4 :  //等級4=====
                        animTxtAlpha = ObjectAnimator.ofFloat(imgGhost, "alpha",0,0);
                        animTxtAlpha2 = ObjectAnimator.ofFloat(imgAudience, "alpha", 1, 1);
                        animTxtAlpha.setDuration(500);
                        animTxtAlpha.setRepeatCount(-1);
                        animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha.setInterpolator(new LinearInterpolator());
                        animTxtAlpha2.setDuration(500);
                        animTxtAlpha2.setRepeatCount(-1);
                        animTxtAlpha2.setRepeatMode(ObjectAnimator.REVERSE);
                        animTxtAlpha2.setInterpolator(new LinearInterpolator());
                        animTxtAlpha.start();
                        animTxtAlpha2.start();

                        ObjectAnimator animTxtMove4 =
                                ObjectAnimator.ofFloat(imgAudience, "y", point1, point2);
                        animTxtMove4.setDuration(900);
                        animTxtMove4.setInterpolator(new AccelerateDecelerateInterpolator());

                        animTxtMove.playSequentially(animTxtMove4);
                        animTxtMove.start();
                        break;
                }
                //=============================================================================

                if (clsLineChart == null)
                    return;

                set1 = set2;  //使動畫較順

                //=======關卡難度=======
                switch (level) {
                    case 1 : //等級1=====
                        if (clsData.iGetAttention() <= 30) {
                            set2 = 700;  //==靠近
                        } else {
                            set2 = 700 + clsData.iGetAttention() * 9;  //==遠離
                        }
                        break;
                    case 2 :  //等級2=====
                        if (clsData.iGetMeditation() <= 70) {
                            set2 = 700 + clsData.iGetMeditation() * 9;  //==遠離
                        } else {
                            set2 = 700;  //==靠近
                        }
                        break;
                    case 3 :  //等級3=====
                        if (clsData.iGetMeditation() <= 80) {
                            set2 = 700 + clsData.iGetMeditation() * 9;  //==遠離
                        } else {
                            set2 = 700;  //==靠近
                        }
                        break;
                    case 4 :  //等級4=====
                        if (clsData.iGetMeditation() <= 90) {
                            set2 = 700 + clsData.iGetMeditation() * 9;  //==遠離
                        } else {
                            set2 = 700;  //==靠近
                        }
                        break;
                }

                //專注值===============
                clsLineChart.AddPoint(1, dX, clsData.iGetAttention());
                tvAttention.setText(String.valueOf(clsData.iGetAttention()));
                //放鬆值==============
                clsLineChart.AddPoint(2, dX, clsData.iGetMeditation());
                tvMeditation.setText(String.valueOf(clsData.iGetMeditation()));

                clsLineChart.AddPoint(3, dX, clsData.iGetFatigue());
                if (fCheckRB == 1){
                    //Main.addSql2();
                }
                if (fCheckRB == 2){
                }
                if (fCheckRB == 3){
                    //new addDataToSheet().execute("https://script.google.com/macros/s/AKfycbyQ0RLFNohoVVE4O4T_eROfEp0evCgdO3hy4ojKeHrkOQ4tDjyK76SE5b9g9O6tXhC1/exec");
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

