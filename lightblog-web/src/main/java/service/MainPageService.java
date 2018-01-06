package service;

import pojo.UserTemp;
import pojo.WorkTemp;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MainPageService {
    List<WorkTemp> getWorkListByOwnerId(String ownerId, HttpSession httpSession);
    UserTemp getUserTempByUserId(String userId,HttpSession httpSession);
}
