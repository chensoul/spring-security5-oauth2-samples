![video_spider](https://socialify.git.ci/chensoul/spring-security-5-oauth2-samples/image?forks=1&issues=1&language=1&name=1&owner=1&stargazers=1&theme=Light)

# <font size="6p">spring-security-5-oauth2-samples</font> <font size="5p">  | [English Documentation](README.md)</font>

<p align="left">
 <a href="https://github.com/chensoul/spring-security-5-oauth2-samples/workflows/maven-build.yml"><img src="https://github.com/chensoul/spring-security-5-oauth2-samples/actions/workflows/maven-build.yml/badge.svg"></a>
 <a href="/pom.xml"><img src="https://img.shields.io/badge/Spring%20Boot%20Version-2.7.18-blue"></a>
 <a href="/pom.xml"><img src="https://img.shields.io/badge/Java%20Version-8-blue"></a>
	<a href="https://github.com/chensoul/spring-security-5-oauth2-samples/network/members"><img src="https://img.shields.io/github/forks/chensoul/spring-security-5-oauth2-samples?style=flat-square&logo=GitHub"></a>
	<a href="https://github.com/chensoul/spring-security-5-oauth2-samples/watchers"><img src="https://img.shields.io/github/watchers/chensoul/spring-security-5-oauth2-samples?style=flat-square&logo=GitHub"></a>
	<a href="https://github.com/chensoul/spring-security-5-oauth2-samples/issues"><img src="https://img.shields.io/github/issues/chensoul/spring-security-5-oauth2-samples.svg?style=flat-square&logo=GitHub"></a>
	<a href="https://github.com/chensoul/spring-security-5-oauth2-samples/blob/main/LICENSE"><img src="https://img.shields.io/github/license/chensoul/spring-security-5-oauth2-samples.svg?style=flat-square"></a>
</p>

使用 [Spring Security OAuth2 Boot](https://github.com/spring-attic/spring-security-oauth2-boot) 搭建 OAuth2
授权和认证服务。Spring Security OAuth2 Boot
在2022年5月31日归档，不再维护，推荐使用 [Spring Authorization Server](https://github.com/spring-projects/spring-authorization-server)
代替。

## 如何运行

### 生成证书

JRE 提供了一个简单的证书管理工具——keytool。它位于您的JRE_HOME\bin目录下。以下代码中的命令生成一个自签名证书并将其放入
PKCS12 KeyStore 中。除了 KeyStore 的类型之外，您还需要设置其有效期、别名以及文件名。在开始生成过程之前，keytool会要求您输入密码和一些其他信息，如下所示：

```bash
keytool -genkeypair -alias mytest -keyalg RSA -keysize 2048 \
    -storetype PKCS12 -keystore mytest.p12 -storepass mypass \
    -dname "CN=WebServer,OU=Unit,O=Organization,L=City,S=State,C=CN" -validity 3650
```

导出公钥文件：

```bash
keytool -list -rfc --keystore mytest.p12 -storepass mypass | \
    openssl x509 -inform pem -pubkey > public.key
```

导出私钥文件：

```bash
keytool -importkeystore -srckeystore mytest.p12 -srcstorepass mypass \
    -destkeystore private.p12 -deststoretype PKCS12 \
    -deststorepass mypass -destkeypass mytest

#输入 storepass 密码 
openssl pkcs12 -in private.p12 -nodes -nocerts -out private.key
```

## 贡献

非常欢迎[提出请求](https://help.github.com/articles/creating-a-pull-request) 。

## 许可

learn-spring-authorization-server 是在 [Apache 2.0 许可](https://www.apache.org/licenses/LICENSE-2.0.html)
下发布的开源软件 。
