package ssm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssm.dao.DepartmentMapper;
import ssm.pojo.Department;

import java.util.List;

/**
 * @author 肥宅快乐码
 * @date 2019/2/25 - 14:12
 */
@Service
public class DepartmentService {

    /**
     *
     */
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> getDepts() {
        List<Department> list = departmentMapper.selectByExample(null);
        return list;
    }
}
