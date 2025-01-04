package com.sky.mapper;

import com.sky.entity.OrderDetail;
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
}
