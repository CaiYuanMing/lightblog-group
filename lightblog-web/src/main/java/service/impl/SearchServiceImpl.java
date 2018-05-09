package service.impl;


import mapper.WorkInfoMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.WorkInfo;
import pojo.WorkInfoExample;
import service.SearchService;
import service.UtilService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {
    private  static Logger log = Logger.getLogger(SearchServiceImpl.class);
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private UtilService utilService;

    public String getSearchTips(String query,String scope,String userId, HttpSession httpSession) {
        log.info("----搜索输入提示处理：start");

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");

        if (scope.equals("all")){
            workInfoExample.or().andWorkTitleLike("%"+query+"%");
        }else {
            if (userId==null||userId.equals("")){
                userId = (String)httpSession.getAttribute("userId");
            }
            workInfoExample.or().andWorkUserIdEqualTo(userId).andWorkTitleLike("%"+query+"%");
        }

        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);
        Set<String> searchTipSet = new HashSet<String>();
        //分类结果去重
        for (int i = 0; i < workInfoList.size(); i++) {
            searchTipSet.add(workInfoList.get(i).getWorkTitle());
        }
        List<String> searchTipList = new ArrayList<String>(searchTipSet);
        //结果按规范封装
        StringBuffer searchTips = utilService.formatDataForSearchTip(searchTipList);
        log.info("返回数据："+searchTips.toString());
        log.info("----搜索输入提示处理：end");
        return searchTips.toString();
    }

    public List<Map<String, Object>> getWorkListByTitle(String title, String userId, HttpSession httpSession) {
        log.info("-----获取某人某标题的所有文章：start");
        List<Map<String, Object>> workListByTitle = new ArrayList<Map<String, Object>>();
        Map<String,Object> workMapByTitle = new HashMap<String, Object>();
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");
        if (userId==null||userId.equals("")){
            userId = (String)httpSession.getAttribute("userId");
        }
        if (title==null||title.equals("")){
            workInfoExample.or().andWorkUserIdEqualTo(userId);
        }else{
            workInfoExample.or().andWorkUserIdEqualTo(userId).andWorkTitleEqualTo(title);
        }
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);
        for (WorkInfo workInfo :
                workInfoList) {
            workMapByTitle.put("workId",workInfo.getWorkId());
            workMapByTitle.put("workTitle",workInfo.getWorkTitle());
            workListByTitle.add(workMapByTitle);
        }
        log.info("-----获取某人某标题的所有文章：end");
        return workListByTitle;
    }
}
