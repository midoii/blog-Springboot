package com.blog.item.service;


import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.GetRandomString;
import com.blog.item.mapper.AdminMapper;
import com.blog.item.pojo.Admin;
import com.blog.item.pojo.TokenTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class AdminService {

    final int EXPIRE_TIME=604800;

    @Autowired
    private AdminMapper adminMapper;

    public ReturnJson createAdmin(String username, String password){
        if(username.length() == 0){
            ReturnJson<String> returnJson =
                    new ReturnJson<>(false, -4002, "用户名不能为空", "");
            return returnJson;
        }
        if(password.length() < 6){
            ReturnJson<String> returnJson =
                    new ReturnJson<>(false, -4002, "密码不能少于6位", "");
            return returnJson;
        }
        MessageDigest messageDigest = null;
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setUserId(GetRandomString.getRandomString(22));
        admin.setCreateTime((new Date().getTime())/1000);
        admin.setStatus(0);
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            String salt = GetRandomString.getRandomString(60);
            admin.setSalt(salt);
            messageDigest.update((password + salt).getBytes());
            String s = new BigInteger(1, messageDigest.digest()).toString(16);
            admin.setPassword(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        adminMapper.insert(admin);
        return new ReturnJson<>(true, 200, "创建用户成功", "");
    }

    public ReturnJson verifyAdminMsg(String username, String password){
        if(username.length() == 0){
            ReturnJson<String> returnJson =
                    new ReturnJson<>(false, -4002, "用户名不能为空", "");
            return returnJson;
        }
        if(password.length() < 6){
            ReturnJson<String> returnJson =
                    new ReturnJson<>(false, -4002, "密码不能少于6位", "");
            return returnJson;
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        Admin one = adminMapper.selectOne(admin);
        if(one == null){
            return new ReturnJson<>(false, -4002, "账号不存在", "");
        }else{
            boolean equal = cbPasswordEqual(one.getPassword(), one.getSalt(), password);
            if(equal){
                System.out.println("ok");
                one.setLastLoginTime((new Date().getTime())/1000);
                one.setTokenExpiresIn(one.getLastLoginTime()+EXPIRE_TIME);
                one.setAccessToken(GetRandomString.getRandomString(22));
                one.setToken(new TokenTemplate(one.getAccessToken(), one.getTokenExpiresIn(), EXPIRE_TIME));
                adminMapper.updateByPrimaryKeySelective(one);
                return new ReturnJson<>(true, 200, "sucess", one);
            }else{
                return new ReturnJson<>(false, -4002, "密码错误", "");
            }
        }
    }
    public boolean cbPasswordEqual(String hash, String salt, String password){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((password + salt).getBytes());
            String s = new BigInteger(1, messageDigest.digest()).toString(16);
            System.out.println(s);
            return (s.equals(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAccessToken(String accessToken){
        Admin admin = new Admin();
        admin.setAccessToken(accessToken);
        Admin one = adminMapper.selectOne(admin);
        if(one == null)
            return false;
        else if(one.getTokenExpiresIn()<((new Date().getTime())/1000)) {
            return false;
        }
        return true;
    }
}
