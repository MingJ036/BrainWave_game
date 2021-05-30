package com.sh.simpleeeg;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;

import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.EEGPower;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;

import java.util.Set;

/**
 * Created by user on 2017/12/28.
 */

public class CLS_BrainWave
{
    CLS_PARAM S = new CLS_PARAM();
    CLS_DATA clsData = new CLS_DATA();

    static Brainwave_Callback_Main m_Callback_Main;
    static Brainwave_Callback_Test m_Callback_Test;


    static TgStreamReader tgStreamReader;
    static BluetoothAdapter mBluetoothAdapter;
    static BluetoothDevice mBluetoothDevice;

    static private ClockThread1000 m_clockThread1000;
    static private Handler m_clockHandler = new Handler();

    static String strBrainwaveAddress = null;

    static int iGood = 0;
    static int iQuestion = 0;
    static int iDisagree = 0;

    static int  iGoodSignal = 0;
    static int  iAttention = 0;
    static int  iMeditation = 0;
    static int  iFatigue = 0;
    static int  iDelta = 0;
    static int  iTheta = 0;
    static int  iLowAlpha = 0;
    static int  iHighAlpha = 0;
    static int  iLowBeta = 0;
    static int  iHighBeta = 0;
    static int  iLowGamma = 0;
    static int  iHighGamma = 0;


    static private int iBadPacketCount = 0;
    static boolean bQuit = false;
    static boolean bDoConnect = true;

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    public CLS_BrainWave()
    {

    }
    //==============================================================================================
    public void Destroy()
    {
        bQuit = true;
        m_clockHandler.removeCallbacks(m_clockThread1000);


        if(tgStreamReader != null)
        {
            tgStreamReader.stop();
            tgStreamReader.close();
            tgStreamReader = null;
        }
    }
    //==============================================================================================
    private class ClockThread1000 extends Thread
    {
        @Override
        public void run()
        {
            if(bQuit)
                return;
            m_clockHandler.postDelayed(m_clockThread1000, 1000);
            if(bDoConnect)
            {
                if (tgStreamReader != null)
                    tgStreamReader.connectAndStart();
            }
        }
    }
    //==============================================================================================
    public interface Brainwave_Callback_Main
    {
        //public abstract
        void Do(int iCmd, int iVal);
    }

    public void SetCallback_Main(Brainwave_Callback_Main _callback)
    {
        this.m_Callback_Main = _callback;
    }
    //==============================================================================================
    public interface Brainwave_Callback_Test
    {
        //public abstract
        void Do(int iCmd, int iVal);
    }

    public void SetCallback_BWTest(Brainwave_Callback_Test _callback)
    {
        this.m_Callback_Test = _callback;
    }
    //==============================================================================================
    public int iConnect()
    {
        try
        {
            // TODO
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
            {
                //ShowToast("!! 請打開藍芽 !!");
                //m_Callback_Main.Do(S.BrainwaveDisconnected, 0);
                return -1;
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Log.i(TAG, "error:" + e.getMessage());
            if(m_Callback_Main != null)
                m_Callback_Main.Do(S.BrainwaveDisconnected, 0);
            return -101;
        }

        //BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> PairedDevices = mBluetoothAdapter.getBondedDevices();

        boolean bFindBraineaveDevice = false;
        String strBrainwaveName = null;

        //List<String> listPairedDevices = new ArrayList<String>();
        for(BluetoothDevice bt : PairedDevices)
        {
            strBrainwaveName = bt.getName();
            if(strBrainwaveName.contains("BrainLink") || strBrainwaveName.contains("Mindwave")
                    || strBrainwaveName.contains("MindWave"))
            {
                strBrainwaveAddress = bt.getAddress();
                clsData.SetMAC(strBrainwaveAddress);
                bFindBraineaveDevice = true;
                break;
            }
            //listPairedDevices.add(bt.getName());
            //listPairedDevices.add(bt.getAddress());
        }
        if(!bFindBraineaveDevice)
        {
            //ShowToast("!! 請先配對腦波儀 !!");
            //m_Callback_Main.Do(S.BrainwaveDisconnected, 0);
            return -2;
        }
        //就算腦波儀沒有開電，依然會往下走
        BluetoothDevice bd = mBluetoothAdapter.getRemoteDevice(strBrainwaveAddress);
        CreateStreamReader(bd);

        //tgStreamReader.connectAndStart();
        bQuit = false;
        bDoConnect = true;
        m_clockThread1000 = new ClockThread1000();
        m_clockHandler.post(m_clockThread1000);



        //m_Callback_Main.Do(S.BrainwavePaired, 0);
        return 1;
    }


    //==============================================================================================
    public TgStreamReader CreateStreamReader(BluetoothDevice bd)
    {
        if(tgStreamReader == null)
        {
            // TgStreamReader(BluetoothDevice mBluetoothDevice,TgStreamHandler tgStreamHandler)
            tgStreamReader = new TgStreamReader(bd, TgStreamHandlerCallback);
            //tgStreamReader.startLog();
        }
        else
        {
            // (1) Demo of changeBluetoothDevice
            tgStreamReader.changeBluetoothDevice(bd);

            // (4) Demo of setTgStreamHandler, you can change the data handler by this function
            tgStreamReader.setTgStreamHandler(TgStreamHandlerCallback);
        }
        return tgStreamReader;
    }
    //==============================================================================================
    private TgStreamHandler TgStreamHandlerCallback = new TgStreamHandler()
    {
        @Override
        public void onStatesChanged(int connectionStates)
        {
            //currentState  = connectionStates;
            switch (connectionStates)
            {
                case ConnectionStates.STATE_CONNECTED:
                    //sensor.start();
                    break;
                case ConnectionStates.STATE_WORKING:
                    //byte[] cmd = new byte[1];
                    //cmd[0] = 's';
                    //tgStreamReader.sendCommandtoDevice(cmd);
                    break;
                case ConnectionStates.STATE_GET_DATA_TIME_OUT:
                    break;
                case ConnectionStates.STATE_COMPLETE:
                    //read file complete
                    break;
                case ConnectionStates.STATE_STOPPED:
                    break;
                case ConnectionStates.STATE_DISCONNECTED:
                    break;
                case ConnectionStates.STATE_ERROR:
                    //Log.d(TAG,"Connect error, Please try again!");
                    break;
                case ConnectionStates.STATE_FAILED:
                    //Log.d(TAG,"Connect failed, Please try again!");
                    break;
            }
            Message msg = m_mindMessageHandler.obtainMessage();
            msg.what = S.MSG_UPDATE_STATE;
            msg.arg1 = connectionStates;
            m_mindMessageHandler.sendMessage(msg);


        }
        @Override
        public void onRecordFail(int a)
        {
            // TODO Auto-generated method stub
            //Log.e(TAG,"onRecordFail: " +a);
        }
        @Override
        public void onChecksumFail(byte[] payload, int length, int checksum)
        {
            // TODO Auto-generated method stub

            iBadPacketCount ++;
            Message msg = m_mindMessageHandler.obtainMessage();
            msg.what = S.MSG_UPDATE_BAD_PACKET;
            msg.arg1 = iBadPacketCount;
            m_mindMessageHandler.sendMessage(msg);

        }
        @Override
        public void onDataReceived(int datatype, int data, Object obj)
        {
            // TODO Auto-generated method stub
            Message msg = m_mindMessageHandler.obtainMessage();
            msg.what = datatype;
            msg.arg1 = data;
            msg.obj = obj;
            m_mindMessageHandler.sendMessage(msg);
        }
    };
    //==============================================================================================
    //private Handler LinkDetectedHandler = new Handler()
    Handler m_mindMessageHandler = new Handler()
    {
        String strSend = "";

        @Override
        public void handleMessage(Message msg)
        {
            int iDiff;

            super.handleMessage(msg);
            //String strValue = String.valueOf(msg.arg1);
            switch (msg.what)
            {
                case MindDataType.CODE_POOR_SIGNAL:
                    iGoodSignal = (200 - msg.arg1) / 2;
                    if(iGoodSignal > 90)
                    {
                        if(m_Callback_Main != null)
                            m_Callback_Main.Do(S.SIGNAL_GOOD, 0);
                    }
                    else
                    {
                        if(m_Callback_Main != null)
                            m_Callback_Main.Do(S.BrainwaveConnected, 0);
                    }
                    break;
                case MindDataType.CODE_ATTENTION:
                    iAttention = msg.arg1;
                    break;
                case MindDataType.CODE_MEDITATION://packet 最後面的資料是放鬆
                    iMeditation = msg.arg1;
                    /*clsData.SetBrainData(iGoodSignal, iAttention, iMeditation,iFatigue, iDelta, iTheta,
                            iLowAlpha, iHighAlpha, iLowBeta, iHighBeta, iLowGamma, iHighGamma);*/

                    if(clsData.bTestReady())
                        m_Callback_Test.Do(S.BrainwaveValue, 0);
                    break;
                case MindDataType.CODE_EEGPOWER:
                    EEGPower power = (EEGPower)msg.obj;
                    if(power.isValidate())
                    {
                        iDelta = power.delta;
                        iTheta = power.theta;
                        iLowAlpha = power.lowAlpha;
                        iHighAlpha = power.highAlpha;
                        iLowBeta = power.lowBeta;
                        iHighBeta = power.highBeta;
                        iLowGamma = power.lowGamma;
                        iHighGamma = power.middleGamma;
                    }
                    break;
            }
            //因為如果使用 S.xxx 在 用 switch case 會出現錯誤(android的bug)
            if(msg.what == S.MSG_UPDATE_STATE)
            {
                switch(msg.arg1)
                {
                    case ConnectionStates.STATE_CONNECTED:
                        if(m_Callback_Main != null)
                            m_Callback_Main.Do(S.BrainwaveConnected, 0);
                        bDoConnect = false;
                        break;
                    case ConnectionStates.STATE_WORKING:
                        //byte[] cmd = new byte[1];
                        //cmd[0] = 's';
                        //tgStreamReader.sendCommandtoDevice(cmd);
                        break;
                    case ConnectionStates.STATE_GET_DATA_TIME_OUT:
                        clsData.ResetNeuroskyConnected();
                        if(m_Callback_Main != null)
                            m_Callback_Main.Do(S.BrainwaveDisconnected, 0);
                        bDoConnect = true;
                        break;
                    case ConnectionStates.STATE_COMPLETE:
                        //read file complete
                        break;
                    case ConnectionStates.STATE_STOPPED:
                        clsData.ResetNeuroskyConnected();
                        if(m_Callback_Main != null)
                            m_Callback_Main.Do(S.BrainwaveDisconnected, 0);
                        bDoConnect = true;
                        break;
                    case ConnectionStates.STATE_DISCONNECTED:
                        clsData.ResetNeuroskyConnected();
                        if(m_Callback_Main != null)
                            m_Callback_Main.Do(S.BrainwaveDisconnected, 0);
                        bDoConnect = true;
                        break;
                    case ConnectionStates.STATE_ERROR:
                        //Log.d(TAG,"Connect error, Please try again!");
                        break;
                    case ConnectionStates.STATE_FAILED:
                        //Log.d(TAG,"Connect failed, Please try again!");
                        break;
                }
                //tv_connection.setText(""+msg.arg1);
            }
            else if(msg.what == S.MSG_UPDATE_BAD_PACKET)
            {
                //tv_badpacket.setText("" + msg.arg1);
            }
        }
    };
    //==============================================================================================
}
