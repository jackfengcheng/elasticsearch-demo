package com.xwtech.service.impl;

import com.xwtech.base.ApiResponse;
import com.xwtech.base.StaticSouces;
import com.xwtech.pojo.User;
import com.xwtech.repository.UserRepository;
import com.xwtech.service.UserService;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by jack on 2019/1/16.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TransportClient client;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ApiResponse saveUser(User user) {
        try {
            User save = this.userRepository.save(user);
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("name",user.getName())
                    .field("password",user.getPassword())
                    .field("age",user.getAge())
                    .field("phone",user.getPhone())
                    .field("email",user.getEmail())
                    .field("address",user.getAddress())
                    .field("createDate",user.getCreateDate())
                    .field("updateDate",user.getUpdateDate())
                    .field("lastLoginTime",user.getLastLoginTime())
                    .field("isUser",user.getIsUser())
                    .endObject();
            IndexResponse response = this.client.prepareIndex(StaticSouces.USER_INDEX, StaticSouces.USER_TYPE, save.getId() + "")
                    .setSource(content).get();
            return  new ApiResponse(200,"OK",response.getResult());
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse(400,"Error");
        }

    }

    @Override
    public ApiResponse deleteUser(Integer id) {
        return null;
    }

    @Override
    public ApiResponse updateUser(User user) {
        return null;
    }

    @Override
    public ApiResponse findAll() {
        return null;
    }

    @Override
    public ApiResponse finfById(Integer id) {
        return null;
    }
}
