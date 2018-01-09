package service;

import pojo.WorkTemp;

import javax.servlet.http.HttpSession;

public interface WorkDetailService {
    WorkTemp getWorkDetailByWorkId(String workId, HttpSession httpSession);
}
