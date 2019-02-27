# 安达通商场客流监控系统API端重构
> Java编程语言

> 引入google guava缓存

> 提升用户体验

# fra_adt_spring_api



# 安达通API

>  Hadoop\Spark\Hive： 大数据

---
[TOC]
## 1.全场实时客流
### 1.1获取实时客流
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p1/today/rt_flow

参数
```JOSN
无
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```

### 1.2今日客流总数
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p1/today/flow_total

参数
```JOSN
无
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```

## 2.月度客流日历
### 2.1获取指定月份客流分布
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p2/month/{year}/{month}

参数
```JOSN
year
month
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```

### 2.2今日客流总数
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p2/month/total/{year}/{month}

参数
```JOSN
year
month
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```

## 3.楼层客流日历
### 3.1获取指定场馆、楼层的热力数据
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p3/heat/{venue}/{floor}

参数
```JOSN
venue
floor
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```

### 3.2 获取指定楼层今日客流
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p3/flow/{venue}/{floor}

参数
```JOSN
venue
floor
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```
## 4.业态客流份额
### 4.1实时业态客流份额
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p4/yetai/rt

参数
```JOSN
无
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```

### 4.2 指定日期业态客流份额
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p4/yetai/{year}/{month}

参数
```JOSN
year
month
```

返回值
```JSON
{
    code: 200
    count: 0
    data: []
    msg: "处理成功"
    msg_flag: "ok"
    page: 1
    pagesize: 500
    total_count: 0
    total_page: 0
}
```










