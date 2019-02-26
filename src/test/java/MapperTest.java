import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ssm.config.RootConfig;
import ssm.dao.DepartmentMapper;
import ssm.dao.EmployeeMapper;
import ssm.pojo.Department;
import ssm.pojo.Employee;

import java.util.UUID;

@ContextConfiguration(classes = RootConfig.class)
@ActiveProfiles("test")
@RunWith(value = SpringJUnit4ClassRunner.class)
@WebAppConfiguration
/**
 * @author 肥宅快乐码
 * @date 2019/2/22 - 21:41
 */
public class MapperTest {

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

    @Autowired
    SqlSession sqlSession;

    @Test
    public void testCRUD() {
        System.out.println(departmentMapper);

        departmentMapper.insertSelective(new Department(null,"开发部"));
        departmentMapper.insertSelective(new Department(null,"测试部"));

        employeeMapper.insertSelective(new Employee(null,"Jerry","M","Jerry@qq.com",1));

        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for (int i = 0; i<1000; i++) {
            String  uid = UUID.randomUUID().toString().substring(0,5) + i;
            mapper.insertSelective(new Employee(null, uid, "M", "@qq.com", 1));
        }
    }
}
