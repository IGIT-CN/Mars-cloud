<div align=center>
<img width="150px" src="https://github.com/yuyenews/resource/blob/master/mars/logo-small.png?raw=true"/>
</div>

<br/>

<div align=center>

<img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
<img src="https://img.shields.io/badge/jdk-1.8+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>

</div>

<br/>

<div align=center>

Mars-cloud is the official distributed middleware for Mars

</div>

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