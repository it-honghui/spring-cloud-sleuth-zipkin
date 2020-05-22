# spring-cloud-sleuth-zipkin实现微服务的链路跟踪

## 简介
在微服务数量较多的系统架构中，一个完整的HTTP请求可能需要经过好几个微服务。如果想要跟踪一条完整的HTTP请求链路所产生的日志，我们需要到各个微服务上去查看日志并检索出我们需要的信息。随着业务发展，微服务的数量也会越来越多，这个过程也变得愈发困难。不过不用担心，`spring-cloud-sleuth-zipkin`为我们提供了分布式服务跟踪的解决方案。

`sleuth`: 为我们生成了请求链路信息，虽然我们已经可以通过Trace ID来跟踪整体请求链路了，但是我们还是得去各个系统中捞取日志。在并发较高得时候，日志是海量的，这个时候我们可以借助zipkin来代替我们完成日志获取与分析。

`zipkin`: 主要有四个组件，可以帮助我们收集由`sleuth`产生的请求链路信息，并保存到`MySQL`或者`Elasticsearch`中，并通过web页面分析与跟踪请求日志。这里提一句：`最新版本的zipkin还不支持6.0.0以上版本的Elasticsearch`。

1. Collector：收集器，负责收集日志信息，以供后续的存储，分析与展示；
2. Storage：存储模块，我们可以通过它将日志存储到MySQL或者Elasticsearch中。
3. RESTful API：API组件，它主要用来提供外部访问接口。 比如给客户端展示跟踪信息，或是外接系统访问以实现监控等；
4. WEB UI：通过web页面，我们可以轻松的分析与跟踪请求日志。

## 构建

在Spring Cloud为Finchley版本时，如果只需要默认的实现，则不需要自己构建Zipkin Server了，只需要下载jar即可，下面我基于Spring Cloud最新版`Hoxton.SR4`，Spring Boot采用`2.3.0`版本构建spring-cloud-sleuth-zipkin的demo项目。

zipkin下载地址：[戳我](https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/)，这里我下载的是目前最新版本的`zipkin-server-2.12.9-exec.jar`
![zipkin001](https://img.gathub.cn/halo/e800939f6ea61dcbb2af57875946fc6a.png)

zipkin整合mysql启动服务：
```bash
java -jar zipkin-server-2.12.9-exec.jar --STORAGE_TYPE=mysql --MYSQL_DB=zipkin --MYSQL_USER=root --MYSQL_PASS=123456 --MYSQL_HOST=localhost --MYSQL_TCP_PORT=3306
```
zip整合Elasticsearch启动服务：
```bash
java -jar zipkin-server-2.12.9-exec.jar --STORAGE_TYPE=elasticsearch --DES_HOSTS=http://localhost:9200
```

默认端口是9411，访问地址`http://localhost:9411/`
![zipkin002](https://img.gathub.cn/halo/6ea7909673d8081c3bf6ba753b17e864.png)

另外demo项目主要由`eureka注册中心`、`zuul服务网关`、`server-provider服务提供者`、`server-consumer服务消费者`、`zipkin-server`等几部分组成，如上图所示。

## 演示

server-provider服务提供者提供接口`UserController`，由server-consumer服务消费者在`TestController`中去调用。启动项目，请求接口：`http://localhost:8888/consumer/user`

**预期结果：**

请求路径：`zuul->server-consumer:TestController:/get/user->UserService->server-provider:UserController:/get`

**访问zipkin检验结果：**
![zipkin003](https://img.gathub.cn/halo/e9f7e75081b30be2e4f0355be61dacdc.png)

![zipkin004](https://img.gathub.cn/halo/d3c460725338105a575152cc416dbae5.png)

![zipkin005](https://img.gathub.cn/halo/2e483e2a29bea9bc461d441f9c458160.png)

**依赖分析：**
![zipkin006](https://img.gathub.cn/halo/e6046e9b7e82b386c66f9e552e0b9691.png)

## 总结

通过zipkin可以有效的帮助我们记录微服务中的请求链路、请求时间、微服务间的依赖关系等信息。尤其是针对多个微服务的项目，可以方便我们有效的理解服务间的调用关系，查错等。

## 演示项目地址

[戳我](https://github.com/it-wwh/spring-cloud-sleuth-zipkin)