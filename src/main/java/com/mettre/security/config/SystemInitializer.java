package com.mettre.security.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mettre.security.pojo.Role;
import com.mettre.security.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * 系统初始化配置类，主要用于加载内置数据到目标数据库上
 */
@Component
public class SystemInitializer {

    @Value("${data:users.json}")
    private String userFileName;

    @Value("${data:roles.json}")
    private String roleFileName;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public boolean initialize() throws Exception {
        try {
            InputStream userInputStream = getClass().getClassLoader().getResourceAsStream(userFileName);
            if (userInputStream == null) {
                throw new Exception("initialzation user file not found: " + userFileName);
            }

            InputStream roleInputStream = getClass().getClassLoader().getResourceAsStream(roleFileName);
            if (roleInputStream == null) {
                throw new Exception("initialzation role file not found: " + roleFileName);
            }

            //导入初始的系统超级管理员角色
            Type roleTokenType = new TypeToken<ArrayList<Role>>() {
            }.getType();
            ArrayList<Role> roles = new Gson().fromJson(new InputStreamReader(roleInputStream, StandardCharsets.UTF_8), roleTokenType);
            for (Role role : roles) {
                if (roleRepository.findByName(role.getName()) == null) {
                    roleRepository.save(role);
                }
            }

            //导入初始的系统管理员用户
            Type teacherTokenType = new TypeToken<ArrayList<User>>() {
            }.getType();
            ArrayList<User> users = new Gson().fromJson(new InputStreamReader(userInputStream, StandardCharsets.UTF_8), teacherTokenType);
            for (User user : users) {
                if (userRepository.findByUsername(user.getUsername()) == null) {
                    userRepository.save(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}