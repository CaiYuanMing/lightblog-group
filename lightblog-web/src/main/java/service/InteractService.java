package service;

import pojo.InteractExample;
import pojo.ShareExample;

import javax.servlet.http.HttpSession;

public interface InteractService {
 int getToReadInterActSumByUser(HttpSession httpSession);
 InteractExample getInteractExampleForThumbUp(String actorId, String workId, HttpSession httpSession);
 ShareExample getInteractExampleForShare(String actorId, String workId, HttpSession httpSession);
}
