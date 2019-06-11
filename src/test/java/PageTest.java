import com.flurry.pageplugin.test.TestDAO;
import com.flurry.pageplugin.mybatis.PageResult;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2019/6/6
 */
public class PageTest {

    @Test
    public void testPage() {


        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("conf/spring-application.xml");


        Map<String, Object> params = new HashMap <> ();
        params.put("code", "23456_1");
        params.put("pageNo", 2);
        params.put("pageSize", 3);

        TestDAO testDAO = applicationContext.getBean(TestDAO.class);

        PageResult pageResult = testDAO.queryByPage(params);

        System.out.println(pageResult);

    }
}
