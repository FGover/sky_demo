package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理模块")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("条件查询订单")
    public Result<PageResult> selectByPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("条件查询订单，参数：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.selectPageAdmin(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("各个状态订单数量统计")
    public Result<OrderStatisticsVO> getStatistics() {
        log.info("各个状态订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.getStatistics();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("查看订单详情")
    public Result<OrderVO> selectOrderDetail(@PathVariable Long id) {
        log.info("查看订单详情，参数：{}", id);
        OrderVO orderDetail = orderService.getOrderDetail(id);
        return Result.success(orderDetail);
    }

    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<String> confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单，参数：{}", ordersConfirmDTO);
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success("接单成功");
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<String> rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("拒单，参数：{}", ordersRejectionDTO);
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success("拒单成功");
    }

    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<String> cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("取消订单，参数：{}", ordersCancelDTO);
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success("取消订单成功");
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("订单派送")
    public Result<String> deliveryOrder(@PathVariable Long id) {
        log.info("订单派送，参数：{}", id);
        orderService.deliveryOrder(id);
        return Result.success("订单派送成功");
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("订单完成")
    public Result<String> completeOrder(@PathVariable Long id) {
        log.info("订单完成，参数：{}", id);
        orderService.completeOrder(id);
        return Result.success("订单完成成功");
    }

}
