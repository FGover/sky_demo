package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台管理接口")
@Slf4j
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    @GetMapping("/businessData")
    @ApiOperation("今日数据")
    public Result<BusinessDataVO> getBusinessData(){
        log.info("获取今日数据");
        return Result.success(workSpaceService.getBusinessData());
    }

    @GetMapping("/overviewOrders")
    @ApiOperation("订单管理")
    public Result<OrderOverViewVO> getOrderOverView(){
        log.info("获取今日订单数据");
        return Result.success(workSpaceService.getOrderOverView());
    }

    @GetMapping("/overviewDishes")
    @ApiOperation("菜品总览")
    public Result<DishOverViewVO> getDishOverView(){
        log.info("获取菜品总览数据");
        return Result.success(workSpaceService.getDishOverView());
    }

    @GetMapping("/overviewSetmeals")
    @ApiOperation("套餐总览")
    public Result<SetmealOverViewVO> getSetmealOverView(){
        log.info("获取套餐总览数据");
        return Result.success(workSpaceService.getSetmealOverView());
    }
}
