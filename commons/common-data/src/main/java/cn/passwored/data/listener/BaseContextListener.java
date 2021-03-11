package cn.passwored.data.listener;

import com.qchtkj.common.data.context.BaseContext;
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
