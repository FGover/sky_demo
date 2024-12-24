package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品模块接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        dishService.addDish(dishDTO);
        String msg = "添加菜品成功";
        return Result.success(msg);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> selectPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.selectPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result<String> deleteDish(@RequestParam List<Long> ids) {
        log.info("删除菜品:{}", ids);
        dishService.deleteDish(ids);
        String msg = "删除成功";
        return Result.success(msg);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}", id);
        DishVO dishVO = dishService.getDishById(id);
        String msg = "查询成功";
        return Result.success(dishVO, msg);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);
        dishService.updateDish(dishDTO);
        String msg = "修改成功";
        return Result.success(msg);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品状态")
    public Result<String> updateDishStatus(long id, @PathVariable Integer status) {
        log.info("修改菜品状态:{}", id);
        dishService.updateDishStatus(id, status);
        String msg = "修改成功";
        return Result.success(msg);
    }
}
