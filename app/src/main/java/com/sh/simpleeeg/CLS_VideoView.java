package com.sh.simpleeeg;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;


public class CLS_VideoView extends VideoView
{
    CLS_DATA cls_data = new CLS_DATA();

    public CLS_VideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CLS_VideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CLS_VideoView(Context context)
    {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        //MyVideoView放在layout裡面,已經會自動按照影片的比例去切換,可是如果是小的vdieo
        // 會往左靠,沒辦法放在正中間,用改尺寸把videoView改成跟影片同尺寸,
        // 就會放在中間
        // layoutCenterInParent 記得要打勾
        if(cls_data.iGetBWTestModel() == 3)
            width = (int)(height / 2.8);
        setMeasuredDimension(width, height);
    }
}
