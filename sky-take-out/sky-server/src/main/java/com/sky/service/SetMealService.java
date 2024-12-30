package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    void addSetMeal(SetmealDTO setmealDTO);

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult selectPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    SetmealVO selectById(Long id);

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void updateSetMeal(SetmealDTO setmealDTO);

    /**
     * 修改套餐状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 删除套餐
     *
     * @param ids
     */
    void deleteSetMeal(List<Long> ids);

    /**
     * 条件查询套餐
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> selectByCategoryId(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品
     *
     * @param setMealId
     * @return
     */
    List<DishItemVO> getDishItemBySetMealId(Long setMealId);
}
