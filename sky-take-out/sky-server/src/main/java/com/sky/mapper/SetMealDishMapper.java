package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 根据菜品id查询套餐id
     *
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByDishId(List<Long> dishIds);

    /**
     * 根据套餐id查询套餐菜品
     *
     * @param setMealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setMealId}")
    List<SetmealDish> selectBySetMealId(Long setMealId);

    /**
     * 根据套餐id删除套餐菜品
     *
     * @param setMealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setMealId}")
    void deleteBySetMealId(Long setMealId);

    /**
     * 新增套餐菜品
     *
     * @param setMealDishes
     */
    void addSetMealDish(List<SetmealDish> setMealDishes);
}
