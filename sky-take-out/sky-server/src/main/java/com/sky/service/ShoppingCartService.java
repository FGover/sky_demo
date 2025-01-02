package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    void addCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 减少数量
     *
     * @param shoppingCartDTO
     */
    void subCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 根据id清空购物车
     */
    void cleanCart();

    /**
     * 根据id查询购物车
     *
     * @return
     */
    List<ShoppingCart> selectCart();
}
