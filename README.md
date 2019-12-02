# Mars-cloud

Mars-cloud is the official distributed middleware for Mars

## Just need to change a start

````
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>mars-cloud-start</artifactId>
    <version>The latest version, see the official website document directory 《Mars Introduction》</version>
</dependency>
````

## Add 5 lines of configuration

````
cloud:
  # Service name, the name of the load balancing cluster of the same service must be the same, and it must be unique among different clusters
  name: cloud-client1
  # Make it as long
  sessionTimeout: 10000
  # Whether to act as a gateway
  gateWay: false
  # Request Mars-Cloud interface timeout
  timeOut: 10000
  # zookeeper address, multiple addresses are separated by commas
  # Multiple addresses, be sure to add double quotes, otherwise it will be wrong to parse the yml file
  register: 10.211.55.9:2180
````