package cn.passwored.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Project：cpc_build
 * Description：
 * Date：2021/3/4 22:29
 * Author wangke
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiUsrApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiUsrApplication.class, args);
    }
}
