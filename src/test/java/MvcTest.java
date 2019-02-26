import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ssm.config.RootConfig;
import ssm.config.SsmWebInitializer;
import ssm.dao.DepartmentMapper;
import ssm.dao.EmployeeMapper;
import ssm.pojo.Department;
import ssm.pojo.Employee;

import java.util.List;
import java.util.UUID;

/**
 * @author 肥宅快乐码
 * @date 2019/2/23 - 15:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {SsmWebInitializer.class,RootConfig.class})
@PropertySource("classpath:/ssm/config/app.properties")
public class MvcTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @Autowired
    SqlSession sqlSession;

    /**
     *
     */
    @Autowired
    DepartmentMapper departmentMapper;


    /**
     *
     */
    @Autowired
    EmployeeMapper employeeMapper;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testPage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn","1")).andReturn();
        MockHttpServletRequest request = result.getRequest();
        PageInfo pi = (PageInfo) request.getAttribute("pageInfo");
        Employee e = (Employee)pi.getList().get(1);
        System.out.println(e.getDepartment().getDeptName());
        System.out.println("当前页码" + pi.getPageNum());
        System.out.println("总页码" + pi.getPages());
        System.out.println("总记录" + pi.getTotal());
        System.out.println("在页码需要连续显示的页码");
        int[] nums = pi.getNavigatepageNums();
        for (int i:
            nums ) {
            System.out.println(" " + i);
        }

        List<Employee> list = pi.getList();
        for (Employee employee :
             list) {
            System.out.println("ID" + employee.getEmpId() + "Name" + employee.getEmpName());
        }
    }

    @Autowired
    Environment env;

    @Test
    public void someTest() {
        System.out.println(env);
        System.out.println(env.getProperty("dataSource.environment"));
    }
}
