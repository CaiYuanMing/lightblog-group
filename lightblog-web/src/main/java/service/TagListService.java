package service;

import pojo.UserTemp;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface TagListService {
    Map<String,Object> getTagListByOwnerId(String ownerId, HttpSession httpSession);
    UserTemp getUserTempByUserId(String userId, HttpSession httpSession);
}
