package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 查询默认地址
     *
     * @return
     */
    @Select("select * from address_book where is_default = 1 and user_id = #{userId}")
    AddressBook selectDefault(Long userId);

    /**
     * 查询所有地址
     *
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> selectAllAddress(Long userId);

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook selectById(Long id);

    /**
     * 修改地址
     *
     * @param addressBook
     */
    void updateAddress(AddressBook addressBook);

    /**
     * 新增地址
     *
     * @param addressBook
     */
    void addAddress(AddressBook addressBook);

    /**
     * 删除地址
     *
     * @param id
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}
