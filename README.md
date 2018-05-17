# javafxDemo
## 概述
- 使用javafx8框架开发的桌面应用，支持多平台。
- JDK版本是Java 1.8。
- 用到了大部分javafx框架的技术，包括属性、绑定、监听等。
- 按照Java课程的作业要求一步一步完成的，可能部分结构设计的不合理，包括包装类的设计都与原来作业的实体类相关（在该javafx项目内并没有太大用处）。
## 功能介绍
- 可以显示添加、编辑和修改相关信息。
- 可以为每个成员设置私人的信息，即使用登录和注册功能。（不同账户成员可以使用共同的信息）
## 错误介绍
- 登录状态：在本程序中，登录信息和持久化文件路径保存在注册表中，默认登录转态为未登录。未登录状态无法保存文件。
- 登录及注册错误：会直接在登录和注册窗口显示。
- 日期格式错误：暂时没有设置验证器。