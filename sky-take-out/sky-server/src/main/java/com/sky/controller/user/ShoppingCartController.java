package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "用户购物车接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    @ApiOperation("查询购物车")
    public Result<List<ShoppingCart>> getCartList() {
        log.info("查询购物车");
        List<ShoppingCart> cartList = shoppingCartService.selectCart();
        return Result.success(cartList);
    }

    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<String> addCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车,参数:{}",shoppingCartDTO);
        shoppingCartService.addCart(shoppingCartDTO);
        String msg = "添加购物车成功";
        return Result.success(msg);
    }

    @PostMapping("/sub")
    @ApiOperation("减少购物车")
    public Result<String> subCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("减少购物车,参数:{}",shoppingCartDTO);
        shoppingCartService.subCart(shoppingCartDTO);
        String msg = "减少购物车成功";
        return Result.success(msg);
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<String> cleanCart() {
        log.info("清空购物车");
        shoppingCartService.cleanCart();
        String msg = "清空购物车成功";
        return Result.success(msg);
    }
}
