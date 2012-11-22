# 实习项目 01

***账户注册、登陆系统***

使用Liftweb框架编写，纯JDBC完成数据库操作。数据库使用PostgreSQL 9.2 +。


## 功能

实现一个简单而完整的账户注册、登陆模块。包括如下功能：

 1 用户注册页面
 2 用户登陆页面
 3 用户管理页面 


## 数据字典

### 用户表(user)

 - id: String = "用户ID，使用此ID进行注册、登陆" @PK
 - username: String = "可选用户名"
 - email: String = "可选邮箱"
 - password: String = "密码，要求使用加密存储"

### 角色表(role)

系统暂时设置两种角色：user, admin。admin角色有权限添加、修改、删除账户。

 - id: String = "角色ID" @PK
 - comment: String = "角色注释"

### 用户角色表

 - userId: String = "用户ID" @FK user.id
 - roleId: String = "角色ID" @FK role.id

