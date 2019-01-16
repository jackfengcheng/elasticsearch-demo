package com.xwtech.service;

import com.xwtech.base.ApiResponse;
import com.xwtech.pojo.User;

import java.util.List;

/**
 * Created by jack on 2019/1/16.
 */
public interface UserService {

    ApiResponse saveUser(User user);

    ApiResponse deleteUser(Integer id);

    ApiResponse updateUser(User user);

    ApiResponse findAll();

    ApiResponse finfById(Integer id);
}
