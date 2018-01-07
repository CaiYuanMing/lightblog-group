package service.impl;

import controller.WorkListController;
import mapper.WorkInfoMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pojo.UserTemp;
import service.TagListService;
import service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class TagListServiceImpl implements TagListService {
    private  static Logger log = Logger.getLogger(WorkListController.class);
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private UserService userService;
    public List<Map<String, Object>> getTagListByOwnerId(String ownerId, HttpSession httpSession) {
        return null;
    }
    public UserTemp getUserTempByUserId(String userId, HttpSession httpSession) {
        return userService.getUserTempByUserId(userId,httpSession);
    }
}
