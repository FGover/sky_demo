package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkSpaceService {

    /**
     * 获取今日工作台数据
     *
     * @return
     */
    BusinessDataVO getBusinessData();

    /**
     * 获取订单概览
     *
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 获取菜品概览
     *
     * @return
     */
    DishOverViewVO getDishOverView();

    /**
     * 获取套餐概览
     *
     * @return
     */
    SetmealOverViewVO getSetmealOverView();
}
