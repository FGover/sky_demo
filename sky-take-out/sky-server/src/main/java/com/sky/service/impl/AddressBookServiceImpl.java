package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 查询默认地址
     *
     * @return
     */
    @Override
    public AddressBook getDefault() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.selectDefault(userId);
    }

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    @Override
    public void updateDefault(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        AddressBook address = addressBookMapper.selectDefault(userId);
        address.setIsDefault(0);
        addressBookMapper.updateAddress(address);
        addressBook.setIsDefault(1);
        addressBookMapper.updateAddress(addressBook);
    }

    /**
     * 修改地址
     *
     * @param addressBook
     */
    @Override
    public void updateAddress(AddressBook addressBook) {
        addressBookMapper.updateAddress(addressBook);
    }

    /**
     * 新增地址
     *
     * @param addressBook
     */
    @Override
    public void addAddress(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        List<AddressBook> addressBooks = addressBookMapper.selectAllAddress(userId);
        if (addressBooks != null && !addressBooks.isEmpty()) {
            addressBook.setIsDefault(0);
        } else {
            addressBook.setIsDefault(1);
        }
        addressBookMapper.addAddress(addressBook);
    }

    /**
     * 删除地址
     *
     * @param id
     */
    @Override
    public void deleteAddress(Long id) {
        addressBookMapper.deleteById(id);
    }

    /**
     * 查询所有地址
     *
     * @return
     */
    @Override
    public List<AddressBook> getAllAddress() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.selectAllAddress(userId);
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressBook getAddressById(Long id) {
        return addressBookMapper.selectById(id);
    }
}
