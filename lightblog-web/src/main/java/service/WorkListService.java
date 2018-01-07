package service;

import pojo.UserTemp;
import pojo.WorkListItem;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface WorkListService {
    List<WorkListItem> getWorkListByOwnerId(String ownerId, HttpSession httpSession);
    UserTemp getUserTempByUserId(String userId, HttpSession httpSession);
}
