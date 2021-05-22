package com.sh.simpleeeg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.media.AudioManager;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;







/**
 * Created by user on 2017/12/13.
 */

public class CLS_DATA
{
    CLS_PARAM S = new CLS_PARAM();

    Bitmap bmpScreenshot;



    static String strServerIP = "114.33.109.227";//"192.168.43.11";//
    static int iLanguage;

    static String strMAC = "";
    static String strNumber = "";
    static int  iGoodSignal = 0;
    static int  iAttention = 0;
    static int  iMeditation = 0;
    static int  iDelta = 0;
    static int  iTheta = 0;
    static int  iLowAlpha = 0;
    static int  iHighAlpha = 0;
    static int  iLowBeta = 0;
    static int  iHighBeta = 0;
    static int  iLowGamma = 0;
    static int  iHighGamma = 0;
    static int  iFeedbackGQD = 0;

    static boolean bfNeuroskyConnected = false;
    static boolean bfTestReady = false;
    static boolean bSendDataOk = false;

    static int iBWTestModel = 0;
    static int iBVRTestModel = 0;

    public static int iScreenWidth = 1;//896;
    public static int iScreenHeight = 1;//504;

    static float fScale = 1;

    static int iMaxVomume = 10;
    static int iVolume = 50;

    static Context m_Context;

    static List<CLS_RAWDATA> listRawData = new ArrayList<CLS_RAWDATA>();

    static double dThetaSum=0,dDeltaSum=0,dLowAlphaSum=0,dHighAlphaSum=0,
            dLowBetaSum=0,dHighBetaSum=0,dLowGammaSum=0,dHighGammaSum=0,
            dAttentionSum=0,dMeditationSum=0;
    static double dThetaAvg=0,dDeltaAvg=0,dLowAlphaAvg=0,dHighAlphaAvg=0,
            dLowBetaAvg=0,dHighBetaAvg=0,dLowGammaAvg=0,dHighGammaAvg=0,
            dAttentionAvg=0,dMeditationAvg=0;
    static double dThetaMax=0,dDeltaMax=0,dLowAlphaMax=0,dHighAlphaMax=0,
            dLowBetaMax=0,dHighBetaMax=0,dLowGammaMax=0,dHighGammaMax=0,
            dAttentionMax=0,dMeditationMax=0;
    static double dThetaPercentage=0,dDeltaPercentage=0,dLowAlphaPercentage=0,dHighAlphaPercentage=0,
            dLowBetaPercentage=0,dHighBetaPercentage=0,
            dLowGammaPercentage=0,dHighGammaPercentage=0;
    static int iEnergy=0;
    static int iSleepQuality=0;
    static double iFatigue = 0;;

    static int iVersion = 2;

    static int iTrainingNum = 0;

    static String strName, strBloodType, strSex, strBirthday;
    static int iGender=1;

    public static int iThreadProcess = 0;

    static float fAttLowPart=0, fAttMediumPart=0, fAttHighPart=0;
    static float fMedLowPart=0, fMedMediumPart=0, fMedHighPart=0;
    static float fFatLowPart=0, fFatMediumPart=0, fFatHighPart=0;


    //==============================================================================================
//==============================================================================================
//==============================================================================================
    public CLS_DATA()
    {
    }
    public void ClearRawData()
    {
        listRawData.clear();
    }
    //==============================================================================================
    public void SetMemberInfo(String _strName, String _strSex, String _strBirthday, String _strBloodType)
    {
        strName = _strName;
        strSex = _strSex;
        strBirthday = _strBirthday;
        strBloodType = _strBloodType;
    }
    public int Version(){return iVersion;}
    public void SetLanguage(int iVal){iLanguage = iVal;}
    public int iGetLanguage()
    {
        return iLanguage;
    }
    public void SetPersonalData(String _strName, String _strSex, String _strBirthday,
                                String _strBloodType, int _iGender)
    {
        strName = _strName;
        strSex = _strSex;
        strBirthday = _strBirthday;
        strBloodType = _strBloodType;
        iGender = _iGender;
    }
    public String strGetName(){return strName;}
    public String strGetBirthday(){return strSex;}
    public String strGetEmail(){return strBirthday;}
    public String strGetPhone(){return strBloodType;}
    public int  iGetGender(){return iGender;}
    public void SetScale(float _fScale){fScale = _fScale;}
    public float fGetScale(){return fScale;}
    public String strGetServerIP(){return strServerIP;}
    public void SendDataOk(boolean bVal){bSendDataOk = bVal;}
    public boolean bIsSendDataOk(){return bSendDataOk;}
    //==============================================================================================
    public void SetMAC(String _strMAC)
    {
        strMAC = _strMAC;
    }
    public String strGetMac(){return strMAC;}
    //==============================================================================================
    public void SetBrainData(int _iGoodSignal, int _iAtt , int _iMed,int _iFat, int _iDelta, int _iTheta,
                             int _iLowAlpha, int _iHighAlpha, int _iLowBeta, int _iHighBeta,
                             int _iLowGamma, int _iHighGamma)
    {
        double _dTheta = 0, _dDelta = 0, _dLowAlpha = 0, _dHighAlpha = 0, _dLowBeta = 0, _dHighBeta = 0;
        double _dLowGamma = 0, _dHighGamma = 0;
        iGoodSignal = _iGoodSignal;
        iAttention = _iAtt;
        iMeditation = _iMed;
        iFatigue = _iFat;
        iDelta = _iDelta;
        iTheta = _iTheta;
        iLowAlpha = _iLowAlpha;
        iHighAlpha = _iHighAlpha;
        iLowBeta = _iLowBeta;
        iHighBeta = _iHighBeta;
        iLowGamma = _iLowGamma;
        iHighGamma = _iHighGamma;

        dThetaSum += _iTheta;
        dLowAlphaSum += _iLowAlpha;
        dHighAlphaSum += _iHighAlpha;
        dLowBetaSum += _iLowBeta;
        dHighBetaSum += _iHighBeta;


        iFatigue = (double)((int)((((dLowAlphaSum+dHighAlphaSum) + dThetaSum)
                / (dLowBetaSum+dHighBetaSum))*1000))/1000;


        bfNeuroskyConnected = true;//有設定代表有收到腦波資料

        CLS_RAWDATA _raw = new CLS_RAWDATA();
        _raw.iGoodSignal = iGoodSignal;
        _raw.iTheta = iTheta;
        _raw.iDelta = iDelta;
        _raw.iLowAlpha = iLowAlpha;
        _raw.iHighAlpha = iHighAlpha;
        _raw.iLowBeta = iLowBeta;
        _raw.iHighBeta = iHighBeta;
        _raw.iLowGamma = iLowGamma;
        _raw.iHighGamma = iHighGamma;
        _raw.iAttention = iAttention;
        _raw.iMeditation = iMeditation;
        _raw.iFatigue = iFatigue;
        listRawData.add(_raw);
    }
    //==============================================================================================
    public String strGetSendingData()
    {
        String str =
                "lan" + iLanguage + "," +
                        "mac" + strMAC + "," +
                        "name" + strName + "," +
                        "sex" + strSex + "," +
                        "birthday" + strBirthday + "," +
                        "bloodtype" + strBloodType + "," +
                        "gender" + iGender + "," +
                        "theta" + (int)dThetaPercentage + "," +
                        "delta" + (int)dDeltaPercentage + "," +
                        //"alpha" + iAlphaPercentage + "," +
                        //"beta" + iBetaPercentage + "," +
                        //"gamma" + iGammaPercentage + "," +
                        "model" + iBWTestModel + "," +
                        "att" + (int)dAttentionAvg + "," +
                        "med" + (int)dMeditationAvg + "," +
                        "sleep" + iSleepQuality + "," +
                        "fatigue" + iFatigue + "," +
                        "energy" + iEnergy + "," +
                        "ending";
        return str;
    }
    //==============================================================================================
    public float fGetPart(int _iCmd, int _iPart)
    {
        float fAttSum=fAttLowPart+fAttMediumPart+fAttHighPart;
        float fMedSum=fMedLowPart+fMedMediumPart+fMedHighPart;
        float fFatSum=fFatLowPart+fFatMediumPart+fFatHighPart;

        switch(_iCmd)
        {
            case 1://att
                switch(_iPart)
                {
                    case 1://low
                        return (fAttLowPart/fAttSum)*100;
                    case 2://med
                        return (fAttMediumPart/fAttSum)*100;
                    case 3://high
                        return (fAttHighPart/fAttSum)*100;
                }
                break;
            case 2://med
                switch(_iPart)
                {
                    case 1://low
                        return (fMedLowPart/fMedSum)*100;
                    case 2://med
                        return (fMedMediumPart/fMedSum)*100;
                    case 3://high
                        return (fMedHighPart/fMedSum)*100;
                }
                break;
            case 3://fat
                switch(_iPart)
                {
                    case 1://low
                        return (fFatLowPart/fFatSum)*100;
                    case 2://med
                        return (fFatMediumPart/fFatSum)*100;
                    case 3://high
                        return (fFatHighPart/fFatSum)*100;
                }
                break;
        }
        return 0;
    }
    //==============================================================================================
    public void DoRawCalculation()
    {
        int iSize, ii;
        double _dTheta = 0, _dDelta = 0, _dLowAlpha = 0, _dHighAlpha = 0, _dLowBeta = 0, _dHighBeta = 0;
        double _dLowGamma = 0, _dHighGamma = 0;
        int _iAtt = 0;
        int _iMed = 0;
        double _iFat = 0;
        //int _iAlpha, _iBeta, _iGamma;

        dThetaSum = 0;
        dDeltaSum = 0;
        dLowAlphaSum = 0;
        dHighAlphaSum = 0;
        dLowBetaSum = 0;
        dHighBetaSum = 0;
        dLowGammaSum = 0;
        dHighGammaSum = 0;
        dAttentionSum = 0;
        dMeditationSum = 0;

        dThetaMax = 0;
        dDeltaMax = 0;
        dLowAlphaMax = 0;
        dHighAlphaMax = 0;
        dLowBetaMax = 0;
        dHighBetaMax = 0;
        dLowGammaMax = 0;
        dHighGammaMax = 0;
        dAttentionMax = 0;
        dMeditationMax = 0;




        CLS_RAWDATA _raw = new CLS_RAWDATA();
        _raw.iTheta = 0;
        _raw.iDelta = 0;
        listRawData.add(_raw);



        for (int kk = 0; kk < listRawData.size(); kk++) {
            if (listRawData.get(kk).iDelta == 0 ||
                    listRawData.get(kk).iTheta == 0 ||
                    listRawData.get(kk).iLowAlpha == 0 ||
                    listRawData.get(kk).iHighAlpha == 0 ||
                    listRawData.get(kk).iLowBeta == 0 ||
                    listRawData.get(kk).iHighBeta == 0 ||
                    listRawData.get(kk).iLowGamma == 0 ||
                    listRawData.get(kk).iHighGamma == 0 ||
                    listRawData.get(kk).iAttention == 0 ||
                    listRawData.get(kk).iMeditation == 0  ||
                    listRawData.get(kk).iFatigue == 0 )
            {
                listRawData.remove(kk);
            }
        }

        iSize = listRawData.size();
        if (iSize == 0)
            return;

        fAttLowPart = 0;
        fAttMediumPart = 0;
        fAttHighPart = 0;
        fMedLowPart = 0;
        fMedMediumPart = 0;
        fMedHighPart = 0;
        fFatLowPart = 0;
        fFatMediumPart = 0;
        fFatHighPart = 0;

        for (ii = 0; ii < iSize; ii++) {
            _dTheta = listRawData.get(ii).iTheta;
            _dDelta = listRawData.get(ii).iDelta;
            _dLowAlpha = listRawData.get(ii).iLowAlpha;
            _dHighAlpha = listRawData.get(ii).iHighAlpha;
            _dLowBeta = listRawData.get(ii).iLowBeta;
            _dHighBeta = listRawData.get(ii).iHighBeta;
            _dLowGamma = listRawData.get(ii).iLowGamma;
            _dHighGamma = listRawData.get(ii).iHighGamma;
            _iAtt = listRawData.get(ii).iAttention;
            _iMed = listRawData.get(ii).iMeditation;
            _iFat = listRawData.get(ii).iFatigue;

            if (_iAtt < 40)
                fAttLowPart++;
            else if (_iAtt > 60)
                fAttHighPart++;
            else
                fAttMediumPart++;

            if (_iMed < 40)
                fMedLowPart++;
            else if (_iMed > 60)
                fMedHighPart++;
            else
                fMedMediumPart++;

            if (_iFat < 40)
                fFatLowPart++;
            else if (_iFat > 60)
                fFatHighPart++;
            else
                fFatMediumPart++;


            dThetaSum += _dTheta;
            dDeltaSum += _dDelta;
            dLowAlphaSum += _dLowAlpha;
            dHighAlphaSum += _dHighAlpha;
            dLowBetaSum += _dLowBeta;
            dHighBetaSum += _dHighBeta;
            dLowGammaSum += _dLowGamma;
            dHighGammaSum += _dHighGamma;
            dAttentionSum += (double) _iAtt;
            dMeditationSum += (double) _iMed;

            if (_dTheta > dThetaMax)
                dThetaMax = _dTheta;
            if (_dDelta > dDeltaMax)
                dDeltaMax = _dDelta;

            if (_dLowAlpha > dLowAlphaMax)
                dLowAlphaMax = _dLowAlpha;
            if (_dHighAlpha > dHighAlphaMax)
                dHighAlphaMax = _dHighAlpha;

            if (_dLowBeta > dLowBetaMax)
                dLowBetaMax = _dLowBeta;
            if (_dHighBeta > dHighBetaMax)
                dHighBetaMax = _dHighBeta;

            if (_dLowGamma > dLowGammaMax)
                dLowGammaMax = _dLowGamma;
            if (_dHighGamma > dHighGammaMax)
                dHighGammaMax = _dHighGamma;

            if (_iAtt > dAttentionMax)
                dAttentionMax = _iAtt;

            if (_iMed > dMeditationMax)
                dMeditationMax = _iMed;
        }
        dThetaAvg = dThetaSum / (double) iSize;
        dDeltaAvg = dDeltaSum / (double) iSize;
        dLowAlphaAvg = dLowAlphaSum / (double) iSize;
        dHighAlphaAvg = dHighAlphaSum / (double) iSize;
        dLowBetaAvg = dLowBetaSum / (double) iSize;
        dHighBetaAvg = dHighBetaSum / (double) iSize;
        dLowGammaAvg = dLowGammaSum / (double) iSize;
        dHighGammaAvg = dHighGammaSum / (double) iSize;
        dAttentionAvg = dAttentionSum / (double) iSize;
        dMeditationAvg = dMeditationSum / (double) iSize;

        //算出標準差
        double _dSqSumTheta = 0, _dSqSumDelta = 0, _dSqSumLowAlpha = 0, _dSqSumHighAlpha = 0,
                _dSqSumLowBeta = 0, _dSqSumHighBeta = 0, _dSqSumLowGamma = 0, _dSqSumHighGamma = 0;
        double _dThetaSD, _dDeltaSD, _dLowAlphaSD, _dHighAlphaSD, _dLowBetaSD, _dHighBetaSD,
                _dLowGammaSD, _dHighGammaSD;
        for (ii = 0; ii < iSize; ii++) {
            _dTheta = listRawData.get(ii).iTheta;
            _dDelta = listRawData.get(ii).iDelta;
            _dLowAlpha = listRawData.get(ii).iLowAlpha;
            _dHighAlpha = listRawData.get(ii).iHighAlpha;
            _dLowBeta = listRawData.get(ii).iLowBeta;
            _dHighBeta = listRawData.get(ii).iHighBeta;
            _dLowGamma = listRawData.get(ii).iLowGamma;
            _dHighGamma = listRawData.get(ii).iHighGamma;
            /*
            _iAlpha = _iLowAlpha + _iHighAlpha;
            _iBeta = _iLowBeta + _iHighBeta;
            _iGamma = _iLowGamma + _iHighGamma;
            */

            _dSqSumTheta += (_dTheta - dThetaAvg) * (_dTheta - dThetaAvg);
            _dSqSumDelta += (_dDelta - dDeltaAvg) * (_dDelta - dDeltaAvg);
            _dSqSumLowAlpha += (_dLowAlpha - dLowAlphaAvg) * (_dLowAlpha - dLowAlphaAvg);
            _dSqSumLowBeta += (_dLowBeta - dLowBetaAvg) * (_dLowBeta - dLowBetaAvg);
            _dSqSumLowGamma += (_dLowGamma - dLowGammaAvg) * (_dLowGamma - dLowGammaAvg);
            _dSqSumHighAlpha += (_dHighAlpha - dHighAlphaAvg) * (_dHighAlpha - dHighAlphaAvg);
            _dSqSumHighBeta += (_dHighBeta - dHighBetaAvg) * (_dHighBeta - dHighBetaAvg);
            _dSqSumHighGamma += (_dHighGamma - dHighGammaAvg) * (_dHighGamma - dHighGammaAvg);

        }
        _dThetaSD = Math.sqrt(_dSqSumTheta / (double) iSize);
        _dDeltaSD = Math.sqrt(_dSqSumDelta / (double) iSize);
        _dLowAlphaSD = Math.sqrt(_dSqSumLowAlpha / (double) iSize);
        _dLowBetaSD = Math.sqrt(_dSqSumLowBeta / (double) iSize);
        _dLowGammaSD = Math.sqrt(_dSqSumLowGamma / (double) iSize);
        _dHighAlphaSD = Math.sqrt(_dSqSumHighAlpha / (double) iSize);
        _dHighBetaSD = Math.sqrt(_dSqSumHighBeta / (double) iSize);
        _dHighGammaSD = Math.sqrt(_dSqSumHighGamma / (double) iSize);

        //去掉超過標準差的，重新計算標準差範圍內的結果
        double _dThetaHigh = dThetaAvg + 1.5 * _dThetaSD;
        double _dDeltaHigh = dDeltaAvg + 1.5 * _dDeltaSD;
        double _dLowAlphaHigh = dLowAlphaAvg + 1.5 * _dLowAlphaSD;
        double _dHighAlphaHigh = dHighAlphaAvg + 1.5 * _dHighAlphaSD;
        double _dLowBetaHigh = dLowBetaAvg + 1.5 * _dLowBetaSD;
        double _dHighBetaHigh = dHighBetaAvg + 1.5 * _dHighBetaSD;
        double _dLowGammaHigh = dLowGammaAvg + 1.5 * _dLowGammaSD;
        double _dHighGammaHigh = dHighGammaAvg + 1.5 * _dHighGammaSD;

        int _iThetaSize = listRawData.size();
        int _iDeltaSize = listRawData.size();
        int _iLowAlphaSize = listRawData.size();
        int _iHighAlphaSize = listRawData.size();
        int _iLowBetaSize = listRawData.size();
        int _iHighBetaSize = listRawData.size();
        int _iLowGammaSize = listRawData.size();
        int _iHighGammaSize = listRawData.size();

        dThetaSum = 0;
        dDeltaSum = 0;
        dLowAlphaSum = 0;
        dLowBetaSum = 0;
        dLowGammaSum = 0;
        dHighAlphaSum = 0;
        dHighBetaSum = 0;
        dHighGammaSum = 0;

        for (ii = 0; ii < iSize; ii++) {
            _dTheta = listRawData.get(ii).iTheta;
            _dDelta = listRawData.get(ii).iDelta;
            _dLowAlpha = listRawData.get(ii).iLowAlpha;
            _dHighAlpha = listRawData.get(ii).iHighAlpha;
            _dLowBeta = listRawData.get(ii).iLowBeta;
            _dHighBeta = listRawData.get(ii).iHighBeta;
            _dLowGamma = listRawData.get(ii).iLowGamma;
            _dHighGamma = listRawData.get(ii).iHighGamma;
            //超過 1.5 個標準差的不要算進去
            if (_dTheta > _dThetaHigh)
                _iThetaSize--;
            else
                dThetaSum += (double) _dTheta;

            if (_dDelta > _dDeltaHigh)
                _iDeltaSize--;
            else
                dDeltaSum += (double) _dDelta;

            if (_dLowAlpha > _dLowAlphaHigh)
                _iLowAlphaSize--;
            else
                dLowAlphaSum += (double) _dLowAlpha;

            if (_dHighAlpha > _dHighAlphaHigh)
                _iHighAlphaSize--;
            else
                dHighAlphaSum += (double) _dHighAlpha;

            if (_dLowBeta > _dLowBetaHigh)
                _iLowBetaSize--;
            else
                dLowBetaSum += (double) _dLowBeta;

            if (_dHighBeta > _dHighBetaHigh)
                _iHighBetaSize--;
            else
                dHighBetaSum += (double) _dHighBeta;

            if (_dLowGamma > _dLowGammaHigh)
                _iLowGammaSize--;
            else
                dLowGammaSum += (double) _dLowGamma;

            if (_dHighGamma > _dHighGammaHigh)
                _iHighGammaSize--;
            else
                dHighGammaSum += (double) _dHighGamma;
        }

        if (dThetaSum <= 0 || _iThetaSize <= 0
                || dDeltaSum <= 0 || _iDeltaSize <= 0
                || dLowAlphaSum <= 0 || _iLowAlphaSize <= 0
                || dHighAlphaSum <= 0 || _iHighAlphaSize <= 0
                || dLowBetaSum <= 0 || _iLowBetaSize <= 0
                || dHighBetaSum <= 0 || _iHighBetaSize <= 0
                || dLowGammaSum <= 0 || _iLowGammaSize <= 0
                || dHighGammaSum <= 0 || _iHighGammaSize <= 0
        )
            return;


        dThetaAvg = dThetaSum / (double) _iThetaSize;
        dDeltaAvg = dDeltaSum / (double) _iDeltaSize;
        dLowAlphaAvg = dLowAlphaSum / (double) _iLowAlphaSize;
        dHighAlphaAvg = dHighAlphaSum / (double) _iHighAlphaSize;
        dLowBetaAvg = dLowBetaSum / (double) _iLowBetaSize;
        dHighBetaAvg = dHighBetaSum / (double) _iHighBetaSize;
        dLowGammaAvg = dLowGammaSum / (double) _iLowGammaSize;
        dHighGammaAvg = dHighGammaSum / (double) _iHighGammaSize;

//20200326,原先在手機計算 % 後傳到pc,改由pc計算後傳手機,避免傳輸中loss data,手機計算的跟電腦紀錄的有誤差

        if(_dThetaHigh==0 || _dDeltaHigh==0 ||_dLowAlphaHigh==0||_dLowBetaHigh==0||_dLowGammaHigh==0
                ||_dHighAlphaHigh==0||_dHighBetaHigh==0||_dHighGammaHigh==0)
            return;


        if(dThetaMax == 0)
            dThetaPercentage = 0;
        else
            dThetaPercentage = 100*dThetaAvg / _dThetaHigh;
        if(dDeltaMax == 0)
            dDeltaPercentage = 0;
        else
            dDeltaPercentage = 100*dDeltaAvg / _dDeltaHigh;
        if(dLowAlphaMax == 0)
            dLowAlphaPercentage = 0;
        else
            dLowAlphaPercentage = 100*dLowAlphaAvg / _dLowAlphaHigh;
        if(dLowBetaMax == 0)
            dLowBetaPercentage = 0;
        else
            dLowBetaPercentage = 100*dLowBetaAvg / _dLowBetaHigh;
        if(dLowGammaMax == 0)
            dLowGammaPercentage = 0;
        else
            dLowGammaPercentage = 100*dLowGammaAvg / _dLowGammaHigh;

        if(dHighAlphaMax == 0)
            dHighAlphaPercentage = 0;
        else
            dHighAlphaPercentage = 100*dHighAlphaAvg / _dHighAlphaHigh;
        if(dHighBetaMax == 0)
            dHighBetaPercentage = 0;
        else
            dHighBetaPercentage = 100*dHighBetaAvg / _dHighBetaHigh;
        if(dHighGammaMax == 0)
            dHighGammaPercentage = 0;
        else
            dHighGammaPercentage = 100*dHighGammaAvg / _dHighGammaHigh;



        /*
        if(iAttentionMax == 0)
            iAttentionPercentage = 0;
        else
            iAttentionPercentage = 100*iAttentionAvg / iAttentionMax;
        if(iMeditationMax == 0)
            iMeditationPercentage = 0;
        else
            iMeditationPercentage = 100*iMeditationAvg / iMeditationMax;
        */

        iSleepQuality = (int)((((dLowBetaPercentage+dHighBetaPercentage) / 2.0)
                + (dThetaPercentage / 20.0))
                / (((dLowAlphaPercentage+dHighAlphaPercentage) / 2.0)
                + (dDeltaPercentage / 20.0)) * 100.0);
        iSleepQuality = (200 - iSleepQuality)/2;
        if(iSleepQuality < 0)
            iSleepQuality = 0;
        iFatigue = (int)(((dLowAlphaPercentage+dHighAlphaPercentage) + dThetaPercentage)
                / (dLowBetaPercentage+dHighBetaPercentage) * 7.0);
    }

    //==============================================================================================
    public int iGetRawSize()
    {
        return listRawData.size();
    }

    public int iGetRaw(int iCmd, int iIdx)
    {
        if(iCmd == S.THETA)
            return listRawData.get(iIdx).iTheta;
        else if(iCmd == S.DELTA)
            return listRawData.get(iIdx).iDelta;

        else if(iCmd == S.LOWALPHA)
            return listRawData.get(iIdx).iLowAlpha;
        else if(iCmd == S.HIGHALPHA)
            return listRawData.get(iIdx).iHighAlpha;

        else if(iCmd == S.LOWBETA)
            return listRawData.get(iIdx).iLowBeta;
        else if(iCmd == S.HIGHBETA)
            return listRawData.get(iIdx).iHighBeta;

        else if(iCmd == S.LOWGAMMA)
            return listRawData.get(iIdx).iLowGamma;
        else if(iCmd == S.HIGHGAMMA)
            return listRawData.get(iIdx).iHighGamma;

        else if(iCmd == S.ATTENTION)
            return listRawData.get(iIdx).iAttention;
        else if(iCmd == S.MEDITATION)
            return listRawData.get(iIdx).iMeditation;
        else if(iCmd == S.FATIGUE)
            return listRawData.get(iIdx).iMeditation;

        return 0;
    }

    public int iGetRawAvg(int iCmd)
    {
        if(iCmd == S.THETA)
            return (int)dThetaAvg;
        else if(iCmd == S.DELTA)
            return (int)dDeltaAvg;

        else if(iCmd == S.LOWALPHA)
            return (int)dLowAlphaAvg;
        else if(iCmd == S.LOWBETA)
            return (int)dLowBetaAvg;
        else if(iCmd == S.LOWGAMMA)
            return (int)dLowGammaAvg;

        else if(iCmd == S.HIGHALPHA)
            return (int)dHighAlphaAvg;
        else if(iCmd == S.HIGHBETA)
            return (int)dHighBetaAvg;
        else if(iCmd == S.HIGHGAMMA)
            return (int)dHighGammaAvg;

        else if(iCmd == S.ATTENTION)
            return (int)dAttentionAvg;
        else if(iCmd == S.MEDITATION)
            return (int)dMeditationAvg;
        return 0;
    }

    public int iGetRawPercentage(int iCmd)
    {
        if(iCmd == S.THETA)
            return (int)dThetaPercentage;
        else if(iCmd == S.DELTA)
            return (int)dDeltaPercentage;

        else if(iCmd == S.LOWALPHA)
            return (int)dLowAlphaPercentage;
        else if(iCmd == S.LOWBETA)
            return (int)dLowBetaPercentage;
        else if(iCmd == S.LOWGAMMA)
            return (int)dLowGammaPercentage;

        else if(iCmd == S.HIGHALPHA)
            return (int)dHighAlphaPercentage;
        else if(iCmd == S.HIGHBETA)
            return (int)dHighBetaPercentage;
        else if(iCmd == S.HIGHGAMMA)
            return (int)dHighGammaPercentage;


        else if(iCmd == S.ATTENTION)
            return (int)dAttentionAvg;
        else if(iCmd == S.MEDITATION)
            return (int)dMeditationAvg;
        return 0;
    }
    //==============================================================================================
    public void ResetNeuroskyConnected() { bfNeuroskyConnected = false; }
    //==============================================================================================
    public int iGetGoodSignal(){return iGoodSignal;}
    public int iGetDelta(){return iDelta;}
    public int iGetTheta(){return iTheta;}
    public int iGetLowAlpha(){return iLowAlpha;}
    public int iGetHighAlpha(){return iHighAlpha;}
    public int iGetLowBeta(){return iLowBeta;}
    public int iGetHighBeta(){return iHighBeta;}
    public int iGetLowGamma(){return iLowGamma;}
    public int iGetHighGamma(){return iHighGamma;}
    public int iGetAttention(){return iAttention;}
    public int iGetMeditation(){return iMeditation;}
    public int iGetEnergy(){return iEnergy;}
    public double iGetFatigue(){return iFatigue;}
    public int iGetSleepQuality()
    {

        return iSleepQuality;
    }

    public boolean bTestReady(){return bfTestReady;}
    public void SetTestReady(boolean _bfBWTestReady){bfTestReady = _bfBWTestReady;}



    public void SetBWTestModel(int iVal){iBWTestModel = iVal;}
    public int iGetBWTestModel(){return iBWTestModel;}
    public void SetBVRTestModel(int iVal){iBVRTestModel = iVal;}
    public int iGetBVRTestModel(){return iBVRTestModel;}
    public void SetMaxVolume(Context _context, int iVal)
    {
        m_Context = _context;
        iMaxVomume = iVal;
        SetVolume(10);
    }
    public void SetVolume(int iVal)
    {
        float fVolume = 0;

        if(iVal > 100)
            iVolume = 100;
        else
            iVolume = iVal;
        fVolume = ((float)iVal / 100)*iMaxVomume;
        AudioManager m_AudioManager = (AudioManager) m_Context.getSystemService(Context.AUDIO_SERVICE);
        m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)fVolume, 0);
    }
    //==============================================================================================
    public void SetScreenWidthHeight(int _iWidth, int _iHeight)
    {
        iScreenWidth = _iWidth;
        iScreenHeight = _iHeight;
    }
    public int iGetScreenWidth(){return iScreenWidth;}
    public int iGetScreenHeight(){return iScreenHeight;}
    public int iGetScaleWidth(){return (int)(iScreenWidth*fScale);}
    public int iGetScaleHeight(){return (int)(iScreenHeight*fScale);}
    //==============================================================================================
    public void SetTrainingNum(int _iNum)
    {
        iTrainingNum = _iNum;
    }
    public int iGetTrainingNum()
    {
        return iTrainingNum;
    }
    //==============================================================================================
    public Bitmap TakeScreenShotFromView(View _view)
    {
        _view.setDrawingCacheEnabled(true);
        _view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        _view.buildDrawingCache();

        if(_view.getDrawingCache() == null)
            return null;

        bmpScreenshot = Bitmap.createBitmap(_view.getDrawingCache());
        //bmpScreenshot = Bitmap.createScaledBitmap(view.getDrawingCache(), 200, 400, true);//圖片縮成需要的尺寸

        _view.setDrawingCacheEnabled(false);
        _view.destroyDrawingCache();

        return bmpScreenshot;
    }
    //==============================================================================================
    public void CreatePdf(Context _context, Bitmap _bmpScreenShot)
    {
        PrintAttributes printAttributes = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.ISO_A4).
                setResolution(new PrintAttributes.Resolution("zooey", "test", 450, 700)).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();

        //PdfDocument document = new PrintedPdfDocument(this, printAttributes);
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1190, 1684, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);



        Canvas canvas = page.getCanvas();
        //Paint paint = new Paint();
        //paint.setColor(Color.RED);

        //button.setVisibility(View.INVISIBLE);
        //imageView.setImageResource(R.drawable.a);

        //canvas.drawCircle(0, 0, 30, paint);
        //Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        //Bitmap bmap = TakeScreenShotFromView(_layout);//在裡面抓完並且縮成想要的大小
        canvas.drawBitmap(_bmpScreenShot, 0, 0, null);


        //imageView.draw(canvas);
        //pieChart.draw(canvas);


        // finish the page
        document.finishPage(page);

        /*
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 2).create();//595,842(A4 portrait)
        page = document.startPage(pageInfo);

        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(0, 0, 100, paint);

        document.finishPage(page);
        */

        // write the document content
        try
        {
            File sdCard = null;
            File filedir = null;
            sdCard = Environment.getExternalStorageDirectory();
            filedir = new File(sdCard.getAbsolutePath() + "/download/MYEEG");
            filedir.mkdirs();
            File filepdf = new File(filedir, "myeeg.pdf");
            FileOutputStream output = new FileOutputStream(filepdf);


            document.writeTo(output);
            //Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(_context, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();



        PrintedPdfDocument d;
    }
    //==============================================================================================
    void WriteXLS(Context _context)
    {
        Workbook workbook = new HSSFWorkbook();
        Cell _cell = null;

        //New Sheet
        Sheet _sheetRaw = null;
        _sheetRaw = workbook.createSheet("Data");
        _sheetRaw.setColumnWidth(0, (8 * 500));
        _sheetRaw.setColumnWidth(1, (8 * 500));
        _sheetRaw.setColumnWidth(2, (8 * 500));
        _sheetRaw.setColumnWidth(3, (8 * 500));
        _sheetRaw.setColumnWidth(4, (8 * 500));
        _sheetRaw.setColumnWidth(5, (8 * 500));
        _sheetRaw.setColumnWidth(6, (8 * 500));
        _sheetRaw.setColumnWidth(7, (8 * 500));
        _sheetRaw.setColumnWidth(8, (8 * 500));
        _sheetRaw.setColumnWidth(9, (8 * 500));
        _sheetRaw.setColumnWidth(10, (8 * 500));

        Row _r1 = _sheetRaw.createRow(0);
        _cell = _r1.createCell(0);    _cell.setCellValue("姓名");   //c.setCellStyle(cs);
        _cell = _r1.createCell(1);    _cell.setCellValue(strName);    //c.setCellStyle(cs);
        Row _r2 = _sheetRaw.createRow(1);
        _cell = _r2.createCell(0);    _cell.setCellValue("生理性別");
        _cell = _r2.createCell(1);    _cell.setCellValue(strSex);
        Row _r3 = _sheetRaw.createRow(2);
        _cell = _r3.createCell(0);    _cell.setCellValue("生日");
        _cell = _r3.createCell(1);    _cell.setCellValue(strBirthday);
        Row _r4 = _sheetRaw.createRow(3);
        _cell = _r4.createCell(0);    _cell.setCellValue("血型");
        _cell = _r4.createCell(1);    _cell.setCellValue(strBloodType);

        Row _r6 = _sheetRaw.createRow(5);
        _cell = _r6.createCell(0);    _cell.setCellValue("ATTENTION(0-100)");
        _cell = _r6.createCell(1);    _cell.setCellValue("MEDITATION(0-100)");
        _cell = _r6.createCell(2);    _cell.setCellValue("FATIGUE(0-100)");
        _cell = _r6.createCell(3);    _cell.setCellValue("DELTA");
        _cell = _r6.createCell(4);    _cell.setCellValue("THETA");
        _cell = _r6.createCell(5);    _cell.setCellValue("LOW ALPHA");
        _cell = _r6.createCell(6);    _cell.setCellValue("HIGH ALPHA");
        _cell = _r6.createCell(7);    _cell.setCellValue("LOW BETA");
        _cell = _r6.createCell(8);    _cell.setCellValue("HIGH BETA");
        _cell = _r6.createCell(9);    _cell.setCellValue("LOW GAMMA");
        _cell = _r6.createCell(10);    _cell.setCellValue("HIGH GAMMA");

        int len = listRawData.size();
        Row[] _r = new Row[len+5];
        for(int ii=5; ii<len+5; ii++)
        {
            _r[ii] = _sheetRaw.createRow(ii+1);
            _cell = _r[ii].createCell(0);    _cell.setCellValue(listRawData.get(ii-5).iAttention);
            _cell = _r[ii].createCell(1);    _cell.setCellValue(listRawData.get(ii-5).iMeditation);
            _cell = _r[ii].createCell(2);    _cell.setCellValue(listRawData.get(ii-5).iFatigue);
            _cell = _r[ii].createCell(3);    _cell.setCellValue(listRawData.get(ii-5).iDelta);
            _cell = _r[ii].createCell(4);    _cell.setCellValue(listRawData.get(ii-5).iTheta);
            _cell = _r[ii].createCell(5);    _cell.setCellValue(listRawData.get(ii-5).iLowAlpha);
            _cell = _r[ii].createCell(6);    _cell.setCellValue(listRawData.get(ii-5).iHighAlpha);
            _cell = _r[ii].createCell(7);    _cell.setCellValue(listRawData.get(ii-5).iLowBeta);
            _cell = _r[ii].createCell(8);    _cell.setCellValue(listRawData.get(ii-5).iHighBeta);
            _cell = _r[ii].createCell(9);    _cell.setCellValue(listRawData.get(ii-5).iLowGamma);
            _cell = _r[ii].createCell(10);    _cell.setCellValue(listRawData.get(ii-5).iHighGamma);
        }


        /*
        Sheet sheet = workbook.createSheet("Employee");
        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        //CellStyle headerCellStyle = workbook.createCellStyle();
        //headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(Employee employee: employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getName());
            row.createCell(1).setCellValue(employee.getEmail());
            Cell dateOfBirthCell = row.createCell(2);
            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
            dateOfBirthCell.setCellStyle(dateCellStyle);
            row.createCell(3).setCellValue(employee.getSalary());
        }
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        */

        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            String strDateTime = dateFormat.format(curDate);

            //Android.OS.Environment.GetExternalStoragePublicDirectory(Android.OS.Environment.DirectoryDownloads)
            //File file = new File(Environment.getExternalStoragePublicDirectory(
            //  Environment.DIRECTORY_DOCUMENTS), filenameExternal);
            String strPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            strPath += "/SimpleEEG";
            //String strPath = Environment.getExternalStorageDirectory().getPath() + "/download/EEGApp";
            File filePath = new File(strPath);
            filePath.mkdirs();

            File fileExcel = new File(filePath+"/", strName+strDateTime+".xls");
            FileOutputStream output = new FileOutputStream(fileExcel);

            // Write the output to a file
            //FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
            workbook.write(output);
            output.close();
            // Closing the workbook
            workbook.close();


            //因為android的issue, 必須在對sd card寫完file後,要做這個才能在windows explorer上看到新的檔案
            CLS_MediaScanner sdfile = new CLS_MediaScanner(_context, strPath);
        }
        catch(Exception ex){
            String str = ex.getMessage().toString();
        }
    }

    //==============================================================================================
    static double dMetricsDensity = 2.0;
    static DisplayMetrics mDisplayMetrics;

    public void SetDspMetrics(Context _context)
    {
        //htc desire 728, fx=2.677 / fy=4.406
        mDisplayMetrics = _context.getResources().getDisplayMetrics();
        dMetricsDensity = mDisplayMetrics.density;
        float fx = mDisplayMetrics.widthPixels / mDisplayMetrics.xdpi;
        float fy = mDisplayMetrics.heightPixels / mDisplayMetrics.ydpi;
        float fx1 = fx/(float)2.677;
        float fy1 = fy/(float)4.406;
        if(fx1>fy1)
            fScale = fx1;
        else
            fScale = fy1;
        iScreenWidth = mDisplayMetrics.widthPixels;
        iScreenHeight = mDisplayMetrics.heightPixels;
    }
    public float fGetLineChartScale()
    {
        return (float)(mDisplayMetrics.density / 2.0) * fScale;
    }
//==============================================================================================

}
