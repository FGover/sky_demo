package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;


public interface OrderService {

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 支付订单
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO OrderPayment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 客户催单
     *
     * @param id
     */
    void reminder(Long id);

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    OrderVO getOrderDetail(Long orderId);

    /**
     * 分页查询历史订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult selectByPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 用户取消订单
     *
     * @param id
     */
    void userCancelOrder(Long id) throws Exception;

    /**
     * 再来一单
     *
     * @param id
     */
    void repetitionOrder(Long id);

    /**
     * 管理端分页条件查询订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult selectPageAdmin(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态订单数量统计
     *
     * @return
     */
    OrderStatisticsVO getStatistics();

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @throws Exception
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     *
     * @param id
     */
    void deliveryOrder(Long id);

    /**
     * 完成订单
     *
     * @param id
     */
    void completeOrder(Long id);
}
