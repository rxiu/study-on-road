# study-on-road

**2019/3/25**

    整合spring security(无xml配置)
    
    在根目录执行mvn clean package -DMaven.test.skip=true
    解压target/trickle-security-1.0-release.zip
    双击starup.bat  访问http://localhost:9001

**2019/3/28**

    整合elasticsearch
    启动junit测试
    
**2019/04/02**

    spring boot + zuul网关（非spring cloud,无eureka）
    自定义zuulFilter
    动态加载路由
    ribbon负载均衡
    
    在根目录执行mvn clean package -DMaven.test.skip=true
    解压target/trickle-gateway-1.0-release.zip
    双击starup.bat  访问http://localhost:8090
    
    动态路由测试：实时修改conf/router.properties中路由信息，无需重启即可重新路由。
    负载测试：启动2个以上trickle-security项目（端口配置为9000-9004中，因为代码写死了），访问http://localhost:8090/login
 
**2019/04/02**

    使用flume 采集FTP数据到本地或者HDFS
    启动junit测试
    或者结合Java Service Wrapper测试
    jsw工具链接: https://pan.baidu.com/s/1Mg483tA0USYqFZ_bNV30tg 提取码: ejda
    
**2019/04/03**
    
    spring boot 整合 email
    junit测试
    
**2019/04/08**

    hibernate jdbc读写数据
    由于maven提供的sqlserver4依赖 不兼容sqlserver2000版
    在官网找了个兼容2000版及以上版本的sqlserver驱动
    驱动包在resources/lib下
    启动junit测试

**2019/04/08**

    zabbix api
    junit测试