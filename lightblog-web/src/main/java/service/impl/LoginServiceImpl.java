package service.impl;

import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.UserExample;
import service.LoginService;
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserExample userExample;
    public boolean isUserExistForLg(UserExample userExample) {
        if (userMapper.selectByExample(userExample).size()>0){
            return true;
        }
        return false;
    }
}
