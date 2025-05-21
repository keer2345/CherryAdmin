package org.cherry.monitor.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Admin 监控启动程序
 *
 * @author keer
 * @date 2025-05-21
 */
@SpringBootApplication
public class MonitorAdminApplication {
  public static void main(String[] args) {
    SpringApplication.run(MonitorAdminApplication.class, args);
    System.out.println("Monitor Admin 监控启动成功");
  }
}
