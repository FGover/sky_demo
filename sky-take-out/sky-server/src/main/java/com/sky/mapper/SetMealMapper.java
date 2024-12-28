package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface SetMealMapper {

    /**
     * 新增套餐
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void addSetMeal(Setmeal setmeal);

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> selectPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据setMealId查询套餐
     *
     * @param setMealId
     * @return
     */
    @Select("select * from setmeal where id = #{setMealId}")
    Setmeal selectById(Long setMealId);

    /**
     * 修改套餐
     *
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void updateSetMeal(Setmeal setmeal);

    /**
     * 删除套餐
     *
     * @param id
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);
}
