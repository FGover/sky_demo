package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("adminSetMealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐模块接口")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 删除redis中的套餐缓存
     *
     * @param pattern
     */
    private void CleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result<String> addSetMeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐，参数：{}", setmealDTO);
        setmealDTO.setStatus(StatusConstant.ENABLE);
        setMealService.addSetMeal(setmealDTO);
        String msg = "新增套餐成功";
        // 删除缓存
//        String key = "setmeal_" + setmealDTO.getCategoryId();
//        CleanCache(key);
        return Result.success(msg);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询套餐")
    public Result<PageResult> selectPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐，参数：{}", setmealPageQueryDTO);
        PageResult pageResult = setMealService.selectPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> selectById(@PathVariable Long id) {
        log.info("根据id查询套餐，参数：{}", id);
        SetmealVO setmealVO = setMealService.selectById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<String> updateSetMeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐，参数：{}", setmealDTO);
        setMealService.updateSetMeal(setmealDTO);
        String msg = "修改套餐成功";
        // 删除缓存
//        CleanCache("setmeal_*");
        return Result.success(msg);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改套餐状态")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<String> updateStatus(Long id, @PathVariable Integer status) {
        log.info("修改套餐状态，参数：id={},status={}", id, status);
        setMealService.updateStatus(id, status);
        String msg = "修改套餐状态成功";
        // 删除缓存
//        CleanCache("setmeal_*");
        return Result.success(msg);
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<String> deleteSetMeal(@RequestParam List<Long> ids) {
        log.info("删除套餐，参数：{}", ids);
        setMealService.deleteSetMeal(ids);
        String msg = "删除套餐成功";
        // 删除缓存
//        CleanCache("setmeal_*");
        return Result.success(msg);
    }
}
