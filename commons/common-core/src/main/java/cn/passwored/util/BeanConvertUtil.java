package cn.passwored.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Project：eparty
 * Description：Bean和Object互相转换
 * Date：2021/2/18 20:47
 * Author pandong
 */
public class BeanConvertUtil {

    /**
     * Bean转Map
     *
     * @param obj Bean对象
     * @return Map对象
     */
    public static Map convertBeanToMap(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Method[] methods = obj.getClass().getDeclaredMethods();
        Map<String, Object> map = new HashMap<>(fields.length);
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            for (Method method : methods) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }
                Object fieldVal;
                try {
                    fieldVal = method.invoke(obj);
                } catch (Exception e) {
                    fieldVal = null;
                }
                map.put(fieldName, fieldVal);
            }
        }
        return map;
    }

    /**
     * Map转Bean
     *
     * @param map   Map对象
     * @param clazz 类
     * @param <T>   Bean描述
     * @return Bean对象
     */
    public static <T> T convertMapToBean(Map map, Class<T> clazz) {
        T obj = null;
        Method[] methods = clazz.getDeclaredMethods();
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
        if (map == null) {
            return null;
        }
        Set keys = map.keySet();
        for (Object key : keys) {
            Object val = map.get(key);
            String keyName = key.toString();
            // 如果存在下划线，必须转换成骆驼命名法
            if (keyName.contains("_")) {
                String keyNewName = "";
                String[] keySplits = keyName.split("_");
                for (String keySplit : keySplits) {
                    keyNewName += keySplit.substring(0, 1).toUpperCase() + keySplit.substring(1);
                }
                keyName = keyNewName;
            }

            try {
                // 获取该参数类型
                Field field = clazz.getDeclaredField(keyName.substring(0, 1).toLowerCase() + keyName.substring(1));
                if (field.getType().getName().equals("int")) {
                    val = new Integer(val.toString());
                }
                if (field.getType().getName().equals("java.lang.String")) {
                    val = val.toString();
                }
                // 获取方法名称
                String methodName = "set" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1);
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        method.invoke(obj, val);
                        break;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return convertObjectToT(obj, clazz);
    }

    /**
     * Map转Bean
     *
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> convertListToBean(List list, Class<T> clazz) {
        List<T> listNew = new ArrayList<>();
        for (Object obj : list) {
            T t;
            if (LinkedHashMap.class.isAssignableFrom(obj.getClass())) {
                t = BeanConvertUtil.convertMapToBean((Map) obj, clazz);
                listNew.add(t);
            } else {
                t = BeanConvertUtil.convertObjectToT(obj, clazz);
            }
            listNew.add(t);
        }
        return listNew;
    }

    public static <T> T convertObjectToT(Object obj, Class<T> clazz) {
        T t;
        try {
            t = clazz.cast(obj);
        } catch (ClassCastException e) {
            t = null;
        }
        return t;
    }
}
