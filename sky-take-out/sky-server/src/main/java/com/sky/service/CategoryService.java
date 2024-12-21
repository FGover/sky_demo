package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

public interface CategoryService {

    /**
     * 查询分类分页
     *
     * @param categoryPageQueryDTO
     * @return PageResult
     */
    PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     *
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类
     *
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 修改分类状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 修改分类信息
     *
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);
}
