package service;

import pojo.UserTemp;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface TagListService {
    List<Map<String,Object>> getTagListByOwnerId(String ownerId, HttpSession httpSession);
    UserTemp getUserTempByUserId(String userId, HttpSession httpSession);
}
