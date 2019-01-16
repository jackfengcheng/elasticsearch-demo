package com.xwtech.controller;

import com.xwtech.base.ApiResponse;
import com.xwtech.pojo.User;
import com.xwtech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        ApiResponse response = this.userService.saveUser(user);
        return new ApiResponse(response.getCode(),response.getMessage(),response.getData());
    }

    @GetMapping("/findAll")
    public ApiResponse findAll(){
        ApiResponse response = this.userService.findAll();
        return new ApiResponse(response.getCode(),response.getMessage(),response.getData());
    }

    @GetMapping("/findById")
    public ApiResponse findById(@RequestParam(name = "id") Integer id){
        ApiResponse response = this.userService.finfById(id);
        return new ApiResponse(response.getCode(),response.getMessage(),response.getData());
    }

    @PutMapping("/updateUser")
    public ApiResponse updateUser(@RequestParam(name = "ID") Integer id,
                                  @RequestParam(name = "name",required = false) String name,
                                  @RequestParam(name = "password",required = false) String password,
                                  @RequestParam(name = "age",required = false) String age,
                                  @RequestParam(name = "phone",required = false) String phone,
                                  @RequestParam(name = "email",required = false) String email,
                                  @RequestParam(name = "address",required = false) String address,
                                  @RequestParam(name = "imageUrl",required = false) String imageUrl){
        User user = new User();
        user.setId(id);
        if(name != null){
            user.setName(name);
        }
        if(password != null){
            user.setPassword(password);
        }
        if(age != null){
            user.setAge(age);
        }
        if(phone != null){
            user.setPhone(phone);
        }
        if(email != null){
            user.setEmail(email);
        }
        if(address != null){
            user.setAddress(address);
        }
        if(imageUrl != null){
            user.setImageUrl(imageUrl);
        }
        ApiResponse response = this.userService.updateUser(user);
        return new ApiResponse(response.getCode(),response.getMessage(),response.getData());
    }

    @DeleteMapping("deleteUser")
    public ApiResponse deleteUser(@RequestParam(name = "ID") Integer id){
        ApiResponse response = this.userService.deleteUser(id);
        return new ApiResponse(response.getCode(),response.getMessage(),response.getData());
    }
}
