package service;

import pojo.UserTemp;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface CategoryListService {
    Map<String,Object> getCategoryListByOwnerId(String ownerId, HttpSession httpSession);
    UserTemp getUserTempByUserId(String userId, HttpSession httpSession);
}
