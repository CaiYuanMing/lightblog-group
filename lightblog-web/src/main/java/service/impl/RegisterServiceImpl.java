package service.impl;

import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.User;
import pojo.UserExample;
import service.RegisterService;

import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private User user;
    public boolean isUserExist(UserExample userExample) {
        if (userMapper.selectByExample(userExample).size()>0){
            return true;
        }
        return false;
    }

    public int addUser(String userId, String userName, String userPassword) {
        user.setUserId(userId);user.setUserName(userName);user.setUserPassword(userPassword);
        user.setUserHeadiconPath("");
        user.setUserIntroduction("");
        user.setUserRegisterTime(new Date());
        int count = userMapper.insert(user);
        return count;
    }
}
