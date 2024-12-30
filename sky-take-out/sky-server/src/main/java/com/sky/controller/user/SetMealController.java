package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Api(tags = "用户端套餐接口")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @GetMapping("/list")
    @ApiOperation("条件查询套餐")
    public Result<List<Setmeal>> selectByCategoryId(Long categoryId) {
        log.info("条件查询套餐，categoryId：{}", categoryId);
        Setmeal setmeal = Setmeal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return Result.success(setMealService.selectByCategoryId(setmeal));
    }

    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询菜品")
    public Result<List<DishItemVO>> getDishItemBySetMealId(@PathVariable Long id) {
        log.info("根据套餐id查询菜品，setMealId：{}", id);
        return Result.success(setMealService.getDishItemBySetMealId(id));
    }
 }
