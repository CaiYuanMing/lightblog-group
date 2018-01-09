package service;

import pojo.WorkTemp;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface WorkDetailService {
    WorkTemp getWorkDetailByWorkId(String workId, HttpSession httpSession);
    Map<String,Object> deleteWorkById(String workId,HttpSession httpSession);
}
