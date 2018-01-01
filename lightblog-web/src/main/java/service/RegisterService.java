package service;

import pojo.UserExample;

import java.util.Date;

public interface RegisterService {
    boolean isUserExist(UserExample userExample);
    int addUser(String userId, String userName, String userPassword);
}
