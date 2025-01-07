package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 提交订单
     *
     * @param orders
     */
    void insertOrder(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     * @return
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders selectOrderByNumber(String orderNumber);

    /**
     * 根据订单id查询订单
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders selectById(Long id);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void updateOrder(Orders orders);

    /**
     * 获取超时订单
     *
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> selectTimeoutOrder(Integer status, LocalDateTime orderTime);

    /**
     * 分页查询订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> selectByPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态统计订单数量
     *
     * @param status
     * @return
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);
}
