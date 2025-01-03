package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "用户端地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/default")
    @ApiOperation("获取默认收获地址")
    public Result<AddressBook> getDefault() {
        log.info("获取默认收获地址");
        return Result.success(addressBookService.getDefault());
    }

    @GetMapping("/list")
    @ApiOperation("获取所有地址")
    public Result<List<AddressBook>> getAllAddress() {
        log.info("获取所有地址");
        return Result.success(addressBookService.getAllAddress());
    }

    @PostMapping
    @ApiOperation("新增地址")
    public Result<String> addAddress(@RequestBody AddressBook addressBook) {
        log.info("新增地址,参数:{}", addressBook);
        addressBookService.addAddress(addressBook);
        return Result.success("新增地址成功");
    }

    @PutMapping
    @ApiOperation("修改地址")
    public Result<String> updateAddress(@RequestBody AddressBook addressBook) {
        log.info("修改地址,参数:{}", addressBook);
        addressBookService.updateAddress(addressBook);
        return Result.success("修改地址成功");
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result<String> updateDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址,参数:{}", addressBook);
        addressBookService.updateDefault(addressBook);
        return Result.success("设置默认地址成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取地址")
    public Result<AddressBook> getAddressById(@PathVariable Long id) {
        log.info("根据id获取地址,参数:{}", id);
        return Result.success(addressBookService.getAddressById(id));
    }

    @DeleteMapping
    @ApiOperation("删除地址")
    public Result<String> deleteAddress(Long id) {
        log.info("删除地址,参数:{}", id);
        addressBookService.deleteAddress(id);
        return Result.success("删除地址成功");
    }
}
