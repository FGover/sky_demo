package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void addSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 新增套餐
        setMealMapper.addSetMeal(setmeal);
        // 新增套餐菜品
        List<SetmealDish> setMealDishes = setmealDTO.getSetmealDishes();
        setMealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        System.out.println(setMealDishes);
        setMealDishMapper.addSetMealDish(setMealDishes);
    }

    @Override
    public PageResult selectPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.selectPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public SetmealVO selectById(Long id) {
        Setmeal setmeal = setMealMapper.selectById(id);
        List<SetmealDish> setMealDishes = setMealDishMapper.selectBySetMealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setMealDishes);
        return setmealVO;
    }

    @Override
    public void updateSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新套餐
        setMealMapper.updateSetMeal(setmeal);
        // 更新套餐菜品
        setMealDishMapper.deleteBySetMealId(setmeal.getId());
        List<SetmealDish> setMealDishes = setmealDTO.getSetmealDishes();
        setMealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setMealDishMapper.addSetMealDish(setMealDishes);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            List<Dish> dishes = dishMapper.selectBySetMealId(id);
            if (!dishes.isEmpty()) {
                for (Dish dish : dishes) {
                    if (Objects.equals(dish.getStatus(), StatusConstant.DISABLE)) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setMealMapper.updateSetMeal(setmeal);
    }

    @Override
    public void deleteSetMeal(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setMealMapper.selectById(id);
            if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        ids.forEach(setMealId -> {
            setMealMapper.deleteById(setMealId);
            setMealDishMapper.deleteBySetMealId(setMealId);
        });
    }

    @Override
    public List<Setmeal> selectByCategoryId(Setmeal setmeal) {
        return setMealMapper.selectByCategoryId(setmeal);
    }

    @Override
    public List<DishItemVO> getDishItemBySetMealId(Long setMealId) {
        return setMealMapper.getDishItemBySetMealId(setMealId);
    }
}
