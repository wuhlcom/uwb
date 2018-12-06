1. gateway可以支持java和非java的服务
2. web修改路由信息地址:
- 192.168.10.232:11007/index.html
- 账户和密码可能是:
- zhilutec/zhilu@2016
- zhilutec/123456  
- 要修改密码可以调用接口来修改账户密码
4. 我们目前只使用了Path类型的路由断言，路由过滤未使用
5. 而实际是可以根据需要适配支持10种路由断言（是指路由匹配的判断规则）和20种路由过滤（路由的限流，重写，重定向，特定字段修改删除等功能），
6. 路由断言和路由过滤可以相互组合,多个路由过滤条件也可以组合使用
7. 要支持其它类型的路由断言和路由过滤需要对代码做相应的适配
8. 路由全部在redis中保存
9. 注意
 - spring boot 2.0中redis的配置与spring 1.5有些不同
 - 目前发现只有以下依赖的版本才能使用

spring boot 使用2.0.3.RELEASE,spring cloud  Finchley.RELEASE
```
<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--声明spring boot依赖-->
            <dependency>
                <!-- Import dependency management from Spring Boot -->
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.0.3.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
分开写
```
 <parent>
        <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.0.3.RELEASE</version>
         <relativePath/> <!-- lookup parent from repository -->
 </parent>
```
```
<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>        
</dependencyManagement>
```
       
       


