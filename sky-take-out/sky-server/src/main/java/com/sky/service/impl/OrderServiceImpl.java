package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 处理异常（地址为空、购物车为空...）
        AddressBook addressBook = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectCart(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // 向订单order表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        orders.setUserName(userMapper.selectById(userId).getName());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        orderMapper.insertOrder(orders);
        // 向订单明细order_detail表插入多条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        // 批量插入
        orderDetailMapper.insertOrderDetail(orderDetailList);
        // 清空购物车
        shoppingCartMapper.cleanCart(ShoppingCart.builder().userId(userId).build());
        // 封装VO返回结果
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO OrderPayment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        Long userId = BaseContext.getCurrentId();
//        User user = userMapper.selectById(userId);
//        // 调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(),
//                new BigDecimal("0.01"),
//                "苍穹外卖订单",
//                user.getOpenid()
//        );
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException(MessageConstant.ORDER_PAID);
//        }
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
//        return vo;
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return new OrderPaymentVO();
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    @Override
    public void paySuccess(String outTradeNo) {
        // 根据订单号查询订单
        Orders orderDB = orderMapper.selectOrderByNumber(outTradeNo);
        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders order = Orders.builder()
                .id(orderDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.updateOrder(order);

        // 通过websocket向客户端浏览器推送消息（type,orderId,content）
        Map<Object, Object> map = new HashMap<>();
        map.put("type", 1);  // 1表示来单提醒，2表示客户催单
        map.put("orderId", order.getId());
        map.put("content", "订单号：" + outTradeNo);
        String jsonString = JSONObject.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);
    }

    /**
     * 客户催单
     *
     * @param id
     */
    @Override
    public void reminder(Long id) {
        // 根据id查询订单是否存在
        Orders order = orderMapper.selectById(id);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        Map<Object, Object> map = new HashMap<>();
        map.put("type", 2);  // 1表示来单提醒，2表示客户催单
        map.put("orderId", id);
        map.put("content", "订单号：" + order.getNumber());
        webSocketServer.sendToAllClient(JSONObject.toJSONString(map));
    }

    /**
     * 查询订单明细
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO getOrderDetail(Long id) {
        // 查询订单
        Orders order = orderMapper.selectById(id);
        // 查询订单对应的菜品/套餐
        List<OrderDetail> orderDetaiList = orderDetailMapper.selectByOrderId(id);
        // 封装数据
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetaiList);
        return orderVO;
    }

    /**
     * 分页查询历史订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectByPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        Page<Orders> page = orderMapper.selectByPage(ordersPageQueryDTO);
        List<OrderVO> list = new ArrayList<>();
        // 查询订单明细，封装VO
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();
                // 查询订单明细
                List<OrderDetail> orderDetaiList = orderDetailMapper.selectByOrderId(orderId);
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetaiList);
                list.add(orderVO);
            }
        }
        assert page != null;
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 取消订单
     *
     * @param id
     * @throws Exception
     */
    @Override
    public void userCancelOrder(Long id) throws Exception {
        // 根据id查询订单
        Orders order = orderMapper.selectById(id);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (order.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(order.getId());
        // 订单处于待接单状态下，需要进行退款
        if (order.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // 调用微信支付退款接口
//            weChatPayUtil.refund(
//                    order.getNumber(),  // 订单号
//                    order.getNumber(),  // 退款单号
//                    new BigDecimal(0.01),  // 退款金额
//                    new BigDecimal(0.01)  // 实收金额
//            );
            // 支付状态修改为退款
            orders.setPayStatus(Orders.REFUND);
        }
        // 更新订单状态
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(orders);
    }

    /**
     * 再来一单
     *
     * @param id
     */
    @Override
    public void repetitionOrder(Long id) {
        // 根据id查询订单
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(id);
        // 将订单详情对象转为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            // 将原订单的菜品/套餐重新加入购物车
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());
        // 将购物车对象批量插入数据库
        shoppingCartMapper.addBatchCart(shoppingCartList);
    }

    /**
     * 管理端分页条件查询订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPageAdmin(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.selectByPage(ordersPageQueryDTO);
        // 部分订单状态，需要额外返回订单菜品信息，将Orders转化为OrderVO
        List<OrderVO> orderVOList = getOrderVOList(page);
        return new PageResult(page.getTotal(), orderVOList);
    }

    /**
     * 封装VO
     *
     * @param page
     * @return
     */
    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        List<OrderVO> orderVOList = new ArrayList<>();
        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);
                // 将订单信息封装到VO，并添加到orderList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * 根据id获取菜品套餐信息字符串
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品套餐详情
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(orders.getId());
        // 将每条订单菜品套餐信息拼接为字符串（格式：宫保鸡丁x3）
        List<String> orderDishList = orderDetailList.stream().map(x -> x.getName()
                + "x" + x.getNumber()).collect(Collectors.toList());
        // 将每条订单菜品套餐信息拼接为字符串（格式：宫保鸡丁x3,鱼香肉丝x2）
        return String.join("，", orderDishList);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @Override
    public OrderStatisticsVO getStatistics() {
        // 根据状态，分别查询出待接单、待派送、派送中的订单数量
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer toBeDelivered = orderMapper.countStatus(Orders.CONFIRMED);
        Integer delivering = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
        // 将查询结果封装到VO
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(toBeDelivered);
        orderStatisticsVO.setDeliveryInProgress(delivering);
        return orderStatisticsVO;
    }

    /**
     * 确认订单
     *
     * @param ordersConfirmDTO
     */
    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.updateOrder(orders);
    }

    /**
     * 拒绝订单
     *
     * @param ordersRejectionDTO
     */
    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        // 根据id查询订单
        Orders order = orderMapper.selectById(ordersRejectionDTO.getId());
        // 订单存在且状态为2（待接单）才可以拒单
        if (order == null || !Objects.equals(order.getStatus(), Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 支付状态
//        Integer payStatus = order.getPayStatus();
//        if (Objects.equals(payStatus, Orders.PAID)) {
//            // 如果订单已支付，则退款
//            String refund = weChatPayUtil.refund(
//                    order.getNumber(),
//                    order.getNumber(),
//                    new BigDecimal("0.01"),
//                    new BigDecimal("0.01")
//            );
//            log.info("退款结果：{}", refund);
//        }
        // 拒单需要退款
        Orders orders = new Orders();
        orders.setId(order.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(orders);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     */
    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        // 根据id查询订单
        Orders order = orderMapper.selectById(ordersCancelDTO.getId());
        // 支付状态
//        Integer payStatus = order.getPayStatus();
//        if (Objects.equals(payStatus, Orders.PAID)) {
//            // 如果订单已支付，则退款
//            String refund = weChatPayUtil.refund(
//                    order.getNumber(),
//                    order.getNumber(),
//                    new BigDecimal("0.01"),
//                    new BigDecimal("0.01")
//            );
//            log.info("退款结果：{}", refund);
//        }
        // 拒单需要退款
        Orders orders = new Orders();
        orders.setId(order.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(orders);
    }

    /**
     * 派送订单
     *
     * @param id
     */
    @Override
    public void deliveryOrder(Long id) {
        Orders order = orderMapper.selectById(id);
        if (order == null || !Objects.equals(order.getStatus(), Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(order.getId());
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.updateOrder(orders);
    }

    /**
     * 完成订单
     *
     * @param id
     */
    @Override
    public void completeOrder(Long id) {
        Orders order = orderMapper.selectById(id);
        if (order == null || !Objects.equals(order.getStatus(), Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(order.getId());
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateOrder(orders);
    }
}
