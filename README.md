# Mars-cloud

Mars-cloud is the official distributed middleware for Mars-java

## Just need to change one start

````
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>mars-cloud-start</artifactId>
    <version>[The latest version, you can see the document]</version>
</dependency>
````

## Add 5 rows configuration

````
cloud:
  # Service name, the name of the load balancing cluster of the same service must be the same, and must be unique between different clusters.
  name: cloud-client1
  sessionTimeout: 10000
  # Whether as a gateway
  gateWay: yes
  timeOut: 10000
  # Zookeeper address, multiple addresses are separated by commas, 
  # multiple addresses, must be double quotes, otherwise parsing yml files will be wrong
  register: 10.211.55.9:2180
````