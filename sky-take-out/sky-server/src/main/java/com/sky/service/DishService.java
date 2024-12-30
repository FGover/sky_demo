package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品或口味
     *
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     */
    PageResult selectPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     *
     * @param ids
     */
    void deleteDish(List<Long> ids);

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    DishVO getDishById(Long id);

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 修改菜品状态
     *
     * @param id
     * @param status
     */
    void updateDishStatus(Long id, Integer status);

    /**
     * 根据分类id查询对应菜品
     *
     * @param categoryId
     * @return
     */
    List<Dish> selectByCategoryId(Long categoryId);

    /**
     * 条件查询菜品和口味
     *
     * @param dish
     * @return
     */
    List<DishVO> selectDishWithFlavor(Dish dish);
}
