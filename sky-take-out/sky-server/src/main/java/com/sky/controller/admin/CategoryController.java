package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类模块接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    @ApiOperation("分页查询分类信息")
    public Result<PageResult> selectPage(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询分类信息，参数：{}",categoryPageQueryDTO);
        PageResult pageResult = categoryService.selectPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @ApiOperation("新增分类")
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：参数：{}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        String msg = "添加成功";
        return Result.success(msg);
    }

    @DeleteMapping
    @ApiOperation("删除分类")
    public Result<String> deleteCategory(Long id){
        log.info("删除分类：参数：{}", id);
        categoryService.deleteCategory(id);
        String msg = "删除成功";
        return Result.success(msg);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改分类状态")
    public Result<String> updateStatus(Long id, @PathVariable Integer status) {
        log.info("修改分类状态：参数：id={},status={}", id, status);
        categoryService.updateStatus(id, status);
        String msg = "修改成功";
        return Result.success(msg);
    }

    @PutMapping
    @ApiOperation("修改分类信息")
    public Result<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：参数：{}", categoryDTO);
        categoryService.updateCategory(categoryDTO);
        String msg = "修改成功";
        return Result.success(msg);
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> selectByType(Integer type) {
        log.info("根据类型查询分类：参数：{}", type);
        List<Category> categories = categoryService.selectByType(type);
        return Result.success(categories);
    }
}
