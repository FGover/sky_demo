package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品或口味
     *
     * @param dishDTO
     */
    @Transactional   // 多表操作，要么全失败要么全成功
    @Override
    public void addDish(DishDTO dishDTO) {
        // 向菜品表插入
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        //获取insert语句返回的id
        Long dishId = dish.getId();
        // 向口味表插入
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {
            dishFlavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorMapper.addDishFlavor(dishFlavors);
        }
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.selectPage(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除菜品
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteDish(List<Long> ids) {
        // 如果菜品为启售状态，不能删除
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                // 不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 如果菜品关联套餐，不能删除
        List<Long> setMealIds = setMealDishMapper.getSetMealIdsByDishId(ids);
        if (setMealIds != null && !setMealIds.isEmpty()) {
            // 不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            // 删除菜品也要删除口味
//            dishFlavorMapper.deleteDishFlavorByDishId(id);
//        }
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteDishFlavorByDishIds(ids);
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getDishById(Long id) {
        // 查询菜品
        Dish dish = dishMapper.selectById(id);
        // 查询口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(id);
        // 封装数据
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品或口味
     *
     * @param dishDTO
     */
    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 修改菜品
        dishMapper.updateDish(dish);
        // 先删除原有口味
        dishFlavorMapper.deleteDishFlavorByDishId(dish.getId());
        // 再插入
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {
            dishFlavors.forEach(flavor -> flavor.setDishId(dish.getId()));
            dishFlavorMapper.addDishFlavor(dishFlavors);
        }
    }

    /**
     * 修改菜品状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateDishStatus(Long id, Integer status) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.updateDish(dish);
    }

}
