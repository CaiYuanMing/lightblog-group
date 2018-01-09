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
import service.WorkService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkListServiceImpl implements WorkListService {
    private  static Logger log = Logger.getLogger(WorkListController.class);
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkService workService;

    public  Map<String,Object> getWorkListMapByOwnerId(String ownerId, HttpSession httpSession) {
        log.info("根据ownerId获取workList : start");
        Map<String,Object> resultMap = new HashMap<String,Object>();//返回结果
        List< Map<String,Object>> workMapList = new ArrayList< Map<String,Object>>();//封装文章内容的list

        List< Map<String,Object>> workMonthList = new ArrayList< Map<String,Object>>();//封装具体年的文章内容 - List
        Map<String,Object> yearMap = new HashMap<String,Object>();//某年年份+某年文章内容 - Map
        List<WorkListItem> workDayList = new ArrayList<WorkListItem>();//封装具体月的文章内容 - List
        Map<String,Object> monthMap = new HashMap<String,Object>();//某月份+某月文章内容 - Map

        Date dateGet;//存放当前遍历到的文章的保存时间
        String yeahNumber; //存放当前遍历到的文章的保存时间，的年份
        String yeahMapNumber = "";//存放当前容器的年份
        String monthNumber;//存放当前遍历到的文章的保存时间，的月份
        String monthMapNumber = "";//存放当前容器的月份
        WorkListItem workListItem;//存放当前遍历到的文章内容
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");//获取用来和文章标题一起显示的时间
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");//获取年份
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");//获取月份
        //获取owner的所有文章信息
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");
        workInfoExample.or().andWorkUserIdEqualTo(ownerId);
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);

        //将文章总数放入resultMap
        resultMap.put("workSum",workInfoList.size());

        //文章以月份为单位存入monthMap,monthMap再以年份为单位存入yearMap,最后yearMap存入workList
        for (int i = 0; i < workInfoList.size(); i++) {
            //值封装
            workListItem = (WorkListItem)applicationContext.getBean("workListItem");
            workListItem.workId = workInfoList.get(i).getWorkId();
            workListItem.workGeneratesTime =  simpleDateFormat.format(workInfoList.get(i).getWorkGeneratesTime());
            workListItem.workTitle = workInfoList.get(i).getWorkTitle();
            workListItem.workBrowseSum = workInfoList.get(i).getWorkBrowseSum();
            //workListItem封装
            dateGet = workInfoList.get(i).getWorkGeneratesTime();
            yeahNumber = yearFormat.format(dateGet);
            monthNumber = monthFormat.format(dateGet);
            if (i==0){
                yearMap.put("yearNumber",yeahNumber);
                yeahMapNumber = yeahNumber;
                monthMap.put("monthNumber",monthNumber);
                monthMapNumber = monthNumber;
            }else if (yeahNumber.equals(yeahMapNumber)){//同年
                    if (!monthNumber.equals(monthMapNumber)){//同年不同月，整理提交上个月，再开新月，放入文章，即把上个月提交到月map,再将操作的月map放入年list，新建一个
                        monthMap.put("dayList",workDayList);
                        workMonthList.add(monthMap);
                        //创建新的monthMap
                        monthMap = new HashMap<String,Object>();
                        monthMap.put("monthNumber",monthNumber);
                        monthMapNumber = monthNumber;
                        //创建新的以月为单位收集文章(WorkListItem)的List
                        workDayList = new ArrayList<WorkListItem>();
                    }
            }else {//不同年，整理提交上一年再开新一年和新一月
                monthMap.put("dayList",workDayList);
                workMonthList.add(monthMap);
                yearMap.put("monthList",workMonthList);
                workMapList.add(yearMap);
                //创建新的yearMap
                yearMap = new HashMap<String,Object>();
                yearMap.put("yeahNumber",yeahNumber);
                yeahMapNumber = yeahNumber;

                //创建新的以年为单位收集月map(monthMap)的List
                workMonthList = new ArrayList< Map<String,Object>>();
                //创建新的monthMap
                monthMap = new HashMap<String,Object>();
                monthMap.put("monthNumber",monthNumber);
                monthMapNumber = monthNumber;
                //创建新的以月为单位收集文章(WorkListItem)的List
                workDayList = new ArrayList<WorkListItem>();
            }
            //放入当前文章
            workDayList.add(workListItem);
        }
        //收尾
        monthMap.put("dayList",workDayList);
        workMonthList.add(monthMap);
        yearMap.put("monthList",workMonthList);
        workMapList.add(yearMap);

        resultMap.put("yearList",workMapList);

        return  resultMap;
    }

    public UserTemp getUserTempByUserId(String userId,HttpSession httpSession) {
       return userService.getUserTempByUserId(userId,httpSession);
    }

    public Map<String, Object> getWorkInfoByCategory(String userId, String category, HttpSession httpSession) {
        return workService.getWorkInfoByCategory(userId,category,httpSession);
    }
}
