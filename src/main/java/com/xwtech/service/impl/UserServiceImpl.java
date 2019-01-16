package com.xwtech.service.impl;

import com.xwtech.base.ApiResponse;
import com.xwtech.base.StaticSouces;
import com.xwtech.pojo.User;
import com.xwtech.repository.UserRepository;
import com.xwtech.service.UserService;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        User user = new User();
        user.setId(id);
        this.userRepository.delete(user);
        DeleteResponse response = this.client.prepareDelete(StaticSouces.USER_INDEX, StaticSouces.USER_TYPE, id + "").get();
        return new ApiResponse(200,"OK",response.getResult());
    }

    @Override
    public ApiResponse updateUser(User user) {
        try {
            this.userRepository.save(user);

            XContentBuilder content = XContentFactory.jsonBuilder().startObject();
            if(user.getAddress() !=null){
                content.field("address",user.getAddress());
            }
            if(user.getAge() !=null){
                content.field("age",user.getAge());
            }
            if(user.getPhone() !=null){
                content.field("phone",user.getPhone());
            }
            if(user.getEmail() !=null){
                content.field("email",user.getEmail());
            }
            if(user.getAddress() !=null){
                content.field("isUser",user.getIsUser());
            }
            content.field("updateDate",user.getUpdateDate()).endObject();

            UpdateRequest request =new UpdateRequest();
            request.index(StaticSouces.USER_INDEX).type(StaticSouces.USER_TYPE).id(user.getId()+"").doc(content);
            UpdateResponse result = this.client.update(request).get();
            return new ApiResponse(200,"OK",result.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(400,"数据异常");
        }


    }

    @Override
    public ApiResponse findAll() {
        List<User> userList = this.userRepository.findAll();
        if(userList.size() > 0 || userList != null){
            return new ApiResponse(200,"OK",userList);
        }
        return new ApiResponse(400,"数据异常");
    }

    @Override
    public ApiResponse finfById(Integer id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user != null){
            return new ApiResponse(200,"OK",user);
        }
        return new ApiResponse(400,"数据异常");
    }
}
