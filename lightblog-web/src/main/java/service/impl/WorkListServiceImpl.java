package service.impl;

import controller.WorkListController;
import mapper.UserMapper;
import mapper.WorkInfoMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.*;
import service.UserService;
import service.WorkListService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Service
public class WorkListServiceImpl implements WorkListService {
    private  static Logger log = Logger.getLogger(WorkListController.class);
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private UserService userService;

    public List<WorkListItem> getWorkListByOwnerId(String ownerId, HttpSession httpSession) {
        log.info("根据ownerId获取workList : start");
        List<WorkListItem> workList = new ArrayList<WorkListItem>();
        WorkListItem workListItem;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");

        workInfoExample.or().andWorkUserIdEqualTo(ownerId);
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);

        for (int i = 0; i < workInfoList.size(); i++) {
            workListItem = (WorkListItem)applicationContext.getBean("workListItem");
            workListItem.workId = workInfoList.get(i).getWorkId();
            workListItem.workGeneratesTime =  simpleDateFormat.format(workInfoList.get(i).getWorkGeneratesTime());
            workListItem.workTitle = workInfoList.get(i).getWorkTitle();
            workListItem.workBrowseSum = workInfoList.get(i).getWorkBrowseSum();
            workList.add(workListItem);
        }

        log.info("根据ownerId获取workList : end");
        return workList;
    }

    public UserTemp getUserTempByUserId(String userId,HttpSession httpSession) {
       return userService.getUserTempByUserId(userId,httpSession);
    }
}
