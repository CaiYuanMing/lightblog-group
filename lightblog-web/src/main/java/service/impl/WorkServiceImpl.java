package service.impl;

import mapper.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.*;
import service.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkServiceImpl implements WorkService {
    private static Logger log = Logger.getLogger(WorkServiceImpl.class);
    int count;
    @Autowired
    private UtilService utilService;
    @Autowired
    private InteractService interactService;
    @Autowired
    private WorkInfo workInfo;
    @Autowired
    private WorkInfoMapper workInfoMapper;
    @Autowired
    private WorkContent workContent;
    @Autowired
    private WorkContentMapper workContentMapper;
    @Autowired
    private TagWorkKey tagWorkKey;
    @Autowired
    private TagWorkMapper tagWorkMapper;
    @Autowired
    private  About about;
    @Autowired
    private AboutMapper aboutMapper;
    @Autowired
    private WorkDetailService workDetailService;
    @Autowired
    private AboutService aboutService;

    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private InteractMapper interactMapper;

    //work_info表-CRUD
    //插入
    public int insertWorkInfo(String workUserId,String workCategory,String workTitle) {
        log.info("insertWorkInfo接收参数： workUserId = "+workUserId+"\n workCategory = "+workCategory+"\nworkTitle = "+workTitle);
        workInfo.setWorkId(null);
        workInfo.setWorkUserId(workUserId);
        workInfo.setWorkGeneratesTime(new Date());
        workCategory = workCategory.trim();
        workInfo.setWorkCategory(workCategory);
        workInfo.setWorkBrowseSum(0);
        workInfo.setWorkTitle(workTitle);
        count = workInfoMapper.insertSelective(workInfo);
        log.info("插入work_info表，"+count+"条记录受到影响！插入的自增workid = "+workInfo.getWorkId());
        return workInfo.getWorkId();
    }
    //修改
    public  int updateWorkInfo(Integer workId, String workCategory,String workTitle) {
        log.info("updateWorkInfo接收参数： workId = "+workId+"\n workCategory = "+workCategory+"\nworkTitle = "+workTitle);
        workInfo.setWorkId(workId);
        workInfo.setWorkUserId(null);
        workInfo.setWorkGeneratesTime(null);
        workCategory = workCategory.trim();
        workInfo.setWorkCategory(workCategory);
        workInfo.setWorkBrowseSum(null);
        workInfo.setWorkTitle(workTitle);
        count = workInfoMapper.updateByPrimaryKeySelective(workInfo);
        log.info("更新work_info表，"+count+"条记录受到影响！");
        return count;
    }
    //浏览量加1
    public  int updateWorkInfoBrowseSum(Integer workId) {
        log.info("updateWorkInfoBrowseSum接收参数： workId = "+workId);
        workInfo = workInfoMapper.selectByPrimaryKey(workId);
        workInfo.setWorkUserId(null);
        workInfo.setWorkGeneratesTime(null);
        workInfo.setWorkTitle(null);
        workInfo.setWorkCategory(null);
        workInfo.setWorkBrowseSum(workInfo.getWorkBrowseSum()+1);
        count  = workInfoMapper.updateByPrimaryKeySelective(workInfo);
        log.info("更新work_info表文章浏览量，"+count+"条记录受到影响！");
        return count;
    }

    //删除
    public  int deleteWorkInfoByExample(WorkInfoExample workInfoExample) {
        count = workInfoMapper.deleteByExample(workInfoExample);
        log.info("依据条件删除work_info表记录，"+count+"条记录受到影响！");
        return count;
    }

    public  int deleteWorkInfoByPrimaryKey(Integer workId) {
        count = workInfoMapper.deleteByPrimaryKey(workId);
        log.info("依据workid: "+workId+"删除work_info表记录，"+count+"条记录受到影响！");
        return count;
    }

    public Map<String, Object> getWorkInfoByCategory(String userId, String category,HttpSession httpSession) {
        log.info("--根据userid category 获取所有相关文章相关信息: start");
        List<Map<String, Object>> workList = new ArrayList<Map<String, Object>>();
        Map<String,Object> workInfoMap;
        Map<String,Object> resultMap = new HashMap<String,Object>();

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");
        workInfoExample.or().andWorkUserIdEqualTo(userId).andWorkCategoryEqualTo(category);
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);
        for (WorkInfo workInfo:
             workInfoList) {
            workInfoMap = new HashMap<String, Object>();
            workInfoMap.put("workId",workInfo.getWorkId());
            workInfoMap.put("workTitle",workInfo.getWorkTitle());
            workList.add(workInfoMap);
        }

        log.info("--根据userid category 获取所有相关文章相关信息: end");
        resultMap.put("categoryName",category);
        resultMap.put("workList",workList);
        return resultMap;
    }

    public WorkTemp getWorkDetailByWorkId(String workId, HttpSession httpSession) {
        return workDetailService.getWorkDetailByWorkId(workId,httpSession);
    }

    public String getCategoryTip(String query, HttpSession httpSession) {
        log.info("----分类输入提示处理：start");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");

        workInfoExample.or().andWorkCategoryLike("%"+query+"%");
        List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);
        Set<String> categoryTipSet = new HashSet<String>();
        //分类结果去重
        for (int i = 0; i < workInfoList.size(); i++) {
            categoryTipSet.add(workInfoList.get(i).getWorkCategory());
        }
        List<String> categoryTipList = new ArrayList<String>(categoryTipSet);
        //结果按规范封装
        StringBuffer categoryTips = utilService.formatDataForSearchTip(categoryTipList);
        log.info("返回数据："+categoryTips.toString());
        log.info("----分类输入提示处理：end");
        return categoryTips.toString();
    }

    //work_content表-CRUD
    //插入
    public  int insertWorkContent(Integer workId, String workContentMarkdown, String workContentHtml) {
        log.info("insertWorkContent接收参数： workId = "+workId+"\n workContentMarkdown = "+workContentMarkdown+"\nworkContentHtml = "+workContentHtml);
        workContent.setWorkId(workId);
        workContent.setWorkContentMarkdown(workContentMarkdown);
        workContent.setWorkContentHtml(workContentHtml);
        count = workContentMapper.insert(workContent);
        log.info("插入work_content表，"+count+"条记录受到影响！");
        return count;
    }
    //修改
    public int updateWorkContent(Integer workId, String workContentMarkdown, String workContentHtml) {
        log.info("updateWorkContent接收参数： workId = "+workId+"\n workContentMarkdown = "+workContentMarkdown+"\n workContentHtml = "+workContentHtml);
        workContent.setWorkId(workId);
        workContent.setWorkContentMarkdown(workContentMarkdown);
        workContent.setWorkContentHtml(workContentHtml);
        count =  workContentMapper.updateByPrimaryKeySelective(workContent);
        log.info("更新workcontent表，"+count+"条记录受到影响！");
        return count;
    }
    //根据条件删除
    public int deleteWorkContentByExample(WorkContentExample workContentExample) {
        count = workContentMapper.deleteByExample(workContentExample);
        log.info("依据条件删除work_content表记录，"+count+"条记录受到影响！");
        return count;
    }
    //根据workId删除
    public int deleteWorkContentByPrimaryKey(Integer workId) {
        count = workContentMapper.deleteByPrimaryKey(workId);
        log.info("依据workid: "+workId+"删除work_content表记录，"+count+"条记录受到影响！");
        return count;
    }
 //tag_work表-CRUD
    //插入
    public int insertTagWork(String tagName, Integer workId) {
        log.info("insertTagWork接收参数： tagName = "+tagName+"\n workId = "+workId);
        tagName = tagName.trim();
        tagWorkKey.setTagName(tagName);
        tagWorkKey.setWorkId(workId);
        count = tagWorkMapper.insert(tagWorkKey);
        log.info("插入tag_work表，"+count+"条记录受到影响！");
        return count;
    }
    //根据条件删除
    public int deleteTagWorkByExample(TagWorkExample tagWorkExample) {
        count = tagWorkMapper.deleteByExample(tagWorkExample);
        log.info("依据条件删除tag_work表记录，"+count+"条记录受到影响！");
        return count;
    }
    //根据主键删除
    public int deleteTagWorkByPrimaryKey(TagWorkKey tagWorkKey) {
        count = tagWorkMapper.deleteByPrimaryKey(tagWorkKey);
        log.info("依据主键删除tag_work表记录，"+count+"条记录受到影响！");
        return count;
    }
    //综合情景处理tag_work表的操作
    public void tagEdit(Integer workId, List<String> tagList,HttpSession httpSession) {
        log.info("----进入tag综合处理");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        //删除，修改时删除的标签
        log.info("--删除修改时删除的标签");
        TagWorkExample tagWorkExample = (TagWorkExample)applicationContext.getBean("tagWorkExample");
        tagWorkExample.or().andWorkIdEqualTo(workId).andTagNameNotIn(tagList);
        deleteTagWorkByExample(tagWorkExample);
        //插入，修改时新加的标签
        log.info("--插入修改时新加的标签");
        TagWorkExample tagWorkExample2 = null;
        for (int i = 0; i < tagList.size() ; i++) {
            tagWorkExample2  = (TagWorkExample)applicationContext.getBean("tagWorkExample");
            tagWorkExample2.or().andWorkIdEqualTo(workId).andTagNameEqualTo(tagList.get(i));
           List<TagWorkKey> tagWorkKeyList = tagWorkMapper.selectByExample(tagWorkExample2);
           if (tagWorkKeyList.size()==0){
               insertTagWork(tagList.get(i),workId);
           }
        }
    }

    //标签输入提示
    public String getTagTip(String query, HttpSession httpSession) {
        log.info("----标签输入提示处理：start");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        TagWorkExample tagWorkExample = (TagWorkExample)applicationContext.getBean("tagWorkExample");
        tagWorkExample.or().andTagNameLike("%"+query+"%");
        List<TagWorkKey> tagWorkKeyList = tagWorkMapper.selectByExample(tagWorkExample);
        Set<String> tagTipSet = new HashSet<String>();
        //分类结果去重
        for (int i = 0; i < tagWorkKeyList.size(); i++) {
            tagTipSet.add(tagWorkKeyList.get(i).getTagName());
        }
        List<String> tagTipList = new ArrayList<String>(tagTipSet);
        //结果按规范封装
        StringBuffer tagTips = utilService.formatDataForSearchTip(tagTipList);
        log.info("返回数据："+tagTips.toString());
        log.info("----标签输入提示处理：end");
        return tagTips.toString();
    }

    public Map<String, Object> getWorkInfoByTag(String userId, String tag, HttpSession httpSession) {
        log.info("--根据userid = "+userId+" tag = "+tag+"获取所有相关文章相关信息: start");
        List<Map<String, Object>> workList = new ArrayList<Map<String, Object>>();
        Map<String,Object> workInfoMap;
        Map<String,Object> resultMap = new HashMap<String,Object>();

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        //获取使用该标签的文章信息
        TagWorkExample tagWorkExample = (TagWorkExample)applicationContext.getBean("tagWorkExample");
        tagWorkExample.or().andTagNameEqualTo(tag);
        List<TagWorkKey> tagWorkKeyList = tagWorkMapper.selectByExample(tagWorkExample);
        //获取该作者使用该标签的文章信息
        for (TagWorkKey tagWorkKey:tagWorkKeyList){
            WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");
            workInfoExample.or().andWorkUserIdEqualTo(userId).andWorkIdEqualTo(tagWorkKey.getWorkId());
            List<WorkInfo> workInfoList = workInfoMapper.selectByExample(workInfoExample);

            for (WorkInfo workInfo:workInfoList) {
                workInfoMap = new HashMap<String, Object>();
                workInfoMap.put("workId",workInfo.getWorkId());
                workInfoMap.put("workTitle",workInfo.getWorkTitle());
                workList.add(workInfoMap);
            }
        }

        log.info("--根据userid = "+userId+" tag = "+tag+"获取所有相关文章相关信息: end");
        resultMap.put("tagName",tag);
        resultMap.put("workList",workList);
        return resultMap;
    }

    public List<String> getTagListByWorkId(int workId, HttpSession httpSession) {
        log.info("getTagListByWorkId : begin");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        TagWorkExample tagWorkExample = (TagWorkExample)applicationContext.getBean("tagWorkExample");
        tagWorkExample.or().andWorkIdEqualTo(workId);
        List<TagWorkKey> tagWorkKeyList = tagWorkMapper.selectByExample(tagWorkExample);
        List<String> tagList = new ArrayList<String>();

        for (TagWorkKey tagWorkKey:tagWorkKeyList){
            tagList.add(tagWorkKey.getTagName());
        }
        log.info("getTagListByWorkId : end");
        return tagList;
    }

    //about表-CRUD
    //插入
    public int insertAbout(String aboutUserId, String aboutContentMarkdown, String aboutContentHtml) {
        log.info("insertAbout接收参数： aboutUserId = "+aboutUserId+"\n aboutContentMarkdown = "+aboutContentMarkdown+"\n aboutContentHtml = "+aboutContentHtml);
        about.setAboutUserId(aboutUserId);
        about.setAboutContentMarkdown(aboutContentMarkdown);
        about.setAboutContentHtml(aboutContentHtml);
        count = aboutMapper.insert(about);
        log.info("插入about表，"+count+"条记录受到影响！");
        return count;
    }
    //修改

    public int updateAbout(String aboutUserId, String aboutContentMarkdown, String aboutContentHtml) {
        log.info("updateAbout接收参数： aboutUserId = "+aboutUserId+"\n aboutContentMarkdown = "+aboutContentMarkdown+"\n aboutContentHtml = "+aboutContentHtml);
        about.setAboutUserId(aboutUserId);
        about.setAboutContentMarkdown(aboutContentMarkdown);
        about.setAboutContentHtml(aboutContentHtml);
        count = aboutMapper.updateByPrimaryKeySelective(about);
        log.info("根据主键更新about表，"+count+"条记录受到影响！");
        return count;
    }

    public  String getAboutMarkdown(HttpSession httpSession) {
        return aboutService.getAboutMarkdown(httpSession);
    }

    public String getSummaryByWorkId(int workId) {
        log.info("getSummaryByWorkId:begin");
        WorkContent workContent = workContentMapper.selectByPrimaryKey(workId);
        String workContentHtml = workContent.getWorkContentHtml();
        workContentHtml = workContentHtml.replaceAll("<p>"," ");
        workContentHtml = workContentHtml.replaceAll("<[^>]+>","");//HTML标签的正则表达式
        workContentHtml = workContentHtml.replaceAll("&nbsp","");
//        workContentHtml = workContentHtml.replaceAll("\t|\r|\n","");//空格回车换行符\s*|
        int endIndex;
        if (workContentHtml.length()<140){
            endIndex = workContentHtml.length()-1;
        }else{
            endIndex = 140;
        }
        log.info(workContentHtml.substring(0,endIndex)+"...");
        log.info("getSummaryByWorkId:end");
        return workContentHtml.substring(0,endIndex)+"...";
    }

    public int getPraiseSumByWorkId(int workId, HttpSession httpSession) {
        log.info("getPraiseSumByWorkId:begin");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        InteractExample interactExample = (InteractExample)applicationContext.getBean("interactExample");
        interactExample.or().andActTypeEqualTo("赞").andToWorkIdEqualTo(workId);
        log.info("getPraiseSumByWorkId:end");
        return interactMapper.countByExample(interactExample);
    }

    public int getComitSumByWorkId(int workId, HttpSession httpSession) {
        log.info("getComitSumByWorkId:begin");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        InteractExample interactExample = (InteractExample)applicationContext.getBean("interactExample");
        interactExample.or().andActTypeIn(Arrays.asList("回复","评论")).andToWorkIdEqualTo(workId);
        log.info("getComitSumByWorkId:end");
        return interactMapper.countByExample(interactExample);
    }

    public int getShareSumByWorkId(int workId, HttpSession httpSession) {
        log.info("getShareSumByWorkId:begin");
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        ShareExample shareExample = (ShareExample)applicationContext.getBean("shareExample");
        shareExample.or().andWorkidEqualTo(workId);
        log.info("getShareSumByWorkId:end");
        return shareMapper.countByExample(shareExample);
    }

    public List<WorkInfo> getWorkInfoListByUserIdList(List<String> userIdList, HttpSession httpSession) {
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        WorkInfoExample workInfoExample = (WorkInfoExample)applicationContext.getBean("workInfoExample");
        workInfoExample.setOrderByClause("work_generates_time desc");
        workInfoExample.or().andWorkUserIdIn(userIdList);
        return workInfoMapper.selectByExample(workInfoExample);
    }

    public List<WorkInfo> getWorkInfoListByBrowerSum(HttpSession httpSession) {
        log.info("根据浏览量所有文章排序查找，开始");
        WorkInfoExample workInfoExample = (WorkInfoExample)utilService.getBeanFromApplicationContext(httpSession,"workInfoExample");
        workInfoExample.setOrderByClause("work_browse_sum desc");
        log.info("根据浏览量所有文章排序查找，结束");
        return workInfoMapper.selectByExample(workInfoExample);
    }

    public List<WorkInfo> getWorkInfoListByTitleKey(String titleKey, HttpSession httpSession) {
        log.info("getWorkInfoListByTitleKey:begin");
        WorkInfoExample workInfoExample = (WorkInfoExample)utilService.getBeanFromApplicationContext(httpSession,"workInfoExample");
        workInfoExample.or().andWorkTitleLike(titleKey);

        log.info("getWorkInfoListByTitleKey:end");
        return workInfoMapper.selectByExample(workInfoExample);
    }

    public String getOwnerIdByWorkId(int workId, HttpSession httpSession) {
        WorkInfo workInfo = workInfoMapper.selectByPrimaryKey(workId);
        return workInfo.getWorkUserId();
    }

    public boolean getIsThumbUp(String actorId, String workId, HttpSession httpSession) {
        if (interactMapper.countByExample(interactService.getInteractExampleForThumbUp(actorId,workId,httpSession))==1){
            log.info("已点赞");
            return true;
        }else {
            log.info("未点赞");
            return false;
        }
    }

    public boolean getIsShare(String actorId, String workId, HttpSession httpSession) {
        if (shareMapper.countByExample(interactService.getInteractExampleForShare(actorId,workId,httpSession))==1){
            log.info("已推荐");
            return true;
        }else {
            log.info("未推荐");
            return false;
        }
    }
}
