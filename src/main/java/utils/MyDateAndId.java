package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @description: 工具类
 * @author: HeyWeCome
 * @createDate: 2020/4/11 14:41
 * @version: 1.0
 */
public class MyDateAndId {
    public static String time_get(){
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return dt.format(new Date());
    }

    // 获取具体的时间
    public static String detailTime_get(){
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date);
    }

    // 获取一个随机的UUID
    public static String id_get(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    // 生成一个比较短的UUID
    public static String shortID_get(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,7);
    }
}
