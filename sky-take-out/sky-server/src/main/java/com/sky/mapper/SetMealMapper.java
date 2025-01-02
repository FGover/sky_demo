package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


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

    /**
     * 条件查询套餐
     *
     * @param setmeal
     * @return
     */
    @Select("select * from setmeal where category_id = #{categoryId} and status = #{status}")
    List<Setmeal> selectByCategoryId(Setmeal setmeal);

    @Select("select sd.name, sd.copies, d.image, d.description from setmeal_dish sd left join " +
            "dish d on sd.dish_id = d.id where sd.setmeal_id = #{setMealId}")
    List<DishItemVO> getDishItemBySetMealId(Long setMealId);
}
