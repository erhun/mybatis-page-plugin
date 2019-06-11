# mybatis-page-plugin
# 通过在调用时识别queryByPage中的返回参数类型来识别是否分页,调用更加简单

```
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("conf/spring-application.xml");

    Map<String, Object> params = new HashMap <> ();
    params.put("code", "test");
    params.put("pageNo", 1);
    params.put("pageSize", 3);

    TestDAO testDAO = applicationContext.getBean(TestDAO.class);

    PageResult pageResult = testDAO.queryByPage(params);

    System.out.println(pageResult.getMaxRecords());
```


