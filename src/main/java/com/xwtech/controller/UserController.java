package com.xwtech.controller;

import com.xwtech.base.ApiResponse;
import com.xwtech.pojo.User;
import com.xwtech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by admini on 2019/1/16.
 */
@RestController
@RequestMapping("userindex/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/saveUser")
    public ApiResponse saveUser(@RequestParam(name = "name") String name,
                                @RequestParam(name = "password") String password,
                                @RequestParam(name = "age") String age,
                                @RequestParam(name = "phone") String phone,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "address") String address,
                                @RequestParam(name = "createDate") Date createDate,
                                @RequestParam(name = "imageUrl") String imageUrl){
        User user = new User();
        user.setAddress(address);
        user.setName(name);
        user.setAge(age);
        user.setPhone(phone);
        user.setPassword(password);
        user.setCreateDate(createDate);
        user.setEmail(email);
        user.setImageUrl(imageUrl);
        user.setIsUser(1);
        ApiResponse apiResponse = this.userService.saveUser(user);
        return new ApiResponse(200,"OK",apiResponse.getData());
    }
}
