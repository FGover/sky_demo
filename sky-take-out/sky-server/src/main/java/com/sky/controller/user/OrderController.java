package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户端订单接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("提交订单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单,参数:{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> OrderPayment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("用户支付,参数:{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.OrderPayment(ordersPaymentDTO);
        log.info("生成预支付交易单:{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/reminder/{id}")
    @ApiOperation("客户订单催单")
    public Result<String> reminder(@PathVariable Long id) {
        log.info("客户催单,参数:{}", id);
        orderService.reminder(id);
        return Result.success("催单成功");
    }

    @GetMapping("/orderDetail/{orderId}")
    @ApiOperation("查询订单明细")
    public Result<OrderVO> getOrderDetail(@PathVariable Long orderId) {
        log.info("查询订单明细,参数:{}", orderId);
        OrderVO orderVO = orderService.getOrderDetail(orderId);
        return Result.success(orderVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("分页查询历史订单")
    public Result<PageResult> selectByPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("查询历史订单,参数:{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.selectByPage(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result<String> cancelOrder(@PathVariable Long id) throws Exception {
        log.info("取消订单,参数:{}", id);
        orderService.userCancelOrder(id);
        return Result.success("取消订单成功");
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result<String> repetitionOrder(@PathVariable Long id) {
        log.info("再来一单,参数:{}", id);
        orderService.repetitionOrder(id);
        return Result.success("再来一单成功");
    }
}
