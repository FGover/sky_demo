package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 获取今日数据
     *
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {
        // 获取今日时间
        LocalDateTime beginTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);
        Map<String, Object> map = new HashMap<>();
        map.put("begin", beginTime);
        map.put("end", endTime);
        // 获取今日营业额
        Double turnover = orderMapper.sumByMap(map);
        // 获取今日全部订单
        Integer orderTotal = orderMapper.getOrderCount(map);
        // 新增用户数
        Integer newUsers = userMapper.sumByMap(map);
        // 获取有效订单数
        map.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.getOrderCount(map);
        // 订单完成率
        Double orderCompletionRate = validOrderCount * 1.0 / orderTotal;
        // 平均客单价
        Double unitPrice = orderMapper.AverageAmount(map);
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    /**
     * 获取订单数据
     *
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverView() {
        // 待接单数量
        Integer waitingOrders = orderMapper.getOrderByStatus(Orders.TO_BE_CONFIRMED);
        // 待派送数量
        Integer deliveredOrders = orderMapper.getOrderByStatus(Orders.CONFIRMED);
        // 已完成数量
        Integer completedOrders = orderMapper.getOrderByStatus(Orders.COMPLETED);
        // 已取消数量
        Integer cancelledOrders = orderMapper.getOrderByStatus(Orders.CANCELLED);
        // 获取全部订单
        Integer allOrders = orderMapper.getAllOrder();
        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 获取菜品数据
     *
     * @return
     */
    @Override
    public DishOverViewVO getDishOverView() {
        // 获取已起售菜品数量
        Integer sold = dishMapper.getSoldCount(StatusConstant.ENABLE);
        // 获取已停售菜品数量
        Integer discontinued = dishMapper.getSoldCount(StatusConstant.DISABLE);
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 获取套餐数据
     *
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        // 获取已起售套餐数量
        Integer sold = setMealMapper.getSoldCount(StatusConstant.ENABLE);
        // 获取已停售套餐数量
        Integer discontinued = setMealMapper.getSoldCount(StatusConstant.DISABLE);
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
