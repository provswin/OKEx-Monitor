package cn.a4miles.okex_monitor.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期辅助类
 *
 * @author
 * @create 2018-05-15 上午10:16
 **/
public class OKDateUtil {
    public static String getCurrentDate(String format) {
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(day);
    }
}
