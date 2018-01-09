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
import service.WorkService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private WorkService workService;
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

    public Map<String, Object> deleteWorkById(String workId, HttpSession httpSession) {
        log.info("删除文章：start workId="+workId);
        int workInfoCount = workService.deleteWorkInfoByPrimaryKey(Integer.parseInt(workId));
//        int workContentCount = workService.deleteWorkContentByPrimaryKey(Integer.parseInt(workId));
        Map<String,Object> resultMap = new HashMap<String,Object>();
//        ServletContext sc = httpSession.getServletContext();
//        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
//        TagWorkExample tagWorkExample = (TagWorkExample)applicationContext.getBean("tagWorkExample");
//        tagWorkExample.or().andWorkIdEqualTo(Integer.parseInt(workId));
//        int tagWorkCount = workService.deleteTagWorkByExample(tagWorkExample);
        if(workInfoCount==1){
            resultMap.put("outcome","success");
        }else {
            resultMap.put("outcome","fail");
            resultMap.put("msg","系统故障，删除失败！");
        }
        return resultMap;
    }
}
