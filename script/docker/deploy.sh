#!/bin/sh

# 使用说明，用来提示输入参数
usage() {
  echo "Usage: sh 执行脚本.sh [init|start|restart|stop|remove]"
  exit 1
}


# 初始化
init() {
  cd ../..
  mvn clean package
  cd -
}

# 启动程序模块（必须）
start() {
#  docker-compose up -d --build
  # docker-compose up -d ry-nacos
  docker-compose up -d cherry-monitor
  sleep 5
  docker-compose up -d cherry-snailjob
  # docker-compose up -d ry-gateway ry-system
}
restart() {
  stop
  start
}
rebuild() {
  stop
  init
  docker-compose up -d cherry-monitor
  sleep 5
  docker-compose up -d cherry-snailjob
}

# 关闭所有环境/模块
stop() {
  docker-compose down -v
}

# 删除所有环境/模块
remove() {
  stop
  docker rm cherry-snailjob cherry-monitor
  docker rmi cherry-snailjob:1.0.0 cherry-monitor:1.0.0
}

# 根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"init")
  init
  ;;
"start")
  start
  ;;
"restart")
  restart
  ;;
"rebuild")
  rebuild
  ;;
"stop")
  stop
  ;;
"remove")
  remove
  ;;
*)
  usage
  ;;
esac
