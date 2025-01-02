package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "用户端菜品接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    @ApiOperation("根据分类id查询")
    public Result<List<DishVO>> selectByCategoryId(Long categoryId) {
        log.info("分类id查询菜品：{}", categoryId);
        String key = "dish_" + categoryId;
        // 从redis中获取菜品列表
        List<DishVO> dishList = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (dishList != null && !dishList.isEmpty()) {
            return Result.success(dishList);
        }
        // redis无缓存再查数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        // 起售中的菜品
        dish.setStatus(StatusConstant.ENABLE);
        dishList = dishService.selectDishWithFlavor(dish);
        // 将菜品列表存入redis
        redisTemplate.opsForValue().set(key, dishList);
        return Result.success(dishList);
    }
}
