
package service.impl;

import controller.WorkListController;
import mapper.TagWorkMapper;
import mapper.WorkInfoMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.*;
import service.TagListService;
import service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class TagListServiceImpl implements TagListService {
    private  static Logger log = Logger.getLogger(WorkListController.class);
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private TagWorkMapper tagWorkMapper;
    @Autowired
    private UserService userService;
    public  Map<String,Object> getTagListByOwnerId(String ownerId, HttpSession httpSession) {
        log.info("获取 ownerId = "+ownerId+" 的所有tag，处理开始");
        //获取owner的所有文章信息
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");
        workInfoExample.or().andWorkUserIdEqualTo(ownerId);
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);
        //获取owner的所有tag信息
        Map<String, Object> resultMap = new HashMap<String,Object>();//存放返回结果
        List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();//存放tagListItemMap
        Map<String, Object> tagMap = new HashMap<String, Object>();//存放tag信息：tag名称+权值

        String tagName;int tagSum;int tagCount = 0;

        for (WorkInfo workInfo:workInfoList){
            //根据owner的workId查找对应tag
            TagWorkExample tagWorkExample = (TagWorkExample)applicationContext.getBean("tagWorkExample");
            tagWorkExample.or().andWorkIdEqualTo(workInfo.getWorkId());
            List<TagWorkKey> tagWorkKeyList = tagWorkMapper.selectByExample(tagWorkExample);
            for (TagWorkKey tagWorkKey : tagWorkKeyList){
                tagName = tagWorkKey.getTagName();
                if (tagMap.get(tagName)==null){
                    log.info("发现新标签： "+tagName);
                    tagMap.put(tagName,1);
                }else{
                    tagSum = Integer.parseInt(tagMap.get(tagName).toString());
                    tagMap.put(tagName,++tagSum);
                }
            }
        }
        //格式化tag信息
        for (String key:tagMap.keySet()){
            Map<String,Object> tagListItemMap = new HashMap<String, Object>();
            tagListItemMap.put("tagName",key);
            tagListItemMap.put("tagSum",tagMap.get(key));
            tagList.add(tagListItemMap);
            tagCount++;
        }
        log.info("打印tagList： "+tagList.toString());
        resultMap.put("tagCount",tagCount);
        resultMap.put("tagList",tagList);
        log.info("获取 ownerId = "+ownerId+" 的所有tag，处理结束");
        return resultMap;
    }
    public UserTemp getUserTempByUserId(String userId, HttpSession httpSession) {
        return userService.getUserTempByUserId(userId,httpSession);
    }
}
