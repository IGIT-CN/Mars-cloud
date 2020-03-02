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

```
public class DemoConfig extends MarsCloudConfig {
	@Override
	public CloudConfig getCloudConfig() {
		CloudConfig cloudConfig = new CloudConfig();
		// Service name, the name of the load balancing cluster of the same service must be the same, and it must be unique among different clusters
		cloudConfig.setName("");
		// Make it as long
		cloudConfig.setSessionTimeout(10000L);
		// Request Mars-Cloud interface timeout
		cloudConfig.setTimeOut(10000L);
		// Whether to act as a gateway
		cloudConfig.setGateWay(false);
		// zookeeper address, multiple addresses are separated by commas
		cloudConfig.setRegister("");
		// Load balancing policy (only supports polling for the time being, random two)
		cloudConfig.setStrategy(Strategy.POLLING);
		return cloudConfig;
	}
}
```
