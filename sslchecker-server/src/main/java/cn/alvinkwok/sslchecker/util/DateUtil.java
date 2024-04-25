package cn.alvinkwok.sslchecker.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description
 * 时间工具类
 *
 * @author alvinkwok
 * @since 2024/4/26
 */
public final class DateUtil {
    private DateUtil() {
    }

    /**
     * 将时间格式化为yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    public static String formatNormalTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
