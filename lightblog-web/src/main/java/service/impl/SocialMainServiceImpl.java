package service.impl;

import controller.WorkListController;
import mapper.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.*;
import service.SocialMainService;
import service.UserService;
import service.UtilService;
import service.WorkService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SocialMainServiceImpl implements SocialMainService{
    private  static Logger log = Logger.getLogger(SocialMainServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private WorkService workService;
    @Autowired
    private UtilService utilService;

    public List<WorkLiteForSocialMain> getWorkTempListForSocialMainByUser(HttpSession httpSession) {

        String userId = (String) httpSession.getAttribute("userId");
        List<String> userIdList = new ArrayList<String>();
        userIdList.add(userId);
        for (Follow follow:userService.getFollowListByUserId(userId,httpSession)){
            userIdList.add(follow.getFollowedUserId());
        }
        log.info("当前用户:"+userId+"和其关注人id为："+userIdList.toString());
        log.info("开始查找id list中用户的文章");

        List<WorkInfo> workInfoList = workService.getWorkInfoListByUserIdList(userIdList,httpSession);

        return getWorkTempListForSocialMainByWorkInfoList(workInfoList,httpSession);
    }

    public List<WorkLiteForSocialMain> getWorkTempListForSocialMain(HttpSession httpSession) {

        List<WorkInfo> workInfoList = workService.getWorkInfoListByBrowerSum(httpSession);

        return getWorkTempListForSocialMainByWorkInfoList(workInfoList,httpSession);
    }

    public List<WorkLiteForSocialMain> getWorkTempListForSocialMainByWorkInfoList(List<WorkInfo> workInfoList, HttpSession httpSession) {
        log.info("getWorkTempListForSocialMainByWorkInfoList :begin");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        List<WorkLiteForSocialMain> workLiteForSocialMainList = new ArrayList<WorkLiteForSocialMain>();

        for (WorkInfo workInfo:workInfoList){
            WorkLiteForSocialMain workLiteForSocialMain = (WorkLiteForSocialMain)applicationContext.getBean("workLiteForSocialMain");
            workLiteForSocialMain.setAuthorId(workInfo.getWorkUserId());
            workLiteForSocialMain.setAuthorHeadIconPath(userService.getUserHeadIconPathByUserId(workInfo.getWorkUserId()));
            workLiteForSocialMain.setWorkId(workInfo.getWorkId());
            workLiteForSocialMain.setAuthorName(userService.getUserNameByUserId(workInfo.getWorkUserId()));
            workLiteForSocialMain.setTitle(workInfo.getWorkTitle());
            workLiteForSocialMain.setWorkGenerateTime(utilService.getFormatGenerateTime("yyyy-MM-dd HH:mm:ss",workInfo.getWorkGeneratesTime()));
            workLiteForSocialMain.setCategory(workInfo.getWorkCategory());
            //标签
            workLiteForSocialMain.setTagList(workService.getTagListByWorkId(workInfo.getWorkId(),httpSession));
            //摘要
            workLiteForSocialMain.setSummary(workService.getSummaryByWorkId(workInfo.getWorkId()));
            //浏览量
            workLiteForSocialMain.setBrowserSum(workInfo.getWorkBrowseSum());

            //评论数
            workLiteForSocialMain.setComitSum(workService.getComitSumByWorkId(workInfo.getWorkId(),httpSession));
            //赞数
            workLiteForSocialMain.setPraiseSum(workService.getPraiseSumByWorkId(workInfo.getWorkId(),httpSession));
            //转发数
            workLiteForSocialMain.setShareSum(workService.getShareSumByWorkId(workInfo.getWorkId(),httpSession));

            log.info(workLiteForSocialMain);

            workLiteForSocialMainList.add(workLiteForSocialMain);

        }
        return workLiteForSocialMainList;
    }

    public List<WorkLiteForSocialMain> getWorkTempListForSocialMainByTitleKey(String titleKey, HttpSession httpSession) {
        List<WorkInfo> workInfoList = workService.getWorkInfoListByTitleKey(titleKey,httpSession);
        return getWorkTempListForSocialMainByWorkInfoList(workInfoList,httpSession);
    }
}
