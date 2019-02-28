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

## 5.店铺类型客流

### 5.1 历史数据对比图
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p5/history/total

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

### 5.2 按月查找客流量百分比
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p5/history/{year}/{month}

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

### 5.2 实时客流量百分比
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p5/realTime

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

## 6.品牌实时客流

### 6.1 实时客流
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p6/brand/{venue}/{name}

参数
```JOSN
venue
name
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

### 6.2 单月总客流
> 获取方式：GET
> 请求URL：http://115.159.194.51:9000/p6/monthflow/{year}/{month}/{venue}/{name}

参数
```JOSN
year
month
venue
name
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

### 6.3 单月按天的客流
> 获取方式：POST
> 请求URL：http://115.159.194.51:9000/p6/dayflow

参数
```JOSN
{
    bi_name: "mall_brand_static"
    dt_month: 0
    dt_year: 0
    name: ""
    venue: ""
}

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

## 7.品牌客流排行

### 7.1 今日客流最大商铺TOP20
> 获取方式：POST
> 请求URL：http://115.159.194.51:9000/p7/realtime

参数
```JOSN
{
    bi_name: "get_pingpai_20"
    dt_day: 0
    dt_month: 0
    dt_year: 0
    locname: ""
    type: ""
    venue: ""
}
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

### 7.2 按月最大客流商铺TOP20
> 获取方式：POST
> 请求URL：http://115.159.194.51:9000/p7/month

参数
```JOSN
{
    bi_name: "get_name_month_20"
    dt_month: 0
    dt_year: 0
    locname: ""
    type: ""
    venue: ""
}
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

## 8.品牌客流导向

### 8.1 按月品牌客流导向
> 获取方式：POST
> 请求URL：http://115.159.194.51:9000/p8/guiding

参数
```JOSN
{
    bi_name: "brand_link"
    dt_month: 0
    dt_year: 0
    name: ""
    venue: ""
}
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

## 9.品牌业态份额

### 9.1 实时业态份额
> 获取方式：POST
> 请求URL：http://115.159.194.51:9000/p9/realTime

参数
```JOSN
{
    bi_name: "brand_shop_prop"
    dt_day: 0
    dt_month: 0
    dt_year: 0
    mall_id: ""
    type: ""
    venue: ""
}
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

### 9.2 按月业态份额
> 获取方式：POST
> 请求URL：http://115.159.194.51:9000/p9/month

参数
```JOSN
{
    bi_name: "brand_shop_prop_month"
    dt_month: 0
    dt_year: 0
    mall_id: ""
    type: ""
    venue: ""
}
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

### 9.3 近13个月的业态份额曲线图
> 获取方式：POST
> 请求URL：http://115.159.194.51:9000/p9/total

参数
```JOSN
{
    name: ""
    type: ""
    venue: ""
}
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











