package com.sh.simpleeeg;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class Main extends Activity {
    private static SQLiteOpenHelper mySampleDbOpenHelper, mySampleDbOpenHelper2;
    private static CLS_DATA clsData = new CLS_DATA();
    CLS_PARAM S = new CLS_PARAM();
    CLS_BrainWave clsBrainWave = new CLS_BrainWave();

    private static final String DB_FILE = "sample.db", DB_FILE2 = "sample2.db",
            DB_TABLE = "sample", DB_TABLE2 = "sample2" , DB_TABLE_ID = "sample3";
    private DBHelper searchID;

    RadioGroup rgSaveMethod;
    RadioButton rbSQLite, rbExcel, rbCloud;

    RequestOptions myGdiOptions;

    TextView tvName, tvBirthday, tvSex, tvBloodType;
    ImageView ivBG, ivSignal,ivCaption,ivStartTest;
    EditText etName, etBirthday, etSex, etBloodType;

    static private ClockThreadMain m_clockThreadMain;
    static private Handler m_clockHandler = new Handler();
    //int iThreadProcess = 0;
    Context mContext;
    boolean bConnected = false;

    static String strName;
    static String strBirthday;
    static String strSex;
    static String strBloodType;

    boolean bQuit = false;

    private ClockThread mClockThread;
    private Handler mClockHandler = new Handler();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);

        final int sqlite = 1, excel = 2, cloud = 3;



        ivStartTest = findViewById(R.id.ivStartTest);
        rbSQLite = findViewById(R.id.rbSQLite);
        rbExcel = findViewById(R.id.rbExcel);
        rbCloud = findViewById(R.id.rbCloud);

        rgSaveMethod = findViewById(R.id.rgSaveMethod);
        ivStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //選擇SQLite------------------------------------------------------------------------
                if (rbSQLite.isChecked()) {
                    mySampleDbOpenHelper =
                            new DBHelper(getApplicationContext(), DB_FILE, null, 1);
                    mySampleDbOpenHelper2 =
                            new DBHelper(getApplicationContext(), DB_FILE2, null, 1);


                    searchID = new DBHelper(getApplicationContext(), DB_TABLE_ID, null, 1);

                    SQLiteDatabase sampleDb = mySampleDbOpenHelper.getWritableDatabase();
                    SQLiteDatabase sampleDb2 = mySampleDbOpenHelper2.getWritableDatabase();


                    // 檢查資料表是否已經存在
                    Cursor cursor = sampleDb.rawQuery(
                            "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                                    DB_TABLE + "'", null);
                    Cursor cursor2 = sampleDb2.rawQuery(
                            "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                                    DB_TABLE2 + "'", null);

                    if (cursor != null) {
                        if (cursor.getCount() == 0) // 沒有資料表，需要建立一個資料表
                            sampleDb.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                                    "_id INTEGER PRIMARY KEY," +
                                    "Name TEXT NOT NULL," +
                                    "Sex TEXT," +
                                    "Birthday TEXT," +
                                    "BloodType TEXT);");

                        cursor.close();
                    }
                    if (cursor2 != null) {
                        if (cursor2.getCount() == 0) // 沒有資料表，需要建立一個資料表
                            sampleDb2.execSQL("CREATE TABLE " + DB_TABLE2 + " (" +
                                    "_id INTEGER PRIMARY KEY," +
                                    "Delta TEXT,"+
                                    "Theta TEXT,"+
                                    "LowAlpha TEXT,"+
                                    "HighAlpha TEXT,"+
                                    "LowBeta TEXT,"+
                                    "HighBeta TEXT,"+
                                    "LowGamma TEXT,"+
                                    "HighGamma TEXT,"+
                                    "Attention TEXT,"+
                                    "Meditation TEXT," +
                                    "Fatigue TEXT);");
                        cursor2.close();
                    }
                    Intent intent = new Intent();
                    intent.setClass(Main.this, Test.class);//input info
                    Bundle bundle = new Bundle();
                    bundle.putString("CheckRB", String.valueOf(sqlite));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                //選擇Excel--------------------------------------------------------------------------
                else if (rbExcel.isChecked()){
                    Intent intent = new Intent();
                    intent.setClass(Main.this, Test.class);//input info
                    Bundle bundle = new Bundle();
                    bundle.putString("CheckRB", String.valueOf(excel));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                //選擇Cloud--------------------------------------------------------------------------
                else if (rbCloud.isChecked()){
                    Intent intent = new Intent();
                    intent.setClass(Main.this, Test.class);//input info
                    Bundle bundle = new Bundle();
                    bundle.putString("CheckRB", String.valueOf(cloud));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Main.this, "請選擇儲存方式", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mContext = this;
        myGdiOptions = new RequestOptions().fitCenter();

        ivBG = (ImageView)findViewById(R.id.ivBG);
        ivSignal = (ImageView)findViewById(R.id.ivSignal);
        ivCaption = (ImageView)findViewById(R.id.ivCaption);
        ivStartTest = (ImageView)findViewById(R.id.ivStartTest);
        tvName = (TextView)findViewById(R.id.tvName);
        tvBirthday = (TextView)findViewById(R.id.tvSex);
        tvSex = (TextView)findViewById(R.id.tvSex);
        tvBloodType = (TextView)findViewById(R.id.tvBloodType);
        etName = (EditText)findViewById(R.id.etName);
        etSex = (EditText)findViewById(R.id.etSex);
        etBirthday = (EditText)findViewById(R.id.etBirthday);
        etBloodType = (EditText)findViewById(R.id.etBloodType);

        clsData.SetDspMetrics(this);
        /*
        //160 dpi 的比例為 1，如果大於160的就按比例下去除，避免圖檔過大占用記憶體
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        clsData.SetScreenWidthHeight(size.x, size.y);
        //
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);
        float fScale = (float)(160.0 / (float)densityDpi);
        clsData.SetScale(fScale);
        */

        float fSize1 = 20f;
        float fSize2 = 15f;

        tvName.setTextSize(fSize2*clsData.fGetScale());
        tvBirthday.setTextSize(fSize2*clsData.fGetScale());
        tvSex.setTextSize(fSize2*clsData.fGetScale());
        tvBloodType.setTextSize(fSize2*clsData.fGetScale());
        etName.setTextSize(fSize2*clsData.fGetScale());
        etSex.setTextSize(fSize2*clsData.fGetScale());
        etBirthday.setTextSize(fSize2*clsData.fGetScale());
        etBloodType.setTextSize(fSize2*clsData.fGetScale());


        SetBrainwaveCallback();

        m_clockThreadMain = new ClockThreadMain();
        m_clockHandler.postDelayed(m_clockThreadMain, 10);

        mClockThread = new ClockThread();
        mClockHandler.post(mClockThread);
        StoragePermissionGranted();
    }

    static void addSql() {
        SQLiteDatabase sampleDb = mySampleDbOpenHelper.getWritableDatabase();
        ContentValues newRow = new ContentValues();
        newRow.put("Name", strName);
        newRow.put("Sex", strSex);
        newRow.put("Birthday", strBirthday);
        newRow.put("BloodType", strBloodType);
        sampleDb.insert(DB_TABLE, null, newRow);
        sampleDb.close();
    }
    static void addSql2() {
        SQLiteDatabase sampleDb2 = mySampleDbOpenHelper2.getWritableDatabase();
        ContentValues newRow = new ContentValues();
        newRow.put("Delta", clsData.iGetDelta());
        newRow.put("Theta", clsData.iGetTheta());
        newRow.put("LowAlpha", clsData.iGetLowAlpha());
        newRow.put("HighAlpha", clsData.iGetHighAlpha());
        newRow.put("LowBeta", clsData.iGetLowBeta());
        newRow.put("HighBeta", clsData.iGetHighBeta());
        newRow.put("LowGamma", clsData.iGetLowGamma());
        newRow.put("HighGamma", clsData.iGetHighGamma());
        newRow.put("Attention", clsData.iGetAttention());
        newRow.put("Meditation", clsData.iGetMeditation());
        newRow.put("Fatigue", clsData.iGetFatigue());


        sampleDb2.insert(DB_TABLE2, null, newRow);
        sampleDb2.close();
    }
    //-------------------------------------------------------------------


    //---------------------------------------------------------------------------

    @Override
    protected void onStart()
    {
        super.onStart();

        RestoreData();
        bQuit = false;
        Glide.with(mContext).load(R.drawable.bg_main).apply(myGdiOptions).into(ivBG);
        Glide.with(mContext).load(R.drawable.caption).apply(myGdiOptions).into(ivCaption);
        Glide.with(mContext).load(R.drawable.start_test).apply(myGdiOptions).into(ivStartTest);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        bQuit = true;
        Glide.with(this).clear(ivBG);
        Glide.with(this).clear(ivSignal);
        Glide.with(this).clear(ivCaption);
        Glide.with(this).clear(ivStartTest);

        strName = etName.getText().toString();
        strBirthday = etBirthday.getText().toString();
        strSex = etSex.getText().toString();
        strBloodType = etBloodType.getText().toString();
        clsData.SetMemberInfo(strName, strSex, strBirthday, strBloodType);

        SharedPreferences prefer = getSharedPreferences("EEGAppFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString("Name", strName);
        editor.putString("Sex", strSex);
        editor.putString("Birthday", strBirthday);
        editor.putString("BloodType", strBloodType);
        editor.commit();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    public void onBackPressed() //不讓user按螢幕左下方的 "退回上一層"
    {
        /*
        Intent intent = new Intent();
        intent.setClass(this, ChooseBW.class);//input info
        startActivity(intent);
        */
    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    void RestoreData()
    {
        SharedPreferences prefer = getSharedPreferences("EEGAppFile", MODE_PRIVATE);
        etName.setText(prefer.getString("Name", ""));
        etSex.setText(prefer.getString("Sex", ""));
        etBirthday.setText(prefer.getString("Birthday", ""));
        etBloodType.setText(prefer.getString("BloodType", ""));
    }
    //==============================================================================================
    public void SetBrainwaveCallback()
    {

        clsBrainWave.SetCallback_Main(new CLS_BrainWave.Brainwave_Callback_Main()
        {
            //@Override
            public void Do(int iCmd, int iVal)
            {
                if(bQuit)
                    return;

                switch(iCmd)
                {
                    case 0:
                        //System.out.println("work");
                        break;
                }
                //
                if(iCmd == S.BrainwaveConnected)
                {
                    //tvMessage.setText("請戴好腦波儀");
                    Glide.with(mContext).load(R.drawable.signal_connected).apply(myGdiOptions).into(ivSignal);
                    bConnected = true;
                }
                else if(iCmd==S.BrainwavePaired)
                {
                    bConnected = false;
                }
                else if(iCmd==S.BrainwaveDisconnected)
                {
                    //tvMessage.setText("檢查藍芽、腦波儀");
                    Glide.with(mContext).load(R.drawable.signal_disconnected).apply(myGdiOptions).into(ivSignal);
                    bConnected = false;
                }
                else if(iCmd==S.SIGNAL_GOOD)
                {
                    //tvMessage.setText("OK");
                    Glide.with(mContext).load(R.drawable.signal_good).apply(myGdiOptions).into(ivSignal);
                    bConnected = true;
                }
            }
        });
    }

    //==============================================================================================
    private class ClockThreadMain extends Thread
    {
        @Override
        public void run()
        {
            switch(clsData.iThreadProcess)
            {
                case 0:
                    switch(clsBrainWave.iConnect())
                    {
                        case 1:
                            clsData.iThreadProcess = 1;
                            m_clockHandler.postDelayed(m_clockThreadMain, 10);
                            break;
                        case -1:
                            clsData.iThreadProcess = 2;
                            bConnected = false;
                            //tvMessage.setText("請開啟藍芽 !");
                            Glide.with(mContext).load(R.drawable.signal_disconnected).apply(myGdiOptions).into(ivSignal);
                            break;
                        case -2:
                            clsData.iThreadProcess = 2;
                            SendProcessMessage(S.BrainwaveDisconnected);
                            break;
                        default:
                            clsData.iThreadProcess = 2;
                            break;
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
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
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            //String strValue = String.valueOf(msg.arg1);
            switch (msg.what)
            {
            }
            //因為如果使用 S.xxx 在 用 switch case 會出現錯誤(android的bug)
            if(msg.what == S.SIGNAL_GOOD)
            {

            }
            else if(msg.what == S.BrainwaveConnected)
            {

            }
            else if(msg.what == S.BrainwaveDisconnected)
            {
                //tvMessage.setText("同組腦波儀未配對");
                Glide.with(mContext).load(R.drawable.signal_disconnected).apply(myGdiOptions).into(ivSignal);
                bConnected = false;
            }
            else if(msg.what == S.BrainwavePaired)
            {

            }
            else if(msg.what == S.BluetoothClosed)
            {

            }
            else if(msg.what == S.MAC_VALID_OK)
            {
                //tvMessage.setText("OK");
            }
            else if(msg.what == S.MAC_VALID_NO)
            {
                //tvMessage.setText("NO");
            }
            else if(msg.what == S.DO_CONNECT_BRAINWAVE)
            {

            }
            else if(msg.what == S.DO_CHECK_VALID)
            {

            }
        }
    };
    //==============================================================================================
    public void ivStartTest_OnClick(View view) {
        System.out.println("=================================================");
        Intent intent = new Intent();
        intent.setClass(this, Test.class);//input info
        startActivity(intent);

    }
    //===================================================
    //===================================================
    //===================================================
    void StoragePermissionGranted() {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        if (Build.VERSION.SDK_INT < 23)
            return;
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    //===================================================
    private class ClockThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                if (ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                    new CountDownTimer(1100,1000){
                        @Override
                        public void onFinish() {
                        }
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                    }.start();
                }
                else {
                    StoragePermissionGranted();
                    mClockHandler.postDelayed(mClockThread, 1000);
                }
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }
    //===================================================
    public void ivBG_OnClick(View view) {
        ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
    //==============================================================================================

}
