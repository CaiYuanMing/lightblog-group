package service;

import pojo.UserExample;

public interface LoginService {
    boolean isUserExistForLg(UserExample userExample);
}
