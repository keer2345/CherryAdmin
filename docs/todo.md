| module   | controller          | route                  | description   | todo   | remark                |
|----------|---------------------|------------------------|---------------|--------|-----------------------|
| server   | CaptchaController   | /auth/code             | 获取验证码         | x      | 完善声明`@RateLimiter `   |
|          | AuthController      | /tenant/list           | 租户列表          | x      |                       |
|          |                     | /auth/login            | 登录            | x      |                       |
|          |                     | /auth/logout           | 登录            |        |                       |
| -------- | ------------------- | ---------------------- | ------------- | ------ | --------------------- |
| system   | SysUserController   | /system/user/getInfo   | 获取用户信息        |        |                       |
