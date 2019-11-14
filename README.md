# mybatis-page-plugin

#### pom.xml
```
<dependency>
     <groupId>com.github.erhun</groupId>
     <artifactId>mybatis-page-plugin</artifactId>
     <version>1.0.0</version>
</dependency>
```
##### spring-application.xml

```
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="mapperLocations">
			<array>
				<value>classpath*:com/flurry/pageplugin/test/*DAO.xml</value>
			</array>
		</property>
		<property name="plugins">
			<array>
				<bean class="com.flurry.pageplugin.mybatis.PageInterceptor">
					<property name="properties">
						<value>
							default.page.size=11
							dialect=mysql
						</value>
					</property>
				</bean>
			</array>
		</property>
	</bean>
```
##### 调用代码

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


