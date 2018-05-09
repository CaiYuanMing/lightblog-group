package service.impl;

import mapper.InteractMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.InteractExample;
import pojo.ShareExample;
import service.InteractService;
import service.UtilService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@Service
public class InteractServiceImpl implements InteractService {
    private  static Logger log = Logger.getLogger(InteractServiceImpl.class);
    @Autowired
    private InteractMapper interactMapper;
    @Autowired
    private UtilService utilService;
    public int getToReadInterActSumByUser(HttpSession httpSession) {
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        //查询当前用户所有未读互动的数量
        InteractExample interactExample = (InteractExample) applicationContext.getBean("interactExample");
        interactExample.or().andActIsReadEqualTo(false).andToActorIdEqualTo((String)httpSession.getAttribute("userId"));
        int toReadInterActSum = interactMapper.countByExample(interactExample);
        log.info("当前用户未读互动数量，查询得：toReadInterActSum = "+toReadInterActSum);
        return toReadInterActSum ;
    }

    public InteractExample getInteractExampleForThumbUp(String actorId, String workId, HttpSession httpSession) {
        int workIdByInt = Integer.parseInt(workId);
        InteractExample interactExample = (InteractExample)utilService.getBeanFromApplicationContext(httpSession,"interactExample");
        interactExample.or().andToWorkIdEqualTo(workIdByInt).andActorIdEqualTo(actorId).andActTypeEqualTo("赞");
        return interactExample;
    }

    public ShareExample getInteractExampleForShare(String actorId, String workId, HttpSession httpSession) {
        int workIdByInt = Integer.parseInt(workId);
        ShareExample shareExample = (ShareExample)utilService.getBeanFromApplicationContext(httpSession,"shareExample") ;
        shareExample.or().andUseridEqualTo(actorId).andWorkidEqualTo(workIdByInt);
        return shareExample;
    }
}
