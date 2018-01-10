package com.threehalf.weatherchart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.util.ArrayList;
import java.util.List;

/**
 * @author jayqiu
 * @describe
 * @date 2016/11/22 10:15
 */
public class WeatherChartView extends RelativeLayout {
    private static final  String TAG="WeatherChartView";
    protected float density;
    private int ITEM_SIZE = 10;// 10天
    private int itemWidth = 140; //每个Item的宽度
    private Paint linePaint, pointPaint, hightTempPaint;
    private Paint textPaint;// 文字
    private int mHeight, mWidth;
    private float weatherHeight = 0;
    private float timeHeight = 0;
    private float chartHeight = 0;
    private float maxTemp = 0f;// 温度最大
    private float minTemp = 0f;// 最小温度
    protected float scaledDensity;
    private  ArrayList<ImageView> imageViews= new ArrayList<>();
    private Path highTempPath = new Path();
    private List<DailyWeather> dailyWeathers = new ArrayList<>();
    private  LinearLayout mLinearLayout;
    public WeatherChartView(Context context) {
        this(context, null, 0);
    }

    public WeatherChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        setWillNotDraw(false);
        mLinearLayout= new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //
        density = context.getResources().getDisplayMetrics().density;
        scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int wWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wWidth;// 高
        itemWidth = wWidth / 6;// 每个Item的宽
        timeHeight = itemWidth;// 时间显示的宽度
        mWidth = itemWidth * ITEM_SIZE;// 总宽度
        weatherHeight = wWidth * 0.35f;// 天气显示的宽度
        chartHeight = mHeight - timeHeight - itemWidth - weatherHeight;

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                mHeight-  (int)weatherHeight+ itemWidth / 3);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLinearLayout.setGravity(Gravity.BOTTOM|Gravity.CENTER);
        mLinearLayout.setLayoutParams(params);
        addView(mLinearLayout);
        initPaint();
    }

    private void initPaint() {
        linePaint = new Paint();// 边框线
        linePaint.setColor(Color.parseColor("#1Affffff"));
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(2);
        //
        textPaint = new Paint();//
        textPaint.setColor(Color.parseColor("#ccffffff"));
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(sp2px(scaledDensity, 13));
        textPaint.setStrokeWidth(2);
        //
        pointPaint = new Paint();// 点
        pointPaint.setColor(Color.parseColor("#ffffff"));
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(2);
        //
        hightTempPaint = new Paint();//
        hightTempPaint.setColor(Color.parseColor("#ffffff"));
        hightTempPaint.setAntiAlias(true);
        hightTempPaint.setStyle(Paint.Style.STROKE);
        hightTempPaint.setTextSize(sp2px(scaledDensity, 14));
        hightTempPaint.setStrokeWidth(dp2px(density, 2));

    }

    /**
     * 设置数据显示
     *
     * @param dailyWeathers
     */
    public void setData(List<DailyWeather> dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
        if (!isEmpty(dailyWeathers)) {
            ITEM_SIZE = dailyWeathers.size();
            mWidth = itemWidth * ITEM_SIZE;// 总宽度
            maxTemp = dailyWeathers.get(0).getHigh();
            minTemp = dailyWeathers.get(0).getLow();
            for (DailyWeather dailyWeather : dailyWeathers) {

                if (dailyWeather.getHigh() > maxTemp) {
                    maxTemp = dailyWeather.getHigh();
                }
                if (dailyWeather.getLow() < minTemp) {
                    minTemp = dailyWeather.getLow();
                }

            }
            Log.e(TAG,"===maxTemp===" + maxTemp + "====minTemp===" + minTemp);
        } else {
            mWidth = itemWidth * ITEM_SIZE;// 总宽度
        }
        invalidate();
        try {
            mLinearLayout.removeAllViews();
        }catch (Exception e){

        }
        for (  int i = 0; i < ITEM_SIZE; i++) {
            if (!isEmpty(this.dailyWeathers)) {
                LinearLayout linearLayout= new LinearLayout(getContext());
                LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                        itemWidth,
                        mHeight-  (int) weatherHeight+ itemWidth / 3);
                linearLayout.setLayoutParams(linearLayoutParams);
                mLinearLayout.addView(linearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                DailyWeather dailyWeather = dailyWeathers.get(i);
            final    ImageView imageView=new ImageView(getContext());
                imageView.setId(1000+i);
                linearLayout.setGravity(Gravity.CENTER|Gravity.BOTTOM);
                linearLayout.addView(imageView,params);
                ImageLoader.getInstance().displayImage(dailyWeather.getImg(), imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        Log.e(TAG,"==============onSizeChanged===================");
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        Log.e(TAG,"==============onSizeChanged===================");
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        Log.e(TAG,"==============onSizeChanged===================");
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        Log.e(TAG,"==============onSizeChanged===================");
                    }
                });

            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG,"==============onSizeChanged===================");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        Log.e(TAG,"==============onMeasure===================");
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG,"==============onDraw===================");
        drawLine(canvas);
        drawText(canvas);
        drawCharLine(canvas);
        drawPoint(canvas);
    }

    private void drawLine( Canvas canvas) {// 画横竖线
        canvas.drawLine(0, 0, mWidth, 0, linePaint);// 上部横线
        for (int i = 1; i < ITEM_SIZE; i++) {
            canvas.drawLine(i * itemWidth, 0, i * itemWidth, mHeight, linePaint);// 上部横线
        }
    }
    private void drawText( Canvas canvas) {
        imageViews.clear();
        textPaint.setTextAlign(Paint.Align.CENTER);
        for (  int i = 0; i < ITEM_SIZE; i++) {
            if (!isEmpty(this.dailyWeathers)) {
                DailyWeather dailyWeather = dailyWeathers.get(i);
                textPaint.setColor(Color.parseColor("#ccffffff"));
                textPaint.setTextSize(sp2px(scaledDensity, 14));
                canvas.drawText(dailyWeather.getWeek(), i * itemWidth + itemWidth / 2, dp2px(density, 25), textPaint);
                textPaint.setTextSize(sp2px(scaledDensity, 10));
                canvas.drawText(dailyWeather.getDate(), i * itemWidth + itemWidth / 2,dp2px(density, 43), textPaint);
                //




//                Drawable drawable = ContextCompat.getDrawable(getContext(), img);
//                drawable.setBounds(i * itemWidth + itemWidth / 4, (int) (mHeight - weatherHeight), i * itemWidth + itemWidth / 4 * 3, (int) (mHeight - weatherHeight + itemWidth / 2));
//                drawable.draw(canvas);
                textPaint.setColor(Color.parseColor("#ffffff"));
                textPaint.setTextSize(sp2px(scaledDensity, 14));

                canvas.drawText(dailyWeather.getTextDay(), i * itemWidth + itemWidth / 2, mHeight - weatherHeight + itemWidth / 3 +dp2px(density, 20), textPaint);
                textPaint.setTextSize(sp2px(scaledDensity, 11));
                canvas.drawText(dailyWeather.getWindDirection(), i * itemWidth + itemWidth / 2, mHeight - weatherHeight + itemWidth / 3 + dp2px(density, 45), textPaint);
                canvas.drawText(dailyWeather.getWindScale() + "级", i * itemWidth + itemWidth / 2, mHeight - weatherHeight + itemWidth / 3 + dp2px(density, 65), textPaint);
            }
        }
//        removeAllViews();

    }


    private void drawPoint(Canvas canvas) {
        float tempDiff = maxTemp - minTemp;// 温差
        float h = chartHeight / tempDiff;// 每一个点所代表的高度
        //// 高温
        for (int i = 0; i < ITEM_SIZE; i++) {
            if (!isEmpty(this.dailyWeathers)) {
                DailyWeather dailyWeather = dailyWeathers.get(i);
                float tempH = (dailyWeather.getHigh() - minTemp) * h;
                float tempL = (dailyWeather.getLow() - minTemp) * h;
                canvas.drawCircle(i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempH, dp2px(density, 4), pointPaint);
                canvas.drawCircle(i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempL, dp2px(density, 4), pointPaint);
            }

        }
    }

    private void drawCharLine(Canvas canvas) {


        //
        float tempDiff = maxTemp - minTemp;// 温差
        float h = chartHeight / tempDiff;// 每一个点所代表的高度
        //// 高温
        for (int i = 0; i < ITEM_SIZE; i++) {
            if (!isEmpty(this.dailyWeathers)) {
                DailyWeather dailyWeather = dailyWeathers.get(i);

                float tempH = (dailyWeather.getHigh() - minTemp) * h;
                //


                if (i == 0) {
                    highTempPath.moveTo(i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempH);
                } else {
                    highTempPath.lineTo(i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempH);
                }
                // 天气数据
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(sp2px(scaledDensity, 14));

                canvas.drawText(dailyWeather.getHigh() + "°", i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempH - dp2px(density, 10), textPaint);


            }
        }
        hightTempPaint.setColor(Color.parseColor("#dc5d5d"));
        canvas.drawPath(highTempPath, hightTempPaint);
        highTempPath.reset();
        for (int i = 0; i < ITEM_SIZE; i++) {
            // 低温
            if (!isEmpty(this.dailyWeathers)) {
                DailyWeather dailyWeather = dailyWeathers.get(i);

                float tempL = (dailyWeather.getLow() - minTemp) * h;
//            canvas.drawCircle(i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempL, ChartUtils.dp2px(density, 4), pointPaint);
                if (i == 0) {
                    highTempPath.moveTo(i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempL);
                } else {
                    highTempPath.lineTo(i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempL);
                }
                // 天气数据
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(sp2px(scaledDensity, 14));
                canvas.drawText(dailyWeather.getLow() + "°", i * itemWidth + itemWidth / 2, mHeight - weatherHeight - itemWidth / 2 - tempL + dp2px(density, 20), textPaint);
            }
        }
        hightTempPaint.setColor(Color.parseColor("#95ba12"));
        canvas.drawPath(highTempPath, hightTempPaint);
        highTempPath.reset();
    }

    public static int dp2px(float density, int dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);

    }



    public static int sp2px(float scaledDensity, int sp) {
        if (sp == 0) {
            return 0;
        }
        return (int) (sp * scaledDensity + 0.5f);
    }


    public static boolean isEmpty(List<?> list) {
        return (list == null || list.size() == 0);
    }
}
