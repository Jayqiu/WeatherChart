package com.threehalf.weatherchart;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private WeatherChartView weatherChartView;
    private ArrayList<DailyWeather> dailyWeathers=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader(this);
        weatherChartView=findViewById(R.id.view_weather);
        for (int i = 0; i < 10; i++) {
            DailyWeather dailyWeather= new DailyWeather();
            Random random = new Random();

            int h = random.nextInt(30)%(30-20+1) + 20;
            dailyWeather.setHigh(h);
            int  l= random.nextInt(20)%(20-10+1) + 10;
            dailyWeather.setLow(l);
            dailyWeather.setTextDay(i+"天");
            dailyWeather.setWeek("周"+i);
            dailyWeather.setDate("12:"+i);
            dailyWeather.setWindDirection("东北");
            dailyWeather.setWindScale(i+"");
            dailyWeather.setImg("http://bpic.588ku.com/element_origin_min_pic/00/85/94/0656ea39a9716c5.jpg");
            dailyWeathers.add(dailyWeather);
        }
        weatherChartView.setData(dailyWeathers);
    }


    public static void initImageLoader(Context mContext) {
        File cacheDir = new File(getImageCachePath(mContext));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(30 * 1024 * 1024)
                // 50 Mb
                .defaultDisplayImageOptions(
                        new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build())
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs()
                .discCache(new UnlimitedDiskCache(cacheDir)).build();
        com.nostra13.universalimageloader.utils.L.disableLogging();
        ImageLoader.getInstance().init(config);
    }
    public static String getImageCachePath(Context context) {

        String folderPath = "/sdcard"+File.separator +"weatherchart"
                + File.separator;
        return folderPath;
    }

}
