package cn.passwored.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project：eparty
 * Description：驼峰和下划线相互转换
 * Date：2021/2/18 20:47
 * Author pandong
 */
public class HumpUtil {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰[String]
     *
     * @param key
     * @return
     */
    public static String lineToHump(String key) {
        if (key.indexOf("_") != -1) {
            //转驼峰
            String keyLow = key.toLowerCase();
            Matcher matcher = linePattern.matcher(keyLow);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
        return key;
    }

    /**
     * 下划线转驼峰[Map]
     *
     * @param params
     * @return
     */
    public static Map lineToHump(Map<String, Object> params) {
        Map resultMap = new HashMap();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String newKey = lineToHump(entry.getKey());
            //微信昵称解码输出
            if ("customerNickName".equals(newKey)) {
                try {
                    resultMap.put(newKey, URLDecoder.decode(String.valueOf(entry.getValue()), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                continue;
            }
            resultMap.put(newKey, entry.getValue());
        }
        return resultMap;
    }

    /**
     * 下划线转驼峰[List]
     *
     * @param data
     * @return
     */
    public static List lineToHump(List<Map> data) {
        List<Map> resultList = new ArrayList<>();
        for (Map map : data) {
            resultList.add(lineToHump(map));
        }
        return resultList;
    }

    /**
     * 驼峰转下划线[String]
     *
     * @param key
     * @return
     */
    public static String humpToLine(String key) {
        Matcher matcher = humpPattern.matcher(key);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线[Map]
     *
     * @param params
     * @return
     */
    public static Map humpToLine(Map<String, Object> params) {
        Map resultMap = new HashMap();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String newKey = humpToLine(entry.getKey());
            resultMap.put(newKey, entry.getValue());
        }
        return resultMap;
    }

    /**
     * 驼峰转下划线[List]
     *
     * @param data
     * @return
     */
    public static List humpToLine(List<Map> data) {
        List<Map> resultList = new ArrayList<>();
        for (Map map : data) {
            resultList.add(humpToLine(map));
        }
        return resultList;
    }

}
