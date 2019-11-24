# Mars-cloud

Mars-cloud 是 Mars-java 的官方分布式中间件

## 只需要换一个start

````
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>mars-cloud-start</artifactId>
    <version>最新版，可看官网文档目录《Mars介绍》</version>
</dependency>
````

## 添加5行配置

````
cloud:
  # 服务名称，同一个服务的负载均衡集群的name必须一致，不同集群之间必须唯一
  name: cloud-client1
  # 尽量长一点，防止接口过多来不及发布
  sessionTimeout: 10000
  # 是否作为网关
  gateWay: yes
  # 请求Mars-Cloud接口超时时间
  timeOut: 10000
  # zookeeper地址，多个地址用英文逗号分割
  # 多个地址，一定要加双引号，不然解析yml文件会出错
  register: 10.211.55.9:2180
````