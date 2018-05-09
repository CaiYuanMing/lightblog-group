package service;

import pojo.Follow;
import pojo.UserTemp;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    UserTemp getUserTempByUserId(String userId, HttpSession httpSession);
    String getUserNameByUserId(String userId);
    String getUserHeadIconPathByUserId(String userId);
    List<Follow> getFollowListByUserId(String userId, HttpSession httpSession);
}
