package cn.passwored.cloud.data.listener;

import cn.passwored.cloud.data.context.BaseContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Project：eparty
 * Description：基础监听
 * Date：2021/2/18 20:47
 * Author pandong
 */
public class BaseContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        BaseContext.setContext(event.getApplicationContext());
    }
}
