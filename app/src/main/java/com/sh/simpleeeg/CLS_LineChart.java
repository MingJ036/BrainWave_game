package com.sh.simpleeeg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by user on 2017/12/25.
 */

public class CLS_LineChart
{
    CLS_DATA clsData = new CLS_DATA();

    View m_View,m_View2;
    XYMultipleSeriesRenderer m_XYMultipleSeriesRenderer1,m_XYMultipleSeriesRenderer2;
    XYSeriesRenderer m_XYSeriesRenderer1;
    XYSeriesRenderer m_XYSeriesRenderer2;
    XYSeriesRenderer m_XYSeriesRenderer3;


    XYMultipleSeriesDataset m_XYMultipleSeriesDataset;
    XYSeries m_XYSeries1;
    XYSeries m_XYSeries2;
    XYSeries m_XYSeries3;



    //boolean bEnable1stLine = true;
    //boolean bEnable2ndLine = false;

    int iLineNumber = 1;


    //============================================================
    public CLS_LineChart(int iVal)
    {
        iLineNumber = iVal;
    }
    //============================================================
    View viewDrawAppearance(Context _context, boolean bShowGrid, int _iXMax, int _iYMax,
                            int _iLine1Color, int _iLine2Color)
    {
        //建立外觀
        m_XYMultipleSeriesRenderer1 = new XYMultipleSeriesRenderer();


        //第一條線
        m_XYSeriesRenderer1 = new XYSeriesRenderer();
        m_XYSeriesRenderer1.setColor(_iLine1Color);//Color.GREEN);
        //m_XYSeriesRenderer1.setPointStyle(PointStyle.CIRCLE); //沒有指定就是沒有
        m_XYSeriesRenderer1.setFillPoints(true);
        m_XYSeriesRenderer1.setLineWidth(5);
        m_XYMultipleSeriesRenderer1.addSeriesRenderer(m_XYSeriesRenderer1);

        //第二條線
        if(iLineNumber > 1)
        {
            m_XYSeriesRenderer2 = new XYSeriesRenderer();
            m_XYSeriesRenderer2.setColor(_iLine2Color);//Color.GREEN);
            //m_XYSeriesRenderer2.setPointStyle(PointStyle.CIRCLE);
            m_XYSeriesRenderer2.setFillPoints(true);
            m_XYSeriesRenderer2.setLineWidth(5);
            //m_XYSeriesRenderer2.setChartValuesTextSize(30);// 數值的文字大小
            m_XYMultipleSeriesRenderer1.addSeriesRenderer(m_XYSeriesRenderer2);
        }


        //X,Y軸設定
        //m_XYMultipleSeriesRenderer1.setChartTitle("專注+放鬆  专注+放松");
        //m_XYMultipleSeriesRenderer1.setXTitle("X");
        //m_XYMultipleSeriesRenderer1.setYTitle("Y");
        m_XYMultipleSeriesRenderer1.setXAxisMin(0);
        m_XYMultipleSeriesRenderer1.setXAxisMax(_iXMax);
        m_XYMultipleSeriesRenderer1.setYAxisMin(0);
        m_XYMultipleSeriesRenderer1.setYAxisMax(_iYMax);
        m_XYMultipleSeriesRenderer1.setAxesColor(Color.WHITE);
        m_XYMultipleSeriesRenderer1.setLabelsColor(Color.WHITE);
        if(bShowGrid)
            m_XYMultipleSeriesRenderer1.setShowGrid(true);
        else
            m_XYMultipleSeriesRenderer1.setShowGrid(false);
        m_XYMultipleSeriesRenderer1.setYLabels(5);//*(int)(clsData.fGetScale()));
        m_XYMultipleSeriesRenderer1.setMargins(new int[] {
                (int)(30*clsData.fGetLineChartScale()), (int)(60*clsData.fGetLineChartScale()),
                (int)(60*clsData.fGetLineChartScale()), (int)(30*clsData.fGetLineChartScale()) });//上,左,下,右
        m_XYMultipleSeriesRenderer1.setXLabelsAlign(Paint.Align.CENTER);  //設定X軸文字置中
        m_XYMultipleSeriesRenderer1.setYLabelsAlign(Paint.Align.RIGHT);
        //m_XYMultipleSeriesRenderer1.setBackgroundColor(Color.BLACK); // ?置背景色透明
        m_XYMultipleSeriesRenderer1.setApplyBackgroundColor(true); // 使背景色生效
        m_XYMultipleSeriesRenderer1.setGridColor(Color.WHITE);
        if(bShowGrid)
            m_XYMultipleSeriesRenderer1.setLabelsTextSize(20f*clsData.fGetLineChartScale());// 設定XY軸顯示數字的文字大小
        else
            m_XYMultipleSeriesRenderer1.setLabelsTextSize(0f);// 設定XY軸顯示數字的文字大小

        m_XYMultipleSeriesRenderer1.setLegendTextSize(30f*clsData.fGetLineChartScale());//左下方線條文字名稱大小



        //m_XYMultipleSeriesRenderer1.setAxisTitleTextSize(60f);
        //m_XYMultipleSeriesRenderer1.setChartTitleTextSize(60f);
        //建立資料串
        m_XYMultipleSeriesDataset = new XYMultipleSeriesDataset();

        //第一條線
        m_XYSeries1 = new XYSeries("專注");
        m_XYMultipleSeriesDataset.addSeries(m_XYSeries1);

        //第二條線
        if(iLineNumber > 1)
        {
            m_XYSeries2 = new XYSeries("放鬆");
            m_XYMultipleSeriesDataset.addSeries(m_XYSeries2);
        }

        m_View = ChartFactory.getLineChartView(_context, m_XYMultipleSeriesDataset, m_XYMultipleSeriesRenderer1);
        //生成圖表
        return m_View;
    }

    //============================================================
    void AddPoint(int _iLineNumber, double _dX, double _dY)
    {
        switch(_iLineNumber)
        {
            case 1:
                m_XYSeries1.add(_dX, _dY);
                if(_dX >= 30)
                {
                    m_XYMultipleSeriesRenderer1.setXAxisMin(_dX-30);
                    m_XYMultipleSeriesRenderer1.setXAxisMax(_dX);
                }
                break;
            case 2:
                m_XYSeries2.add(_dX, _dY);
                if(_dX >= 30)
                {
                    m_XYMultipleSeriesRenderer1.setXAxisMin(_dX-30);
                    m_XYMultipleSeriesRenderer1.setXAxisMax(_dX);
                }
                break;
            case 3:
                m_XYSeries3.add(_dX, _dY);
                if(_dX >= 30)
                {
                    m_XYMultipleSeriesRenderer2.setXAxisMin(_dX-30);
                    m_XYMultipleSeriesRenderer2.setXAxisMax(_dX);
                }
                break;




        }
    }
    //============================================================
    void Clear()
    {
        /*
        //m_XYMultipleSeriesDataset.removeSeries(m_XYSeries1);
        m_XYSeries1.clear();
        //m_XYSeries2.clear();
        m_XYMultipleSeriesDataset.clear();
        m_XYSeries1 = null;
        //m_XYSeries2 = null;
        m_XYMultipleSeriesDataset = null;
        m_XYSeriesRenderer1 = null;
        //m_XYSeriesRenderer2 = null;
        m_XYMultipleSeriesRenderer = null;
        */
    }
    //============================================================
    View viewDrawAppearance2(Context _context, boolean bShowGrid, int _iXMax, int _iYMax,
                             int _iLine3Color, int _iLine4Color)
    {
        //建立外觀
        m_XYMultipleSeriesRenderer2 = new XYMultipleSeriesRenderer();


        //第三條線
        if(iLineNumber > 2) {
            m_XYSeriesRenderer3 = new XYSeriesRenderer();
            m_XYSeriesRenderer3.setColor(_iLine3Color);//Color;
            //m_XYSeriesRenderer3.setPointStyle(PointStyle.CIRCLE); //沒有指定就是沒有
            m_XYSeriesRenderer3.setFillPoints(true);
            m_XYSeriesRenderer3.setLineWidth(5);//線寬的度
            m_XYMultipleSeriesRenderer2.addSeriesRenderer(m_XYSeriesRenderer3);
        }


        //X,Y軸設定
        //m_XYMultipleSeriesRenderer2.setChartTitle("專注+放鬆  专注+放松");
        //m_XYMultipleSeriesRenderer2.setXTitle("X");
        //m_XYMultipleSeriesRenderer2.setYTitle("Y");
        m_XYMultipleSeriesRenderer2.setXAxisMin(0);//設置x軸的起始點
        m_XYMultipleSeriesRenderer2.setXAxisMax(_iXMax);//設置一屏有多少個點
        m_XYMultipleSeriesRenderer2.setYAxisMin(0);
        m_XYMultipleSeriesRenderer2.setYAxisMax(_iYMax);
        m_XYMultipleSeriesRenderer2.setAxesColor(Color.WHITE);
        m_XYMultipleSeriesRenderer2.setLabelsColor(Color.WHITE);
        if(bShowGrid)
            m_XYMultipleSeriesRenderer2.setShowGrid(true);
        else
            m_XYMultipleSeriesRenderer2.setShowGrid(false);
        m_XYMultipleSeriesRenderer2.setYLabels(10);//*(int)(clsData.fGetScale()));//把y軸刻度平均分成多少個
        m_XYMultipleSeriesRenderer2.setMargins(new int[] {
                (int)(30*clsData.fGetLineChartScale()), (int)(60*clsData.fGetLineChartScale()),
                (int)(60*clsData.fGetLineChartScale()), (int)(30*clsData.fGetLineChartScale()) });//上,左,下,右
        m_XYMultipleSeriesRenderer2.setXLabelsAlign(Paint.Align.CENTER);  //設定X軸文字置中
        m_XYMultipleSeriesRenderer2.setYLabelsAlign(Paint.Align.RIGHT);
        //m_XYMultipleSeriesRenderer2.setBackgroundColor(Color.BLACK); // ?置背景色透明
        m_XYMultipleSeriesRenderer2.setApplyBackgroundColor(true); // 使背景色生效
        m_XYMultipleSeriesRenderer2.setGridColor(Color.WHITE);
        if(bShowGrid)
            m_XYMultipleSeriesRenderer2.setLabelsTextSize(25f*clsData.fGetLineChartScale());// 設定XY軸顯示數字的文字大小
        else
            m_XYMultipleSeriesRenderer2.setLabelsTextSize(0f);// 設定XY軸顯示數字的文字大小

        m_XYMultipleSeriesRenderer2.setLegendTextSize(40f*clsData.fGetLineChartScale());//左下方線條文字名稱大小



        //m_XYMultipleSeriesRenderer2.setAxisTitleTextSize(60f);
        //m_XYMultipleSeriesRenderer2.setChartTitleTextSize(60f);
        //建立資料串
        m_XYMultipleSeriesDataset = new XYMultipleSeriesDataset();


        //第三條線
        if(iLineNumber > 2) {
            m_XYSeries3 = new XYSeries("疲勞");
            m_XYMultipleSeriesDataset.addSeries(m_XYSeries3);
        }

        m_View2 = ChartFactory.getLineChartView(_context, m_XYMultipleSeriesDataset, m_XYMultipleSeriesRenderer2);


        return m_View2;

    }
}
