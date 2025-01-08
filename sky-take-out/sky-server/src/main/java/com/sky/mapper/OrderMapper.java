package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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


    /**
     * 根据条件营业订单金额统计
     *
     * @param map
     * @return
     */
    Double sumByMap(Map<String, Object> map);

    /**
     * 平均客单价
     *
     * @param map
     * @return
     */
    Double AverageAmount(Map<String, Object> map);

    /**
     * 根据条件统计订单数量
     *
     * @param map
     * @return
     */
    Integer getOrderCount(Map<String, Object> map);

    /**
     * 根据条件统计top10菜品
     *
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getTop10(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据状态查询订单数量
     *
     * @param status
     * @return
     */
    Integer getOrderByStatus(Integer status);

    /**
     * 获取全部订单数量
     *
     * @return
     */
    @Select("select count(id) from orders")
    Integer getAllOrder();
}
