package cn.a4miles.okex_monitor.util;

import java.text.SimpleDateFormat;

/**
 * Created by huhuaxiang on 2018/5/15.
 */

public class OKTimeUtil {
    public static final String PATTERN_YMDHMS_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YMDHMS_2 = "yyyy/MM/dd HH:mm:ss";
    public static final String PATTERN_YMDHM_1 = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_YMDHM_2 = "yy/MM/dd HH:mm";
    public static final String PATTERN_YMD_1 = "yyyy/MM/dd";

    /**
     * 根据时间戳获取时间
     *
     * @return
     */
    public static String getStrDateFromLong(long time, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
