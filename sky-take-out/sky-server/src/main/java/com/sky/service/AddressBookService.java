package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 查询默认地址
     *
     * @return
     */
    AddressBook getDefault();

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    void updateDefault(AddressBook addressBook);

    /**
     * 修改地址
     *
     * @param addressBook
     */
    void updateAddress(AddressBook addressBook);

    /**
     * 新增地址
     */
    void addAddress(AddressBook addressBook);

    /**
     * 删除地址
     *
     * @param id
     */
    void deleteAddress(Long id);

    /**
     * 查询所有地址
     *
     * @return
     */
    List<AddressBook> getAllAddress();

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    AddressBook getAddressById(Long id);
}
