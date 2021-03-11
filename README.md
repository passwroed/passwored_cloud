# passwored_cloud
passwored自用的微服务框架

# 框架集成
| 集成  |版本|  完成 |
| ------------ | ------------ | ------------ |
| Spring Cloud Alibaba  |  2.2.5 |  ✔ |
| Spring Cloud Alibaba Sentinel  |  1.8.0 | ✔  |
| Spring Cloud Alibaba Nocas | 1.4.1  | ✔  |
| Spring Cloud Alibaba Dubbo | 2.7.8  | ✔  |
| Spring Cloud Alibaba Seata | 1.3.0  | ✔  |
| Spring Cloud Alibaba RocketMQ | 4.4.0  |   |
# 工程结构目录
> passwored_cloud
>> api(接口)
>>> 
> 
>> commons(共有)
>>> 
> 
>> servers(服务器)
>>> gateway-server(网关服务器)
> 
>> services(服务)
>>> oauth2-service(认证服务)

# 端口规划

| 服务  |  端口号 |
| ------------ | ------------ |
| sentinel  |  8858 |
| nacos | 8848  |
| seate  | 8091  |

# api端口规划
### 各微服务服务以9500开始
| 服务  |  端口号 |
| ------------ | ------------ |
| api-demo  |  8050 |
| nacos | 8848  |
| seate  | 8091  |