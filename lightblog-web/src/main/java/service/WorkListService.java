package service;

import pojo.UserTemp;
import pojo.WorkListItem;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface WorkListService {
    Map<String,Object> getWorkListMapByOwnerId(String ownerId, HttpSession httpSession);
    UserTemp getUserTempByUserId(String userId, HttpSession httpSession);
    Map<String,Object> getWorkInfoByCategory(String userId,String category,HttpSession httpSession);
}
