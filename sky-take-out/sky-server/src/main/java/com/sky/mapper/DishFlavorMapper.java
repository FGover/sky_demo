package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 新增菜品口味
     *
     * @param dishFlavors
     */
    void addDishFlavor(List<DishFlavor> dishFlavors);

    /**
     * 根据菜品id删除口味
     *
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteDishFlavorByDishId(Long dishId);

    /**
     * 批量删除口味
     *
     * @param ids
     */
    void deleteDishFlavorByDishIds(List<Long> ids);

    /**
     * 根据菜品id查询口味
     *
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> selectByDishId(Long id);
}
