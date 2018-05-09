package service.impl;

import mapper.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.*;

import service.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkDetailServiceImpl implements WorkDetailService {
    private  static Logger log = Logger.getLogger(WorkServiceImpl.class);
    @Autowired
    private UtilService utilService;
    @Autowired
    private InteractService interactService;
    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private InteractMapper interactMapper;
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
    @Autowired
    private UserService userService;
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

    public Map<String,Object> thumbUpToggle(String actorId, String workId, HttpSession httpSession) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        int count;
        int workIdByInt = Integer.parseInt(workId);
        if (workService.getIsThumbUp(actorId,workId,httpSession)){
            log.info("执行取消赞操作！");
            count = interactMapper.deleteByExample(interactService.getInteractExampleForThumbUp(actorId,workId,httpSession));
            if (count == 1){
                resultMap.put("outcome","success");
                resultMap.put("msg","取消赞成功！");
            }else{
                resultMap.put("outcome","false");
                resultMap.put("msg","Opp,取消赞失败！");
            }
        }else{
            log.info("执行点赞操作！");
            Interact interact = (Interact)utilService.getBeanFromApplicationContext(httpSession,"interact");
            interact.setActGenerateTime(new Date());
            interact.setActorId(actorId);
            interact.setActType("赞");
            interact.setToActorId(workService.getOwnerIdByWorkId(workIdByInt,httpSession));
            interact.setToWorkId(workIdByInt);
            interact.setActIsRead(false);
            count = interactMapper.insert(interact);
            if (count == 1){
                resultMap.put("outcome","success");
                resultMap.put("msg","点赞成功！");
            }else{
                resultMap.put("outcome","false");
                resultMap.put("msg","Opp,点赞失败！");
            }
        }
        log.info("----点赞处理：end");
        return resultMap;
    }

    public Map<String, Object> shareToggle(String actorId, String workId, HttpSession httpSession) {
        int workIdByInt = Integer.parseInt(workId);
        int count;
        Map<String,Object> resultMap = new HashMap<String, Object>();
        if (workService.getIsShare(actorId,workId,httpSession)){
            log.info("已推荐，执行取消推荐操作！");
            count = shareMapper.deleteByExample(interactService.getInteractExampleForShare(actorId,workId,httpSession));
            if (count == 1){
                resultMap.put("outcome","success");
                resultMap.put("msg","取消推荐成功！");
            }else{
                resultMap.put("outcome","false");
                resultMap.put("msg","Opp,取消推荐失败！");
            }
        }else{
            log.info("未推荐，执行推荐操作！");
            Share share = (Share)utilService.getBeanFromApplicationContext(httpSession,"share");
            share.setShareTime(new Date());
            share.setUserid(actorId);
            share.setWorkid(workIdByInt);
            count = shareMapper.insert(share);
            if (count == 1){
                resultMap.put("outcome","success");
                resultMap.put("msg","推荐成功！");
            }else{
                resultMap.put("outcome","false");
                resultMap.put("msg","Opp,推荐失败！");
            }
        }
        log.info("----推荐处理：end");
        return resultMap;
    }

    public boolean getIsThumbUp(String actorId,String workId, HttpSession httpSession) {
        return workService.getIsThumbUp(actorId,workId,httpSession);
    }

    public boolean getIsShare(String actorId,String workId, HttpSession httpSession) {
        return workService.getIsShare(actorId,workId,httpSession);
    }

    public Map<String, Object> commitSubmit(String actorId, String actType, String toWorkId, String actContent, HttpSession httpSession) {
        int count;
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Interact interact = (Interact)utilService.getBeanFromApplicationContext(httpSession,"interact");
        interact.setActGenerateTime(new Date());
        interact.setActorId(actorId);
        interact.setActType(actType);
        interact.setToWorkId(Integer.parseInt(toWorkId));
        interact.setActContent(actContent);
        interact.setActIsRead(false);
        interact.setToActorId(workService.getOwnerIdByWorkId(Integer.parseInt(toWorkId),httpSession));
        log.info(interact);
        count = interactMapper.insert(interact);
        log.info(count);
        if (count == 1){
            resultMap.put("outcome","success");
            //封装新添加的完整评论信息
            SimpleCommitBean simpleCommitBean = (SimpleCommitBean)utilService.getBeanFromApplicationContext(httpSession,"simpleCommitBean");
            Interact interact1 = interactMapper.selectByPrimaryKey(interact.getActId());
            simpleCommitBean.setCommitEditAuthority("不能回复,能编辑、删除");
            simpleCommitBean.setActId(interact1.getActId());
            simpleCommitBean.setActorId(interact1.getActorId());
            simpleCommitBean.setActorName(userService.getUserNameByUserId(interact1.getActorId()));
            simpleCommitBean.setActorHeadIconPath(userService.getUserHeadIconPathByUserId(interact1.getActorId()));
            simpleCommitBean.setCommitContent(interact1.getActContent());
            simpleCommitBean.setActGenerateTime(utilService.getFormatGenerateTime("yyyy-MM-dd HH:mm:ss",interact1.getActGenerateTime()));

            resultMap.put("simpleCommitBean",simpleCommitBean);
        }else{
            resultMap.put("outcome","false");
        }

        log.info(resultMap);
        log.info("评论提交处理：end");
        return resultMap;
    }

    public Map<String, Object> reCommitSubmit(String toActId, String commit, HttpSession httpSession) {
        int count;
        log.info("toActId ="+toActId+" commit="+commit);
        Map<String,Object> resultMap = new HashMap<String, Object>();
        int toActIdByInt = Integer.parseInt(toActId);

        Interact interact = (Interact)utilService.getBeanFromApplicationContext(httpSession,"interact") ;
        interact.setActGenerateTime(new Date());
        interact.setActType("回复");
        interact.setActorId((String)httpSession.getAttribute("userId"));
        Interact interact1 = interactMapper.selectByPrimaryKey(toActIdByInt);
        //二级评论控制
        if (interact1.getToActId()==null){
            interact.setToActId(toActIdByInt);
        }else {
            interact.setToActId(interact1.getToActId());
        }
        log.info("interact1="+interact1);
        interact.setToActorId(interact1.getActorId());
        interact.setToWorkId(interact1.getToWorkId());
        interact.setToActContent(interact1.getActContent());
        interact.setActContent(commit);
        interact.setActIsRead(false);

        count = interactMapper.insert(interact);
        log.info(count);
        if (count == 1){
            resultMap.put("outcome","success");
            //封装完整的回复信息
            RecomitBean recomitBean = (RecomitBean)utilService.getBeanFromApplicationContext(httpSession,"recomitBean");
            Interact interact2 = interactMapper.selectByPrimaryKey(interact.getActId());
            recomitBean.setActId(interact2.getActId());
            recomitBean.setActorId(interact2.getActorId());
            recomitBean.setActorName(userService.getUserNameByUserId(interact2.getActorId()));
            recomitBean.setActorHeadIconPath(userService.getUserHeadIconPathByUserId(interact2.getActorId()));
            recomitBean.setToActorId(interact2.getToActorId());
            recomitBean.setToActorName(userService.getUserNameByUserId(interact2.getToActorId()));
            recomitBean.setActGenerateTime(utilService.getFormatGenerateTime("yyyy-MM-dd HH:mm:ss",interact2.getActGenerateTime()));
            recomitBean.setCommitContent(interact2.getActContent());
            recomitBean.setCommitEditAuthority("不能回复,能编辑、删除");
            recomitBean.setToActContent(interact2.getToActContent());

            resultMap.put("recomitBean",recomitBean);
        }else{
            resultMap.put("outcome","false");
        }
        log.info(resultMap);
        log.info("回复提交处理：end");
        return resultMap;
    }

    public List<ComitListItemBean> getCommitListByWorkId(String workId, HttpSession httpSession) {
        log.info("getCommitListByWorkId : start ");
        InteractExample interactExample = (InteractExample)utilService.getBeanFromApplicationContext(httpSession,"interactExample") ;
        int workIdByInt = Integer.parseInt(workId);
        interactExample.setOrderByClause("act_generate_time desc");
        interactExample.or().andActTypeEqualTo("评论").andToWorkIdEqualTo(workIdByInt);
        List<Interact> interactList = interactMapper.selectByExample(interactExample);
        List<ComitListItemBean> comitList = new ArrayList<ComitListItemBean>();
        SimpleCommitBean simpleCommitBean;
        RecomitBean recomitBean;
        List<RecomitBean> recomitBeanList;
        for (Interact interact:interactList){
            simpleCommitBean = (SimpleCommitBean)utilService.getBeanFromApplicationContext(httpSession,"simpleCommitBean");
            simpleCommitBean.setActId(interact.getActId());
            simpleCommitBean.setActorId(interact.getActorId());
            simpleCommitBean.setActGenerateTime(utilService.getFormatGenerateTime("yyyy-MM-dd HH:mm:ss",interact.getActGenerateTime()));
            simpleCommitBean.setCommitContent(interact.getActContent());
            simpleCommitBean.setActorName(userService.getUserNameByUserId(interact.getActorId()));
            simpleCommitBean.setActorHeadIconPath(userService.getUserHeadIconPathByUserId(interact.getActorId()));
            //评论权限
            simpleCommitBean.setCommitEditAuthority(getCommitAuthority(interact.getActorId(),httpSession));
            ComitListItemBean comitListItemBean = (ComitListItemBean)utilService.getBeanFromApplicationContext(httpSession,"comitListItemBean");
            comitListItemBean.setSimpleCommitBean(simpleCommitBean);
            //副评论封装
            interactExample = (InteractExample)utilService.getBeanFromApplicationContext(httpSession,"interactExample");
            interactExample.setOrderByClause("act_generate_time desc");
            interactExample.or().andToActIdEqualTo(interact.getActId());
            interactList = interactMapper.selectByExample(interactExample);
            recomitBeanList = new ArrayList<RecomitBean>();
            for (Interact interact1:interactList){
               recomitBean = (RecomitBean)utilService.getBeanFromApplicationContext(httpSession,"recomitBean");
               recomitBean.setActId(interact1.getActId());
               recomitBean.setActorId(interact1.getActorId());
               recomitBean.setActorName(userService.getUserNameByUserId(interact1.getActorId()));
               recomitBean.setActorHeadIconPath(userService.getUserHeadIconPathByUserId(interact1.getActorId()));
               recomitBean.setToActorId(interact1.getToActorId());
               recomitBean.setToActorName(userService.getUserNameByUserId(interact1.getToActorId()));
               recomitBean.setToActContent(interact1.getToActContent());
               recomitBean.setCommitContent(interact1.getActContent());
               recomitBean.setActGenerateTime(utilService.getFormatGenerateTime("yyyy-MM-dd HH:mm:ss",interact1.getActGenerateTime()));
                //评论权限
                recomitBean.setCommitEditAuthority(getCommitAuthority(interact1.getActorId(),httpSession));
                recomitBeanList.add(recomitBean);
            }
            comitListItemBean.setRecomitBeanList(recomitBeanList);
            comitList.add(comitListItemBean);
        }
        log.info(comitList);
        log.info("getCommitListByWorkId : end ");
        return comitList;
    }

    public String getCommitAuthority(String actorId,HttpSession httpSession) {
        String commitEditAuthority;
        if(httpSession.getAttribute("userId")==null){
            commitEditAuthority = "不能回复";
        }else {
            String visitorId = (String)httpSession.getAttribute("userId");
            if (visitorId.equals(actorId)){
                commitEditAuthority = "不能回复,能编辑、删除";
            }else {
                commitEditAuthority = "能回复，不能编辑、删除";
            }
        }
        return commitEditAuthority;
    }

    public Map<String, Object> deleteCommit(String actId) {
        log.info("deleteCommit : begin");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int count = interactMapper.deleteByPrimaryKey(Integer.parseInt(actId));
        if (count==1){
            resultMap.put("outcome","success");
        }else {
            resultMap.put("outcome","false");
        }
        log.info(resultMap);
        log.info("deleteCommit : end");
        return resultMap;
    }

    public Map<String, Object> editCommitSubmit(String actId, String commit) {
        log.info("editCommitSubmit: begin");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Interact interact = interactMapper.selectByPrimaryKey(Integer.parseInt(actId));
        interact.setActContent(commit);
        int count = interactMapper.updateByPrimaryKey(interact);
        if (count==1){
            resultMap.put("outcome","success");
            resultMap.put("commit",interact.getActContent());
        }else {
            resultMap.put("outcome","false");
        }
        log.info(resultMap);
        log.info("editCommitSubmit: end");
        return resultMap;
    }

    public String getToActContentByToActId(int toActId) {
        Interact interact = interactMapper.selectByPrimaryKey(toActId);
        return interact.getActContent();
    }
}
