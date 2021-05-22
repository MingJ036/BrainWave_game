package com.sh.simpleeeg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by user on 2018/8/28.
 */

public class CLS_BruceIV
{
    final int NONE = 0;
    final int DRAG = 1;
    final int ZOOM = 2;

    int iMode = NONE;
    PointF pntStart = new PointF();
    PointF pntMid = new PointF();
    float fOldDist = 1f;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    long lPrevTouchTime = 0, lNewTouchTime=0;
    float fDownX=0, fUpX=0;
    boolean bFitCenter = true;

    int iScreenWidth = 1;//896;
    int iScreenHeight = 1;//504;
    float fScale = 1;
    ImageView m_iv;
    int iResourceID;
    Context m_context;
    float fOrgZoomWidth, fOrgZoomHeight;

    Canvas canvas;
    Bitmap _bitmapSrc, bitmapDest;
    //==============================================================================================
    public CLS_BruceIV(Context _context, ImageView _iv)
    {
        m_context = _context;
        m_iv = _iv;

        //GetOriginalWidthHeight();
        SetTouchListener();
    }
    public void Clear()
    {
        m_iv.setImageBitmap(null);
        bitmapDest.recycle();
        bitmapDest = null;
        System.gc();
    }
    //==============================================================================================
    //void SetBitmap(Context _context, ImageView _iv, int _iResoruceID, int _iWidth, int _iHeight)
    void SetBitmap(int _iResoruceID)
    {


        _bitmapSrc = decodeSampledBitmapFromResource(m_context.getResources(),
                _iResoruceID, (int)fOrgZoomWidth, (int)fOrgZoomHeight);
        bitmapDest = Bitmap.createBitmap(
                _bitmapSrc.getWidth(), _bitmapSrc.getHeight(),_bitmapSrc.getConfig());
        canvas = new Canvas(bitmapDest);
        canvas.drawBitmap(_bitmapSrc, 0, 0, null);
        m_iv.setImageBitmap(bitmapDest);
        _bitmapSrc.recycle();
        _bitmapSrc = null;
        System.gc();
    }
    //==============================================================================================
    public void GetOriginalWidthHeight(float _fWidth, float _fHeight)
    {
        fOrgZoomWidth = _fWidth;
        fOrgZoomHeight = _fHeight;
        /*
        fOrgZoomWidth = m_iv.getResources().getDisplayMetrics().widthPixels;
        fOrgZoomHeight = m_iv.getResources().getDisplayMetrics().heightPixels;
        */
        /*
        m_iv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        fOrgZoomWidth = m_iv.getMeasuredWidth();
                        fOrgZoomHeight = m_iv.getMeasuredHeight();
                    }
                });
        */
/*
        m_iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            public boolean onPreDraw()
            {
                m_iv.getViewTreeObserver().removeOnPreDrawListener(this);
                fOrgZoomWidth = m_iv.getMeasuredWidth();
                fOrgZoomHeight = m_iv.getMeasuredHeight();
                return true;
            }
        });
        */
    }
    //==============================================================================================
    void SetTouchListener()
    {
        m_iv.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View _view, MotionEvent _event)
            {
                ImageView view = (ImageView) _view;

                // Handle touch events here...
                switch (_event.getAction() & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_DOWN:
                        fDownX = _event.getX();

                        lNewTouchTime = System.currentTimeMillis();
                        if((lNewTouchTime - lPrevTouchTime) < 400)
                        {
                            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            bFitCenter = true;
                        }
                        else
                        {
                            view.setScaleType(ImageView.ScaleType.MATRIX);

                        }
                        lPrevTouchTime = lNewTouchTime;

                        matrix.set(view.getImageMatrix());
                        savedMatrix.set(matrix);
                        pntStart.set(_event.getX(), _event.getY());
                        iMode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        bFitCenter = false;

                        lPrevTouchTime = 0;
                        fOldDist = spacing(_event);
                        if (fOldDist > 10f)//2指按下的距離，代表是有按下2個指頭
                        {
                            savedMatrix.set(matrix);
                            midPoint(pntMid, _event);
                            iMode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        fUpX = _event.getX();
                        if(bFitCenter)
                        {
                            if ((fUpX - fDownX) > 8)
                            {
                                /*
                                if(iPage == 2)
                                {
                                    iPage = 1;
                                    GotoPage(1);
                                    textView_Page.setText("1");
                                }
                                */
                            }
                            else if ((fUpX - fDownX) < -8)
                            {
                                /*
                                if(iPage == 1)
                                {
                                    iPage = 2;
                                    GotoPage(2);
                                    textView_Page.setText("2");
                                }
                                */
                            }
                        }

                        iMode = NONE;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if(bFitCenter)
                            break;
                        float[] fMatrixValues = new float[9];
                        float fx, fy, fMatrixX = 0, fMatrixY = 0, fWidth = 0, fHeight = 0;
                        if (iMode == DRAG)
                        {
                            matrix.set(savedMatrix);
                            matrix.getValues(fMatrixValues);
                            fMatrixX = fMatrixValues[2];
                            fMatrixY = fMatrixValues[5];
                            fWidth = fMatrixValues[0] * (((ImageView) view).getDrawable()
                                    .getIntrinsicWidth());
                            fHeight = fMatrixValues[4] * (((ImageView) view).getDrawable()
                                    .getIntrinsicHeight());
                            fx = _event.getX() - pntStart.x;
                            fy = _event.getY() - pntStart.y;
                            //if image will go outside left bound
                            if((fMatrixX + fx) >= 0)
                                fx = -fMatrixX;
                                //if image will go outside right bound
                            else if((fMatrixX + fx + fWidth) <= view.getWidth())
                                fx = view.getWidth() - fMatrixX - fWidth;
                            //if image will go oustside top bound
                            if (fMatrixY + fy >= 0)
                                fy = -fMatrixY;
                                //if image will go outside bottom bound
                            else if((fMatrixY + fy + fHeight) <= view.getHeight())
                                fy = view.getHeight() - fMatrixY - fHeight;
                            matrix.postTranslate(fx, fy);
                        }
                        else if (iMode == ZOOM)
                        {
                            //>>> 讀取imageview現在的寬高度
                            matrix.getValues(fMatrixValues);
                            fWidth = fMatrixValues[0] * (((ImageView) view).getDrawable()
                                    .getIntrinsicWidth());
                            fHeight = fMatrixValues[4] * (((ImageView) view).getDrawable()
                                    .getIntrinsicHeight());
                            //<<<
                            float fNewDist = spacing(_event);
                            if (fNewDist > 10f)
                            {
                                matrix.set(savedMatrix);
                                float fScale = fNewDist / fOldDist;
                                //>>> 判斷imageview現在的寬高如果小於原有的,就不准縮小
                                if((fWidth < fOrgZoomWidth || fHeight < fOrgZoomHeight)
                                        && (fScale < 1.0))
                                {
                                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    break;
                                }
                                //<<<
                                matrix.postScale(fScale, fScale, pntMid.x, pntMid.y);
                            }
                        }

                        break;

                }
                view.setImageMatrix(matrix);
                return true;
            }
        });
    }
    //==============================================================================================
    public static int calculateInSampleSize(BitmapFactory.Options _options, int _iReqWidth, int _iReqHeight)
    {
        //參考 https://developer.android.com/topic/performance/graphics/load-bitmap.html
        // Raw height and width of image
        final int height = _options.outHeight;
        final int width = _options.outWidth;
        int inSampleSize = 1;

        if (height > _iReqHeight || width > _iReqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= _iReqHeight && (halfWidth / inSampleSize) >= _iReqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    //==============================================================================================
    public static Bitmap decodeSampledBitmapFromResource(Resources _res, int _iResId,
                                                         int _iReqWidth, int _iReqHeight)
    {
        //參考 https://developer.android.com/topic/performance/graphics/load-bitmap.html
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options _options = new BitmapFactory.Options();
        _options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(_res, _iResId, _options);

        // Calculate inSampleSize
        _options.inSampleSize = calculateInSampleSize(_options, _iReqWidth, _iReqHeight);

        // Decode bitmap with inSampleSize set
        _options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(_res, _iResId, _options);
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
    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //==============================================================================================
    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    //==============================================================================================
}
