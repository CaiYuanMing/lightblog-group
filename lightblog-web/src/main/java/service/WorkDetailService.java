package service;

import pojo.ComitListItemBean;
import pojo.WorkTemp;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface WorkDetailService {
    WorkTemp getWorkDetailByWorkId(String workId, HttpSession httpSession);
    Map<String,Object> deleteWorkById(String workId,HttpSession httpSession);
    Map<String,Object> thumbUpToggle(String actorId,String workId,HttpSession httpSession);
    Map<String,Object> shareToggle(String actorId,String workId,HttpSession httpSession);
    boolean getIsThumbUp(String actorId,String workId,HttpSession httpSession);
    boolean getIsShare(String actorId,String workId,HttpSession httpSession);
    Map<String,Object> commitSubmit(String actorId,String actType,String toWorkId,String actContent,HttpSession httpSession);
    Map<String,Object> reCommitSubmit(String toActId,String commit,HttpSession httpSession);
    List<ComitListItemBean> getCommitListByWorkId(String workId,HttpSession httpSession);
    String getCommitAuthority(String actorId,HttpSession httpSession);
    Map<String,Object> deleteCommit(String actId);
    Map<String,Object> editCommitSubmit(String actId,String commit);
    String getToActContentByToActId(int toActId);
}
