package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询购物车
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> selectCart(ShoppingCart shoppingCart);

    /**
     * 更新数量
     *
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 根据id删除购物车
     *
     * @param id
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteCartById(Long id);

    /**
     * 添加购物车
     *
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            "values (#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void addCart(ShoppingCart shoppingCart);

    /**
     * 根据id清空购物车
     *
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void cleanCart(ShoppingCart shoppingCart);

    /**
     * 批量添加购物车
     *
     * @param shoppingCartList
     */
    void addBatchCart(List<ShoppingCart> shoppingCartList);
}
