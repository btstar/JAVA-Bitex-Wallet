# JAVA-DEMO
JAVA SPRING DEMO for join up UDUN-WALLET
Java接入网关的示例代码以及必要说明。

## 主要内容

[前置要求](#)

[商户号注册](#)

[使用说明](#)

[文档说明](#)

[demo目录结构](#)

### 前置要求
运行该项目，需要安装JDK，MySQL数据库。SQL文件在本项目中的src/main/resource/sql目录下。
另外还需要配置商户信息，如果您没有商户账户，那么您需要注册商户号。具体步骤见商户注册。

### 商户号注册
1、打开[udun官网](https://www.udun.io)，点击 登录/注册 完成注册。

2、等注册完成后按照官网提示完成 

3、接入开发需要 您配置 ：

    udun.demo.global.merchantId=您的商户号
    udun.demo.global.apiKey=apiKey=该商户的apikey  获取方式在官网

4、还需要配置我们网关的地址，需要获取请联系我们

    udun.demo.global.gatewayHost=http://gatewayhosts:port

### 使用说明

导入提供的SQL数据库，然后配置连接数据库的相关属性，最后通过java -jar命令直接启动即可。

### 文档说明

文档说明，在代码中有具体解释每一个步骤。请查看demo代码。

### demo目录结构
> 根目录：com.udun_demo
>>* api: 提供对外开放的接口，包括对接手机端的api以及接受网关回调的接口
>>* dao: 数据访问层
>>* server.job: 定时拉取网关的本地商户支持的所有币种，然后刷新本地币种
>>* service: 业务层封装
>>>> * wallet: 最核心的 访问网关的服务接口。
>>* support: 公共模块
>>> * common: 公共的异常、通用结果类
>>> * config: 配置类
>>> * constant: 常亮配置
>>> * dto: 在本系统内的数据传输类
>>> * enums: 枚举
>>> * utils: 工具类
>>> * vo: 视图层展示类