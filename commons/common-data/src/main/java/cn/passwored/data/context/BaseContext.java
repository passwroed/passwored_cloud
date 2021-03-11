package cn.passwored.data.context;

import org.springframework.context.ApplicationContext;

/**
 * Project：eparty
 * Description：基础上下文
 * Date：2021/2/18 20:47
 * Author pandong
 */
public class BaseContext {
    private static ApplicationContext context;

    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static Object getBean(String beanName) {
        if (context == null) {
            return null;
        }
        return context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> t) {
        if (context == null) {
            return null;
        }
        return context.getBean(t);
    }
}
