package com.thinkd.xshare.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.thinkd.xshare.R;


/**
 * 扫描的动画效果
 * 自定义RadarView
 */
public class RadarScanView extends View {

    private static final int MSG_RUN = 1;

    private int mCircleColor = Color.BLACK;
    private int mLineColor = Color.BLACK;
    private int mArcColor = Color.WHITE;
    private int mArcStartColor = Color.WHITE;
    private int mArcEndColor = Color.TRANSPARENT;

    private Paint mCirclePaint; // 绘制圆形画笔
    private Paint mArcPaint; // 绘制扇形画笔
    private Paint mLinePaint; // 绘制线条画笔

    private RectF mRectF;

    private int mSweep; // 扇形角度

    public RadarScanView(Context context) {
        this(context, null);
    }

    public RadarScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public RadarScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    /**
     * 初始化
     */
    private void init(Context context){
        mCircleColor = context.getResources().getColor(R.color.color_989898);
        mArcColor = context.getResources().getColor(R.color.color_989898);
        mLineColor = context.getResources().getColor(R.color.color_989898);

        mArcStartColor = context.getResources().getColor(android.R.color.transparent);
        mArcEndColor = context.getResources().getColor(R.color.color_989898);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(1.f);

        mArcPaint.setColor(mArcColor);
        mArcPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(1.f);

        mRectF = new RectF();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = getMeasuredWidth();
        //确定当前view的大小
        setMeasuredDimension(size, size);
        mRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());

        //着色器
        mArcPaint.setShader(new SweepGradient(size / 2, size / 2, mArcEndColor,mArcStartColor));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;

        canvas.save();
        canvas.rotate(mSweep, centerX, centerY);
        canvas.drawArc(mRectF, 0, mSweep, true, mArcPaint);
        //合并之前的绘制
        canvas.restore();

        //直线 横线，竖线
        canvas.drawLine(0, centerY, getMeasuredWidth(), centerY, mLinePaint);
        canvas.drawLine(centerX, 0, centerX, getMeasuredHeight(), mLinePaint);

        //圆,圆心，半径，画笔
        canvas.drawCircle(centerX, centerY, centerX / 2, mCirclePaint);
        canvas.drawCircle(centerX, centerY, centerX, mCirclePaint);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_RUN) {

                mSweep-=3;
//                if(mSweep > 15){
//                    mSweep = 0;
//                }
                postInvalidate();
                sendEmptyMessageDelayed(MSG_RUN, 25);
            }
        }
    };

    /**
     * 对外公开扫描的方法
     */
    public void startScan(){
        if(mHandler != null){
            mHandler.obtainMessage(MSG_RUN).sendToTarget();
        }
    }
}
