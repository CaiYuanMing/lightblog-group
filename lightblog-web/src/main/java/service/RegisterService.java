package service;

import pojo.UserExample;

public interface RegisterService {
    boolean isUserExist(UserExample userExample);
}
