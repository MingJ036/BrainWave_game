package com.sh.simpleeeg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static com.sh.simpleeeg.CLS_DATA.dAttentionAvg;
import static com.sh.simpleeeg.CLS_DATA.dDeltaPercentage;
import static com.sh.simpleeeg.CLS_DATA.dHighAlphaPercentage;
import static com.sh.simpleeeg.CLS_DATA.dHighBetaPercentage;
import static com.sh.simpleeeg.CLS_DATA.dHighGammaPercentage;
import static com.sh.simpleeeg.CLS_DATA.dLowAlphaPercentage;
import static com.sh.simpleeeg.CLS_DATA.dLowBetaPercentage;
import static com.sh.simpleeeg.CLS_DATA.dLowGammaPercentage;
import static com.sh.simpleeeg.CLS_DATA.dMeditationAvg;
import static com.sh.simpleeeg.CLS_DATA.dThetaPercentage;
import static com.sh.simpleeeg.CLS_DATA.iDelta;
import static com.sh.simpleeeg.CLS_DATA.iHighAlpha;
import static com.sh.simpleeeg.CLS_DATA.iHighGamma;
import static com.sh.simpleeeg.CLS_DATA.iLowAlpha;
import static com.sh.simpleeeg.CLS_DATA.iLowGamma;
import static com.sh.simpleeeg.CLS_DATA.iTheta;
import static com.sh.simpleeeg.CLS_DATA.strBirthday;
import static com.sh.simpleeeg.CLS_DATA.strSex;
import static com.sh.simpleeeg.CLS_DATA.strName;
import static com.sh.simpleeeg.CLS_DATA.strBloodType;

public class Report extends Activity {
    CLS_DATA clsData = new CLS_DATA();
    CLS_PARAM S = new CLS_PARAM();

    PieChart chartPreAtt;    //chartPreMed
    TextView txtList,txtData;
    TextView tvDelta, tvTheta, tvLowAlpha, tvHighAlpha, tvLowBeta, tvHighBeta, tvLowGamma, tvHighGamma;
    ProgressBar pbDelta, pbTheta, pbLowAlpha, pbHighAlpha, pbLowBeta, pbHighBeta, pbLowGamma, pbHighGamma;

    Context mContext;

    private static final String DB_FILE = "sample.db", DB_FILE2 = "sample2.db",
            DB_TABLE = "sample", DB_TABLE2 = "sample2" , DB_TABLE_ID = "sample3";
    private DBHelper mySampleDbOpenHelper,mySampleDbOpenHelper2;



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.report);


        mySampleDbOpenHelper =
                new DBHelper(getApplicationContext(), DB_FILE, null, 1);
        mySampleDbOpenHelper2 =
                new DBHelper(getApplicationContext(), DB_FILE2, null, 1);


        SQLiteDatabase sampleDb = mySampleDbOpenHelper.getWritableDatabase();

        // 檢查資料表是否已經存在
        Cursor cursor = sampleDb.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                        DB_TABLE + "'", null);


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



        mContext = this;


        txtList = (TextView) findViewById(R.id.txtList);
        txtData = (TextView) findViewById(R.id.txtData);
        //chartPreMed = (PieChart) findViewById(R.id.pieChartMed);


        float fSize = 10f;




        clsData.DoRawCalculation();


        ShowSql();


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
    }
    @Override
    protected void onStart()
    {
        super.onStart();


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
        Intent intent = new Intent();
        intent.setClass(this, Main.class);//input info
        startActivity(intent);
    }
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    void ShowReport()
    {
        tvDelta.setText(String.valueOf(clsData.iGetRawPercentage(S.DELTA))+" % ");
        pbDelta.setProgress(clsData.iGetRawPercentage(S.DELTA));

        tvTheta.setText(String.valueOf(clsData.iGetRawPercentage(S.THETA))+" % ");
        pbTheta.setProgress(clsData.iGetRawPercentage(S.THETA));

        tvLowAlpha.setText(String.valueOf(clsData.iGetRawPercentage(S.LOWALPHA))+" % ");
        pbLowAlpha.setProgress(clsData.iGetRawPercentage(S.LOWALPHA));

        tvHighAlpha.setText(String.valueOf(clsData.iGetRawPercentage(S.HIGHALPHA))+" % ");
        pbHighAlpha.setProgress(clsData.iGetRawPercentage(S.HIGHALPHA));

        tvLowBeta.setText(String.valueOf(clsData.iGetRawPercentage(S.LOWBETA))+" % ");
        pbLowBeta.setProgress(clsData.iGetRawPercentage(S.LOWBETA));

        tvHighBeta.setText(String.valueOf(clsData.iGetRawPercentage(S.HIGHBETA))+" % ");
        pbHighBeta.setProgress(clsData.iGetRawPercentage(S.HIGHBETA));

        tvLowGamma.setText(String.valueOf(clsData.iGetRawPercentage(S.LOWGAMMA))+" % ");
        pbLowGamma.setProgress(clsData.iGetRawPercentage(S.LOWGAMMA));

        tvHighGamma.setText(String.valueOf(clsData.iGetRawPercentage(S.HIGHGAMMA))+" % ");
        pbHighGamma.setProgress(clsData.iGetRawPercentage(S.HIGHGAMMA));

        CreatePieChart();
    }
    //==============================================================================================
    void CreatePieChart()
    {
        PieDataSet pieDataSetPreAtt, pieDataSetPostAtt, pieDataSetPreMed, pieDataSetPostMed;
        PieData pieDataPreAtt, pieDataPostAtt, pieDataPreMed, pieDataPostMed;
        ArrayList<PieEntry> _entriesPreAtt, _entriesPostAtt, _entriesPreMed, _entriesPostMed;
        Description _descriptionPreAtt, _descriptionPostAtt, _descriptionPreMed, _descriptionPostMed;

        String str;

        //---------------------------
        /*
        final int[] MY_COLORS = {Color.rgb(255,180,180), Color.rgb(0,255,0), Color.rgb(0,255,255)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS)
            colors.add(c);
        */
        //---------------------------
        //--------------------------- Pre Att
        //---------------------------
        _entriesPreAtt = new ArrayList<>();
        if(clsData.fGetPart(1,1) > 0)
            _entriesPreAtt.add(new PieEntry(clsData.fGetPart(1,1), "低"));//顏色無效
        if(clsData.fGetPart(1,2) > 0)
            _entriesPreAtt.add(new PieEntry(clsData.fGetPart(1,2), "中"));
        if(clsData.fGetPart(1,3) > 0)
            _entriesPreAtt.add(new PieEntry(clsData.fGetPart(1,3), "高"));
        pieDataSetPreAtt = new PieDataSet(_entriesPreAtt, "");
        pieDataSetPreAtt.setValueTextSize(15f*clsData.fGetScale());//每片裡面的文字大小

        pieDataSetPreAtt.setFormSize(20f*clsData.fGetScale());//圖下方高中低顏色塊的大小

        if(clsData.fGetPart(1,1) > 0 && clsData.fGetPart(1,2) > 0 && clsData.fGetPart(1,3) > 0)
            pieDataSetPreAtt.setColors(Color.rgb(255,180,180), Color.rgb(0,255,0), Color.rgb(0,255,255));
        else if(clsData.fGetPart(1,1) > 0 && clsData.fGetPart(1,2) > 0)
            pieDataSetPreAtt.setColors(Color.rgb(255,180,180), Color.rgb(0,255,0));
        else if(clsData.fGetPart(1,2) > 0 && clsData.fGetPart(1,3) > 0)
            pieDataSetPreAtt.setColors(Color.rgb(0,255,0), Color.rgb(0,255,255));
        else if(clsData.fGetPart(1,1) > 0 && clsData.fGetPart(1,3) > 0)
            pieDataSetPreAtt.setColors(Color.rgb(255,180,180), Color.rgb(0,255,255));
        else if(clsData.fGetPart(1,1) > 0)
            pieDataSetPreAtt.setColors(Color.rgb(255,180,180));
        else if(clsData.fGetPart(1,2) > 0)
            pieDataSetPreAtt.setColors(Color.rgb(0,255,0));
        else if(clsData.fGetPart(1,3) > 0)
            pieDataSetPreAtt.setColors(Color.rgb(0,255,255));


        //pieDataSetPreAtt.setValueLineColor(Color.DKGRAY);
        pieDataSetPreAtt.setValueTextColor(Color.BLUE);//每片裡面的數值的顏色

        pieDataPreAtt = new PieData();
        pieDataPreAtt.addDataSet(pieDataSetPreAtt);
        //---------------------------每片裡面的文字
        chartPreAtt.setDrawSliceText(true);//每片裡面的中文的文字
        chartPreAtt.setEntryLabelColor(Color.BLUE);//每片裡面的中文的顏色
        chartPreAtt.setData(pieDataPreAtt);
        //---------------------------中間洞洞
        str = String.valueOf(clsData.iGetRawPercentage(S.ATTENTION));
        //pieChart.setHoleRadius(25f);
        chartPreAtt.setCenterText("專注\n"+str+"%");
        chartPreAtt.setCenterTextSize(20f*clsData.fGetScale());
        chartPreAtt.setCenterTextColor(Color.RED);
        //---------------------------左下方的文字標示
        chartPreAtt.getLegend().setEnabled(true);//隱藏圖片左下方的文字標示
        chartPreAtt.getLegend().setTextSize(20f*clsData.fGetScale());
        chartPreAtt.getLegend().setTextColor(Color.WHITE);
        chartPreAtt.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER );
        //---------------------------右下方的文字標示
        //pieChart.setDescription("");//圖片右下角的文字
        _descriptionPreAtt = new Description();
        _descriptionPreAtt.setTextColor(Color.GREEN);
        _descriptionPreAtt.setTextSize(20*clsData.fGetScale());
        _descriptionPreAtt.setText("");
        chartPreAtt.setDescription(_descriptionPreAtt);//圖片右下角的文字
        //---------------------------
        chartPreAtt.animateY(1500);//可以手動轉圈圈的旋轉動畫
        chartPreAtt.setRotationEnabled(false);//可以手動轉圈圈
        //---------------------------
        //--------------------------- pre med
        //---------------------------
        _entriesPreMed = new ArrayList<>();
        if(clsData.fGetPart(2,1) > 0)
            _entriesPreMed.add(new PieEntry(clsData.fGetPart(2,1), "低"));//顏色無效
        if(clsData.fGetPart(2,2) > 0)
            _entriesPreMed.add(new PieEntry(clsData.fGetPart(2,2), "中"));
        if(clsData.fGetPart(2,3) > 0)
            _entriesPreMed.add(new PieEntry(clsData.fGetPart(2,3), "高"));
        pieDataSetPreMed = new PieDataSet(_entriesPreMed, "");
        pieDataSetPreMed.setValueTextSize(20f*clsData.fGetScale());//每片裡面的文字大小

        pieDataSetPreMed.setFormSize(20f*clsData.fGetScale());//圖下方高中低顏色塊的大小


        if(clsData.fGetPart(2,1) > 0 && clsData.fGetPart(2,2) > 0 && clsData.fGetPart(2,3) > 0)
            pieDataSetPreMed.setColors(Color.rgb(255,180,180), Color.rgb(0,255,0), Color.rgb(0,255,255));
        else if(clsData.fGetPart(2,1) > 0 && clsData.fGetPart(2,2) > 0)
            pieDataSetPreMed.setColors(Color.rgb(255,180,180), Color.rgb(0,255,0));
        else if(clsData.fGetPart(2,2) > 0 && clsData.fGetPart(2,3) > 0)
            pieDataSetPreMed.setColors(Color.rgb(0,255,0), Color.rgb(0,255,255));
        else if(clsData.fGetPart(2,1) > 0 && clsData.fGetPart(2,3) > 0)
        pieDataSetPreMed.setColors(Color.rgb(255,180,180), Color.rgb(0,255,255));
    else if(clsData.fGetPart(2,1) > 0)
            pieDataSetPreMed.setColors(Color.rgb(255,180,180));
        else if(clsData.fGetPart(2,2) > 0)
            pieDataSetPreMed.setColors(Color.rgb(0,255,0));
        else if(clsData.fGetPart(2,3) > 0)
            pieDataSetPreMed.setColors(Color.rgb(0,255,255));



        //pieDataSetPreMed.setValueLineColor(Color.DKGRAY);
        pieDataSetPreMed.setValueTextColor(Color.BLUE);//每片裡面的數值的顏色

        pieDataPreMed = new PieData();
        pieDataPreMed.addDataSet(pieDataSetPreMed);
        //---------------------------每片裡面的文字
        //chartPreMed.setDrawSliceText(true);//每片裡面的中文的文字
        //chartPreMed.setEntryLabelColor(Color.BLUE);//每片裡面的中文的顏色
        //chartPreMed.setData(pieDataPreMed);
        //---------------------------中間洞洞
        str = String.valueOf(clsData.iGetRawPercentage(S.MEDITATION));
        //pieChart.setHoleRadius(25f);
        //chartPreMed.setCenterText("放鬆\n"+str+"%");
        //chartPreMed.setCenterTextSize(20f*clsData.fGetScale());
        //chartPreMed.setCenterTextColor(Color.RED);
        //---------------------------左下方的文字標示
        //chartPreMed.getLegend().setEnabled(true);//隱藏圖片左下方的文字標示
       // chartPreMed.getLegend().setTextSize(20f*clsData.fGetScale());
        //chartPreMed.getLegend().setTextColor(Color.WHITE);
        //chartPreMed.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER );
        //---------------------------右下方的文字標示
        //pieChart.setDescription("");//圖片右下角的文字
        _descriptionPreMed = new Description();
        _descriptionPreMed.setTextColor(Color.GREEN);
        _descriptionPreMed.setTextSize(20*clsData.fGetScale());
        _descriptionPreMed.setText("");
        //chartPreMed.setDescription(_descriptionPreMed);//圖片右下角的文字
        //---------------------------
        //chartPreMed.animateY(1500);//可以手動轉圈圈的旋轉動畫
        ///chartPreMed.setRotationEnabled(false);//可以手動轉圈圈
    }
    //==============================================================================================

    //==============================================================================================


        @SuppressLint("SetTextI18n")
        void ShowSql()
        {
            SQLiteDatabase sampleDb = mySampleDbOpenHelper.getWritableDatabase();
            Cursor c = sampleDb.query(true, DB_TABLE, new String[]{}, null, null, null, null, null, null);

            if (c == null)
                return;



            if (c.getCount() == 0) {
                txtList.setText("");
                Toast.makeText(Report.this, "沒有資料", Toast.LENGTH_LONG)
                    .show();
            } else {

                c.moveToFirst();
                txtList.setText("Name : " + c.getString(1) + "\n"
                        + "Sex : " + c.getString(2) + "\n"
                        + "Birthday : " + c.getString(3) + "\n"
                        + "BloodType : " + c.getString(4) + "\n");


                while (c.moveToNext()) {
                    txtList.append("\n"
                            + "Sex : " + c.getString(1) + "\n"
                            + "Birthday : " + c.getString(2) + "\n"
                            + "Birthday : " + c.getString(3) + "\n"
                            + "BloodType : " + c.getString(4) + "\n");

                    }
            }
            sampleDb.close();

            SQLiteDatabase sampleDb2 = mySampleDbOpenHelper2.getWritableDatabase();
            Cursor c2 = sampleDb2.query(true, DB_TABLE2, new String[]{}, null, null, null, null, null, null);

            if (c2 == null)
                return;

            if (c2.getCount() == 0) {
                txtData.setText("");
                Toast.makeText(Report.this, "沒有資料", Toast.LENGTH_LONG).show();
            }
            else {
                c2.moveToFirst();

                txtData.setText(
                        c2.getString(0) + " : " + "\n"
                        + "Delta : " + c2.getString(1) + "\n"
                        + "Theta : " + c2.getString(2) + "\n"
                        + "LowAlpha : " + c2.getString(3) + "\n"
                        + "HighAlpha : " + c2.getString(4) + "\n"
                        + "LowBeta : " + c2.getString(5) + "\n"
                        + "HighBeta : " + c2.getString(6) + "\n"
                        + "LowGamma : " + c2.getString(7) + "\n"
                        + "HighGamma : " + c2.getString(8) + "\n"
                        + "Attention : " + c2.getString(9) + "\n"
                        + "Meditation : " + c2.getString(10) + "\n"
                        + "Fatigue : " + c2.getString(11) + "\n"
                        );

                while (c2.moveToNext()) {
                    txtData.append("\n"
                            + c2.getString(0) + " : " + "\n"
                            + "Delta : " + c2.getString(1) + "\n"
                            + "Theta : " + c2.getString(2) + "\n"
                            + "LowAlpha : " + c2.getString(3) + "\n"
                            + "HighAlpha : " + c2.getString(4) + "\n"
                            + "LowBeta : " + c2.getString(5) + "\n"
                            + "HighBeta : " + c2.getString(6) + "\n"
                            + "LowGamma : " + c2.getString(7) + "\n"
                            + "HighGamma : " + c2.getString(8) + "\n"
                            + "Attention : " + c2.getString(9) + "\n"
                            + "Meditation : " + c2.getString(10) + "\n"
                            + "Fatigue : " + c2.getString(11) + "\n"
                            );
                }
            }

            sampleDb2.close();
        }





    //刪除
    private void del(){

        String id = "1"; //刪除id為1的資料

        SQLiteDatabase sampleDb = mySampleDbOpenHelper.getWritableDatabase();

        sampleDb.delete(DB_TABLE, 1 + "=" + id, null);

    }





}
