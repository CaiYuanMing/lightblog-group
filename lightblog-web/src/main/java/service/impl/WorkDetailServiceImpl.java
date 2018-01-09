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

import service.WorkDetailService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class WorkDetailServiceImpl implements WorkDetailService {
    private  static Logger log = Logger.getLogger(WorkServiceImpl.class);
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private WorkContentMapper workContentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TagWorkMapper tagWorkMapper;
    public WorkTemp getWorkDetailByWorkId(String workId, HttpSession httpSession) {
        log.info("通过workId获取workDetail处理：start");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        WorkInfo workInfo = workInfoMapper.selectByPrimaryKey(Integer.parseInt(workId));
        WorkTemp workTemp = (WorkTemp)applicationContext.getBean("workTemp");

        workTemp.workId = workInfo.getWorkId();
        workTemp.workTitle = workInfo.getWorkTitle();
        workTemp.workGeneratesTime = simpleDateFormat.format(workInfo.getWorkGeneratesTime());
        workTemp.workCategory = workInfo.getWorkCategory();
        workTemp.workBrowseSum = workInfo.getWorkBrowseSum();
        workTemp.workUserId = workInfo.getWorkUserId();

        WorkContent workContent = workContentMapper.selectByPrimaryKey(Integer.parseInt(workId));
        workTemp.workContentHtml = workContent.getWorkContentHtml();
        workTemp.workContentMarkdown = workContent.getWorkContentMarkdown();

        User user = userMapper.selectByPrimaryKey(workInfo.getWorkUserId());
        workTemp.authorName = user.getUserName();

        TagWorkExample tagWorkExample  = (TagWorkExample)applicationContext.getBean("tagWorkExample");
        tagWorkExample.or().andWorkIdEqualTo(Integer.parseInt(workId));
        List<TagWorkKey> tagWorkKeyList = tagWorkMapper.selectByExample(tagWorkExample);

        for (TagWorkKey tagWorkKey :
               tagWorkKeyList ) {
            workTemp.tagList.add(tagWorkKey.getTagName());
        }
        log.info("通过workId获取workDetail处理：end");
        return workTemp;
    }
}
