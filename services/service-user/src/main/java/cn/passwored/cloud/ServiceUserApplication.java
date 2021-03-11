package cn.passwored.cloud;

import cn.passwored.cloud.data.listener.BaseContextListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Project：cpc_build
 * Description：
 * Date：2021/2/20 14:20
 * Author wangke
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ServiceUserApplication.class);
        application.addListeners(new BaseContextListener());
        application.run(args);
    }
}
