package service.impl;

import mapper.TagWorkMapper;
import mapper.UserMapper;
import mapper.WorkContentMapper;
import mapper.WorkInfoMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.*;
import service.MainPageService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
@Service
public class MainPageServiceImpl implements MainPageService {
    private  static Logger log = Logger.getLogger(WorkServiceImpl.class);
    private  int count;
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private WorkContentMapper workContentMapper;
    @Autowired
    private TagWorkMapper tagWorkMapper;
    @Autowired
    private UserMapper userMapper;

    public List<WorkTemp> getWorkListByOwnerId(String ownerId,HttpSession httpSession) {
        log.info("----文章list获取处理：start");
        List<WorkTemp> workTempList = new ArrayList<WorkTemp>();
        WorkTemp workTemp ;
        WorkContent workContent;
        TagWorkExample tagWorkExample;
        List<TagWorkKey> tagWorkKeyList;

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");

        workInfoExample.or().andWorkUserIdEqualTo(ownerId);
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);

        for (int i = 0; i < workInfoList.size(); i++) {
            workTemp = (WorkTemp)applicationContext.getBean("workTemp");
            workTemp.workId = workInfoList.get(i).getWorkId();
            workTemp.workGeneratesTime = workInfoList.get(i).getWorkGeneratesTime();
            workTemp.workBrowseSum = workInfoList.get(i).getWorkBrowseSum();
            workTemp.workCategory = workInfoList.get(i).getWorkCategory();
            workTemp.workTitle = workInfoList.get(i).getWorkTitle();

            workContent = workContentMapper.selectByPrimaryKey(workTemp.workId);
            workTemp.workContentHtml = workContent.getWorkContentHtml();

            log.info("一个workTemp加入workTempList\nworkId = "+ workTemp.workId
                    + "\nworkGeneratesTime = "+workTemp.workGeneratesTime+"\nworkBrowseSum = "+workTemp.workBrowseSum
                    +"\nworkCategory = "+ workTemp.workCategory+"\nworkTitle = "+ workTemp.workTitle);

            tagWorkExample = (TagWorkExample)applicationContext.getBean("tagWorkExample");
            tagWorkExample.or().andWorkIdEqualTo(workTemp.workId);
            tagWorkKeyList = tagWorkMapper.selectByExample(tagWorkExample);
            for (int j = 0; j < tagWorkKeyList.size(); j++) {
                workTemp.tagList.add(tagWorkKeyList.get(j).getTagName());
                log.info(tagWorkKeyList.get(j).getTagName());
            }
            log.info("\nworkContentHtml = "+ workTemp.workContentHtml);
            workTempList.add(workTemp);
    }

        log.info("----文章list获取处理：end");
        return workTempList;
    }

    public UserTemp getUserTempByUserId(String userId,HttpSession httpSession) {
        log.info("----usertemp获取处理：start");

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        UserTemp userTemp = (UserTemp) applicationContext.getBean("userTemp");

        User user = userMapper.selectByPrimaryKey(userId);

        userTemp.userName = user.getUserName();
        userTemp.userIntroduction = user.getUserIntroduction();
        userTemp.userHeadiconPath = user.getUserHeadiconPath();

        log.info("返回userTemp: userName = "+userTemp.userName+"\nuserIntroduction = "+userTemp.userIntroduction+"\nuserHeadiconPath = "+userTemp.userHeadiconPath);

        log.info("----usertemp获取处理：end");
        return userTemp;
    }
}
