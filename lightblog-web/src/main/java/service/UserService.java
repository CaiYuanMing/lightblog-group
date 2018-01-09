package service;

import pojo.UserTemp;

import javax.servlet.http.HttpSession;

public interface UserService {
    UserTemp getUserTempByUserId(String userId, HttpSession httpSession);
    String getUserNameByUserId(String userId);
}
