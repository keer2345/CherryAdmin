package com.cherry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动程序
 *
 * @author keer
 * @date 2025-05-21
 */
@SpringBootApplication
//@EnableCaching
//@EnableTransactionManagement //开启注解方式的事务管理
//@EnableScheduling//开启定时任务增量同步
public class CherryApplication {
  public static void main(String[] args) {
//    SpringApplication application = new SpringApplication(CherryApplication.class);
//    application.setApplicationStartup(new BufferingApplicationStartup(2048));
//    application.run(args);
//    System.out.println("(♥◠‿◠)ﾉﾞ  Cherry Server 启动成功   ლ(´ڡ`ლ)ﾞ");
      SpringApplication.run(CherryApplication.class, args);
  }
}
