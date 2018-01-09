package service.impl;

import controller.WorkListController;
import mapper.WorkInfoMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.UserTemp;
import pojo.WorkInfo;
import pojo.WorkInfoExample;
import service.CategoryListService;
import service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CategoryListServiceImpl implements CategoryListService {
    private  static Logger log = Logger.getLogger(WorkListController.class);
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private UserService userService;
    public Map<String, Object> getCategoryListByOwnerId(String ownerId, HttpSession httpSession) {
        log.info("---获取分类Map处理：start");
        Map<String, Object> resultMap = new HashMap<String,Object>();
        List<Map<String, Object>> categoryList = new ArrayList<Map<String, Object>>();//存放分类map列表
        Map<String, Object> categoryListItemMap = new HashMap<String, Object>();//存放分类信息：分类名称+权值
        String categoryName = "";
        String category;
        int count = 0;
        int categorySum = 0;
        //获取owner的所有文章信息
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");
        workInfoExample.or().andWorkUserIdEqualTo(ownerId);
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);
        for (int i = 0; i < workInfoList.size(); i++) {
            category = workInfoList.get(i).getWorkCategory();
            if (i==0){
                categoryName = category;
                categoryListItemMap.put("categoryName",categoryName);categorySum++;
                count++;
            }else if (categoryName.equals(category)){
                count++;
            }else {
                categoryListItemMap.put("sum",count);
                categoryList.add(categoryListItemMap);
                categoryListItemMap = new HashMap<String, Object>();
                categoryName = category;
                categoryListItemMap.put("categoryName",categoryName);categorySum++;
                count = 0;
            }
        }
        categoryListItemMap.put("sum",count);
        categoryList.add(categoryListItemMap);

        resultMap.put("categorySum",categorySum);
        resultMap.put("categoryList",categoryList);
        log.info("---获取分类Map处理：end");
        return resultMap;
    }

    public UserTemp getUserTempByUserId(String userId, HttpSession httpSession) {
        return userService.getUserTempByUserId(userId,httpSession);
    }
}
