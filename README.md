# blog

## demo
这里提供了一个简单的 [demo site](http://146.56.220.117)，可以查看blog应用的详细信息。

## mysql数据库
如果是第一次启动应用，请在数据库创建完毕后执行指令 `mvn flyway:migrate` 利用“数据迁移插件”来创建表和迁移一些基础数据，使得程序可以正确的运行。  

### mysql数据库连接信息
```
数据库名称: test
用户名: root
密码: fnst-3d3k
```

### mysql数据库docker启动命令
```
docker run -d -p 3306:3306 --name=mysql -e MYSQL_ROOT_PASSWORD=fnst-3d3k -e MYSQL_DATABASE=test mysql
```

## Redis
### Redis docker 启动命令
```
docker run -d -p 6379:6379 --name=redis redis
```

## Nginx
### Nginx docker 启动命令
```
docker run --restart=always --name=nginx -v `pwd`/nginx.conf:/etc/nginx/nginx.conf:ro -p 80:80 -d nginx 
```

## jenkins 

jenkins 控制台地址: [146.56.220.117:8081](http://146.56.220.117:8081)  

### 私有镜像仓库 registry docker 启动命令
```
docker run -d --name=registry --restart=always -p 5000:5000 -v /data/registry:/tmp/registry registry
```

### jenkins docker启动命令
```
docker run -d -u root --name=jenkins \
	--restart=always \
	-p 8081:8080 \
	-v /root/.ssh:/root/.ssh \
	-v `pwd`/jenkins-data:/var/jenkins_home \
	-v /var/run/docker.sock:/var/run/docker.sock:ro \
	-v $(which docker):/usr/bin/docker \
	jenkins/jenkins:lts
```
