package service.impl;

import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.UserExample;
import service.RegisterService;
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserExample userExample;
    public boolean isUserExist(UserExample userExample) {
        if (userMapper.selectByExample(userExample).size()>0){
            return true;
        }
        return false;
    }
}
