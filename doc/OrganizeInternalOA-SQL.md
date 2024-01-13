# OrganizeInternalOA 数据库设计

<div align=right>Author: 筱锋xiao_lfeng | Version: 1.1.0</div>

## 数据库设计

本数据库设计基于原有 [《数据库设计》](./数据库设计.md) 进行测试修改，进一步处理数据库结构与增加可维护性。

> 数据库所有字段均待定，后续可能会对某些字段进行删除，或者新增一些字段，尽量做成可维护的接口
>
> 所有的表之间，关联性尽量不要做的那么强，适当解耦，不然可维护性不高

另外，为保证数据库的完整性，对数据库适当位置加以外键约束，若后续需要进行维护删除表等操作，可删除外键约束后操作。

使用数据库系统 `Mysql8`



## 建表准备

总建表语句在 /mysql/[organize_oa.sql](../mysql/organize_oa.sql) 下存放。**（重要：在执行导入中，请先看下面内容）**

若需要导入数据库中，请先操作数据库进入对应数据库进行操作。无需执行表导入

```mysql
USE organize_oa;
```

### 数据库、用户名和密码

> 为了保证业务后期可拓展性及充分利用 mysql 默认数据库中数据，请参考下面内容。

另外，由于各位开发者配置环境为本地环境，非测试环境。故数据库部署并非统一，避免在 `application.yml` 文件中对 `spring.datasource` 频繁修改（操作git）带来不必要的麻烦，建议开发者建数据库时，数据库名字为 `organize_oa` 。单独开设账号 `organize_oa` 密码 `123456`

```mysql
-- 数据库及用户建表语句（执行后创建 organize_oa 数据库与同名 organize_oa 用户（密码：123456）
CREATE USER 'organize_oa'@'%' IDENTIFIED WITH caching_sha2_password BY '123456';GRANT USAGE ON *.* TO 'organize_oa'@'%';ALTER USER 'organize_oa'@'%' REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;CREATE DATABASE IF NOT EXISTS `organize_oa`;GRANT ALL PRIVILEGES ON `organize_oa`.* TO 'organize_oa'@'%';
```

application.yml 配置文件如下：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306
    username: organize_oa
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

> 注意，url部分中，没有使用 `jdbc:mysql://localhost:3306/organize_oa` ，在 mysql 中存在默认数据库 `information_schema` 该部分可以提取出该数据库（含 organize_oa 数据库）的信息，以及可以支持同账户下多张数据表，对后期若有需要可以调整使用。

### url差别对于mapper的使用区别

在习惯使用 `jdbc:mysql://localhost:3306/organize_oa` 下，Mapper中数据库语法的描写较为简单。

```java
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM oa_user WHERE username = #{username}")
    UserDO getUserByUsername(String username);
}
```

若修改为 `jdbc:mysql://localhost:3306` 之后，操作数据库都需要加上数据库名字，告知 **DBMS** 使用哪一个数据库后再选择数据表

```java
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM organize_oa.oa_user WHERE username = #{username}")
    UserDO getUserByUsername(String username);
}
```

> 为何这样使用。
>
> 上述这样操作可以保证此服务对应一个数据库的用户，不使用 root 用户。使用 root 用户是一个很危险的事情，分出专门一个用户处理并且专门的用户中对应 `information_schema` 也有自己及名下相关的数据内容方便利用。除此之外，可以进行业务拓展。**若后期需要额外的开发要求，需要多个数据库，而不是单个数据库对应多个数据表的时候。**无需进行Mapper内繁琐代码修改（替换），只需在编写新的业务Mapper代码写入新的数据表名称即可。

```java
// 举个例子
@Mapper
public interface GeneralMapper {
    @Select("SELECT * FROM organize_oa.oa_user WHERE username = #{username}")
    UserDO getUserByUsername(String username);
    
    @Select("SELECT * FROM organize_log.oa_logs WHERE id = #{id}")
    LogDO getLogById(Long id);
}
```



## organize_oa 数据库设计

![image-20240113005049859](./assets/image-20240113005049859.png)

### oa_user 数据表

#### 说明

> 【用户表】存放本项目所有用户数据

#### 数据表字段属性

| 序号 | 名称 | 描述 | 类型 | 键 | 为空 | 额外 | 默认值 |
| :--: | :--: | :--: | :--: | :--: | :--: | :--: | :--: |
| 1 | `id` | 主键 | bigint unsigned | PRI | NO | auto_increment |  |
| 2 | `job_id` | 工作ID：正则表达 "^[STU\|TEA\|OTH][0-9]{7}" | char(10) | UNI | NO |  |  |
| 3 | `username` | 用户名 | varchar(40) | UNI | NO |  |  |
| 4 | `password` | 密码 | varchar(255) |  | NO |  |  |
| 5 | `address` | 用户家庭地址 | varchar(255) |  | NO |  |  |
| 6 | `phone` | 电话 | varchar(11) | UNI | NO |  |  |
| 7 | `email` | 邮箱 | varchar(100) | UNI | NO |  |  |
| 8 | `age` | 年龄 | tinyint unsigned |  | NO |  |  |
| 9 | `signature` | 一句话描述自己 | varchar(50) |  | YES |  |  |
| 10 | `sex` | 0/1/2:保密/男/女 | tinyint unsigned |  | NO |  | 0 |
| 11 | `avatar` | 头像地址 | text |  | YES |  |  |
| 12 | `nickname` | 昵称 | varchar(20) |  | YES |  |  |
| 13 | `enabled` | 账户是否可用 | tinyint(1) |  | NO |  | 1 |
| 14 | `account_no_expired` | 账户是否过期 | tinyint(1) |  | NO |  | 1 |
| 15 | `credentials_no_expired` | 密码是否过期 | tinyint(1) |  | NO |  | 0 |
| 16 | `recommend` | 账户是否被推荐 | tinyint(1) |  | NO |  | 0 |
| 17 | `account_no_locked` | 账户是否被锁定 | tinyint(1) |  | NO |  |  |
| 18 | `description` | 个人简介 | text |  | YES |  |  |
| 19 | `created_at` | 创建时间 | timestamp |  | NO | DEFAULT_GENERATED | CURRENT_TIMESTAMP |
| 20 | `updated_at` | 更新时间 | timestamp |  | YES |  |  |



### oa_role 数据表

#### 说明

#### 数据表字段属性

| 序号 | 名称 | 描述 | 类型 | 键 | 为空 | 额外 | 默认值 |
| :--: | :--: | :--: | :--: | :--: | :--: | :--: | :--: |
| 1 | `id` | 角色id | int unsigned | PRI | NO | auto_increment |  |
| 2 | `role_name` | 角色名称 | varchar(20) |  | NO |  |  |



### oa_permissions 数据表

#### 说明

#### 数据表字段属性

| 序号 |     名称     |            描述            |      类型       |  键  | 为空 |      额外      | 默认值 |
| :--: | :----------: | :------------------------: | :-------------: | :--: | :--: | :------------: | :----: |
|  1   |     `id`     |            主键            | bigint unsigned | PRI  |  NO  | auto_increment |        |
|  2   |    `pid`     |          权限父id          | bigint unsigned | MUL  | YES  |                |        |
|  3   |    `name`    |          权限名称          |  varchar(100)   |      |  NO  |                |        |
|  4   |    `code`    |          权限编码          |   varchar(50)   |      |  NO  |                |        |
|  5   |    `type`    |      0为菜单，1为权限      |   tinyint(1)    |      |  NO  |                |   1    |
|  6   | `deleted_at` | 删除时间(没有删除应当为空) |    timestamp    |      | YES  |                |        |



### oa_role_user 数据表

| 序号 | 名称  |  描述  |      类型       |  键  | 为空 | 额外 | 默认值 |
| :--: | :---: | :----: | :-------------: | :--: | :--: | :--: | :----: |
|  1   | `uid` | 用户id | bigint unsigned | PRI  |  NO  |      |        |
|  2   | `rid` | 角色id |  int unsigned   | MUL  |  NO  |      |        |



### oa_role_permissions 数据表

| 序号 | 名称  |  描述  |      类型       |  键  | 为空 | 额外 | 默认值 |
| :--: | :---: | :----: | :-------------: | :--: | :--: | :--: | :----: |
|  1   | `rid` | 角色id |  int unsigned   | PRI  |  NO  |      |        |
|  2   | `pid` | 权限id | bigint unsigned | MUL  |  NO  |      |        |



### oa_project 数据表

| 序号 |        名称        |           描述           |       类型       |  键  | 为空 |      额外      | 默认值 |
| :--: | :----------------: | :----------------------: | :--------------: | :--: | :--: | :------------: | :----: |
|  1   |        `id`        |          项目id          | bigint unsigned  | PRI  |  NO  | auto_increment |        |
|  2   |       `name`       |         项目名称         |   varchar(255)   |      |  NO  |                |        |
|  3   |   `description`    |        一句话描述        |   varchar(255)   |      |  NO  |                |        |
|  4   |   `introduction`   |       项目详细介绍       |       text       |      |  NO  |                |        |
|  5   |    `code_open`     |       代码是否开放       |    tinyint(1)    |      |  NO  |                |   0    |
|  6   |    `core_code`     | 核心代码内容（Markdown） |       text       |      | YES  |                |        |
|  7   |       `git`        |     git代码仓库内容      |       json       |      | YES  |                |        |
|  8   | `difficulty_level` |         难度等级         | tinyint unsigned |      |  NO  |                |   1    |
|  9   |       `type`       |           类型           |   int unsigned   | MUL  |  NO  |                |        |
|  10  |      `reward`      |           报酬           |      double      |      | YES  |                |        |
|  11  |      `status`      |           状态           | tinyint unsigned |      |  NO  |                |   0    |



### oa_project_type 数据表

| 序号 |     名称     |    描述    |     类型     |  键  | 为空 |       额外        |      默认值       |
| :--: | :----------: | :--------: | :----------: | :--: | :--: | :---------------: | :---------------: |
|  1   |     `id`     | 项目类型id | int unsigned | PRI  |  NO  |  auto_increment   |                   |
|  2   |    `name`    |  类型名字  | varchar(50)  |      |  NO  |                   |                   |
|  3   | `created_at` |  创建时间  |  timestamp   |      |  NO  | DEFAULT_GENERATED | CURRENT_TIMESTAMP |
|  4   | `updated_at` |  修改时间  |  timestamp   |      | YES  |                   |                   |



### oa_user_project 数据表

| 序号 |     名称     |       描述       |      类型       |  键  | 为空 |       额外        |      默认值       |
| :--: | :----------: | :--------------: | :-------------: | :--: | :--: | :---------------: | :---------------: |
|  1   |     `id`     |      主键id      | bigint unsigned | PRI  |  NO  |  auto_increment   |                   |
|  2   |    `uid`     |      用户id      | bigint unsigned | MUL  |  NO  |                   |                   |
|  3   |    `pid`     | 接到分割项目内容 | bigint unsigned |      |  NO  |                   |                   |
|  4   | `created_at` |     创建时间     |    timestamp    |      |  NO  | DEFAULT_GENERATED | CURRENT_TIMESTAMP |
|  5   | `updated_at` |     修改时间     |    timestamp    |      | YES  |                   |                   |



### oa_config 数据表

| 序号 |     名称     |    描述    |      类型       |  键  | 为空 |       额外        |      默认值       |
| :--: | :----------: | :--------: | :-------------: | :--: | :--: | :---------------: | :---------------: |
|  1   |     `id`     |    主键    | bigint unsigned | PRI  |  NO  |  auto_increment   |                   |
|  2   |   `value`    | 调用关键字 |   varchar(50)   | UNI  |  NO  |                   |                   |
|  3   |    `data`    |  json数据  |      json       |      | YES  |                   |                   |
|  4   | `created_at` |  创建时间  |    timestamp    |      |  NO  | DEFAULT_GENERATED | CURRENT_TIMESTAMP |
|  5   | `updated_at` |  修改时间  |    timestamp    |      | YES  |                   |                   |
