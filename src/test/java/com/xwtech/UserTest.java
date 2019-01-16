package com.xwtech;

import com.xwtech.base.ApiResponse;
import com.xwtech.pojo.User;
import com.xwtech.repository.UserRepository;
import com.xwtech.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admini on 2019/1/16.
 */
public class UserTest extends DemoApplicationTests{

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUser(){
        User user = new User();
        user.setName("刘振");
        user.setPassword("123456");
        User u = this.userRepository.save(user);
        System.out.println(u.getId());

    }
}
