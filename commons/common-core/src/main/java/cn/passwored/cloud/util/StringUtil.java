package cn.passwored.cloud.util;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project：eparty
 * Description：字符串判断工具
 * Date：2021/2/18 20:47
 * Author pandong
 */
@SuppressWarnings("unchecked")
public class StringUtil {

    /**
     * 判断是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null) || "".equals(str) || "null".equals(str);
    }

    /**
     * 从参数中获取字符串
     *
     * @param params
     * @param key
     * @return
     */
    public static String getString(Map params, String key) {
        return getString(params, key, "");
    }

    public static String getString(Map params, String key, String defaultVal) {
        if (params == null) {
            return defaultVal;
        }
        Object val = params.get(key);
        return val == null || "".equals(val.toString().trim()) ? defaultVal : val.toString();
    }

    /**
     * 从参数中获取int数字
     *
     * @param params
     * @param key
     * @return
     */
    public static int getInt(Map params, String key) {
        return getInt(params, key, 0);
    }

    public static int getInt(Map params, String key, int defaultVal) {
        if (params == null || params.get(key) == null || params.get(key).toString().length() == 0) {
            return defaultVal;
        }
        String curInt = params.get(key).toString();
        if (curInt.indexOf(".") >= 0) {
            curInt = curInt.substring(0, curInt.indexOf("."));
        }
        return Integer.parseInt(curInt);
    }

    /**
     * 从参数中获取Long数字
     *
     * @param params
     * @param key
     * @return
     */
    public static Long getLong(Map params, String key) {
        return getLong(params, key, 0L);
    }

    public static Long getLong(Map params, String key, Long defaultVal) {
        if (params == null || params.get(key) == null || params.get(key).toString().length() == 0) {
            return defaultVal;
        }
        String curInt = params.get(key).toString();
        if (curInt.indexOf(".") >= 0) {
            curInt = curInt.substring(0, curInt.indexOf("."));
        }
        return Long.parseLong(curInt);
    }

    /**
     * 从参数中获取double数字
     *
     * @param params
     * @param key
     * @return
     */
    public static double getDouble(Map params, String key) {
        return getDouble(params, key, 0.00);
    }

    public static double getDouble(Map params, String key, double defaultVal) {
        if (params == null || params.get(key) == null || params.get(key).toString().length() == 0) {
            return defaultVal;
        }
        return Double.parseDouble(params.get(key).toString());
    }

    /**
     * 精确格式化成金额格式(例如：1.199999 转成：1.2 )
     *
     * @param obj
     * @return
     */
    public static double formatMoney(double obj) {
        String obj1 = new BigDecimal(obj).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        double obj2 = new BigDecimal(obj1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return obj2;
    }

    /**
     * 判断是否是手机号码
     *
     * @param obj
     * @return
     */
    public static boolean isPhone(String obj) {
        if (obj == null) {
            return false;
        }
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(obj);
        return m.matches();
    }

    /**
     * 判断是否是固定电话号码
     *
     * @param obj
     * @return
     */
    public static boolean isTelPhone(String obj) {
        if (obj == null) {
            return false;
        }
        Pattern p = Pattern.compile("^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");
        Matcher m = p.matcher(obj);
        return m.matches();
    }

    /**
     * 判断是否是Email
     *
     * @param obj
     * @return
     */
    public static boolean isEmail(String obj) {
        if (obj == null) {
            return false;
        }
        Pattern p = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        Matcher m = p.matcher(obj);
        return m.matches();
    }

    /**
     * 判断是否为数字
     *
     * @param text
     * @return
     */
    public static boolean isNumeric(String text) {
        Pattern pattern = Pattern.compile("[+-]?\\d*[.]?\\d*");
        Matcher isNum = pattern.matcher(text);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为汉字
     *
     * @param text
     * @return
     */
    public static boolean isChinese(String text) {
        Pattern pat = Pattern.compile("[\u4e00-\u9fa5]*");
        Matcher matcher = pat.matcher(text);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 删除最后一个字符
     *
     * @param str
     * @return
     */
    public static String deleteLastChar(String str) {
        if (str.length() > 0) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 删除某个拼接字符中的一个值
     *
     * @param handldChar 要处理的拼接字符串
     * @param deleteChar 要删除的值
     * @param splitChar  拼接符号
     * @return
     */
    public static String deleteCharForSplit(String handldChar, String deleteChar, String splitChar) {
        if (isBlank(handldChar) || isBlank(deleteChar) || isBlank(splitChar)) {
            return handldChar;
        }
        List<String> resultList = new ArrayList<String>();
        String[] handleCharArr = handldChar.split(splitChar);
        for (String temp : handleCharArr) {
            if (temp.equals(deleteChar)) {
                continue;
            }
            resultList.add(temp);
        }

        if (resultList.size() == 0) {
            return "";
        }

        // 把List中的值拼接成字符串
        String result = "";
        for (String temp : resultList) {
            result += temp + splitChar;
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 得到参数长度的星号*
     *
     * @param length
     * @return
     */
    private static String getStarMark(int length) {
        String valueString = "";
        for (int i = 0; i < length; i++) {
            valueString += "*";
        }
        return valueString;
    }

    /**
     * 将单个参数值替换为多个参数值
     *
     * @param fieldVal
     * @return 1, 2, 3 --> '1','2','3'
     */
    public static String changeMultipleParam(String fieldVal) {
        if (isBlank(fieldVal)) {
            return "";
        }
        String[] vals = fieldVal.split(",");
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < vals.length; i++) {
            result.append("'" + vals[i] + "',");
        }
        result = result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static String changeMultipleParam(Map params, String field) {
        String fieldVal = getString(params, field);
        return changeMultipleParam(fieldVal);
    }

    /**
     * 如何含有.00和.0就直接取消，否则保留小数点后两位
     */
    public static String formatNumberString(Map params, String fieldName) {
        String number = getString(params, fieldName);
        return formatNumberString(number);
    }

    public static String formatNumberStringWithoutZero(Map params, String fieldName) {
        String number = formatNumberString(getString(params, fieldName));
        return "0".equals(number) ? "" : number;
    }

    public static String formatNumberStringWithZero(Map params, String fieldName) {
        String number = getString(params, fieldName);
        if ("".equals(number) || number.isEmpty()) {
            return "0";
        }
        return formatNumberString(number);
    }

    public static String formatNumberString(double fieldValue) {
        return formatNumberString(new Double(fieldValue).toString());
    }

    public static String formatNumberString(String fieldValue) {
        String number = fieldValue;
        if ("".equals(number) || number.isEmpty()) {
            return "";
        }
        if (number.indexOf(".") == -1) {
            return number;
        }
        if (number.endsWith(".00")) {
            return number.replace(".00", "");
        }

        if (number.endsWith(".0")) {
            return number.replace(".0", "");
        }

        return new DecimalFormat("0.00").format(Double.parseDouble(number));
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 获取随机字符串
     *
     * @param length 位数
     * @return
     */
    public static String getNonceStr(int length) {
        String symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new SecureRandom();
        char[] nonceChars = new char[length];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = symbols.charAt(random.nextInt(symbols.length()));
        }
        return new String(nonceChars);
    }

    public static String getNonceStr() {
        return getNonceStr(6);
    }
}
