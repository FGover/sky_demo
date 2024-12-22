package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.enumeration.OperationType;
import com.sky.vo.EmployeePageVO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface EmployeeMapper {
    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return employee
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     *
     * @param employee
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time," +
            "update_time, create_user, update_user)" + "values" +
            "(#{name}, #{username}, #{password}, #{phone}, " +
            "#{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void addEmployee(Employee employee);

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO
     * @return Page<Employee>
     */
    Page<EmployeePageVO> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工
     *
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateEmployee(Employee employee);

    /**
     * 根据id查询员工
     *
     * @param id
     * @return employee
     */
    @Select("select * from employee where id = #{id}")
    EmployeeDTO getById(Long id);
}
