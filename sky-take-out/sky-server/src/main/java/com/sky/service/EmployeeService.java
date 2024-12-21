package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return employee
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO
     * @return PageResult
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 禁用启用员工账号
     *
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据id查询员工
     *
     * @param id
     * @return employee
     */
    EmployeeDTO getById(Long id);

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     */
    void updateEmployee(EmployeeDTO employeeDTO);
}
