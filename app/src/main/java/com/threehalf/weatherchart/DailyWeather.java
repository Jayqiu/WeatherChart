package com.threehalf.weatherchart;

import java.io.Serializable;

/**
 * @author jayqiu
 * @describe
 * @date 2016/11/25 18:44
 */
public class DailyWeather implements Serializable {

    /**
     * week : 周五
     * date : 11/25
     * high : 20
     * low : 14
     * windDirection : 东北
     * wind_scale : 3
     * text_day : 阵雨
     * code : 10
     * img :
     */

    private String week;
    private String date;
    private int high;
    private int low;
    private String windDirection;
    private String windScale;
    private String textDay;
    private String code;
    private String img;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public String getTextDay() {
        return textDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
