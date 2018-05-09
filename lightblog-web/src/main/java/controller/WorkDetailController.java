package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.ComitListItemBean;
import pojo.WorkTemp;
import service.WorkDetailService;
import service.WorkService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("workdetail")
public class WorkDetailController {
    private  static Logger log = Logger.getLogger(WorkDetailController.class);
    @Autowired
    private WorkDetailService workDetailService;
    @Autowired
    private WorkService workService;
    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> init(HttpSession httpSession){
        log.info("-----处理文章详情页初始化：start");
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String visitType = (String) httpSession.getAttribute("visitType");
        WorkTemp workTemp = (WorkTemp)httpSession.getAttribute("workTemp") ;
        resultMap.put("visitType",visitType);
        resultMap.put("workTemp",workTemp);
        resultMap.put("commitList",(List<ComitListItemBean>)httpSession.getAttribute("commitList"));
        resultMap.put("pageNature",(String)httpSession.getAttribute("pageNature"));
        if (visitType.equals("visitor")){
            resultMap.put("ownerId",(String)httpSession.getAttribute("ownerId"));
        }
        resultMap.put("isThumbUp",Boolean.parseBoolean(httpSession.getAttribute("isThumbUp").toString()));
        resultMap.put("isShare",Boolean.parseBoolean(httpSession.getAttribute("isShare").toString()));
        log.info("-----处理文章详情页初始化：：end");
        return resultMap;
    }

    @RequestMapping("jumpToWorkDetail")
    public void  jumpToWorkDetail(String ownerId,String workId,String pageNature,HttpServletResponse response, HttpSession httpSession) throws IOException {
        httpSession.setAttribute("pageNature",pageNature);
        WorkTemp workTemp = workDetailService.getWorkDetailByWorkId(workId,httpSession);
        if (httpSession.getAttribute("userId")!=null){
            String userId = (String)httpSession.getAttribute("userId");
            httpSession.setAttribute("isThumbUp",workDetailService.getIsThumbUp(userId,workId,httpSession));
            httpSession.setAttribute("isShare",workDetailService.getIsShare(userId,workId,httpSession));
        }else {//未登录默认未点赞未推荐
            httpSession.setAttribute("isThumbUp",false);
            httpSession.setAttribute("isShare",false);
        }
        if(ownerId==null){
            log.info("--访问类型为：master");
            httpSession.setAttribute("visitType","master");
        }else {
            log.info("--访问类型为：visitor");
            log.info("ownerId = "+ownerId);
            httpSession.setAttribute("ownerId",ownerId);
            httpSession.setAttribute("visitType","visitor");
          }
        httpSession.setAttribute("workTemp",workTemp);
        httpSession.setAttribute("commitList",workDetailService.getCommitListByWorkId(workId,httpSession));
        response.sendRedirect("../workDetail.html");
    }

    @RequestMapping("deleteWorkById")
    @ResponseBody
    public Map<String,Object> userIdCheck(String workId,HttpSession httpSession){
        log.info("----文章删除处理：start");
        log.info("workId = "+workId);
        Map<String,Object> resultMap = workDetailService.deleteWorkById(workId,httpSession);
        log.info("----文章删除处理：end");
        return resultMap;
    }

    @RequestMapping("shareToggle")
    @ResponseBody
    public Map<String,Object> shareToggle(String workId,HttpSession httpSession){
        log.info("----推荐处理：start");
        //登录检查
        log.info("登录检查");
        if (httpSession.getAttribute("userId")==null){
            String pageNature = (String)httpSession.getAttribute("pageNature");
            String urlToJump = "/lightblog/workdetail/jumpToWorkDetail?workId="+workId+"&pageNature="+pageNature;
            httpSession.setAttribute("urlToJump",urlToJump);
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("isLogined","false");
            log.info("未登录，无法推荐，将由前端跳转登录页");
            return resultMap;
        }
        log.info("已经登录");
        String actorId = (String)httpSession.getAttribute("userId");
        return workDetailService.shareToggle(actorId,workId,httpSession);
    }

    @RequestMapping("thumbUpToggle")
    @ResponseBody
    public Map<String,Object> thumbUpToggle(String workId,HttpSession httpSession,HttpServletResponse response){
        log.info("----点赞处理：start");
        //登录检查
        log.info("登录检查");
        if (httpSession.getAttribute("userId")==null){
            String pageNature = (String)httpSession.getAttribute("pageNature");
            String urlToJump = "/lightblog/workdetail/jumpToWorkDetail?workId="+workId+"&pageNature="+pageNature;
            httpSession.setAttribute("urlToJump",urlToJump);
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("isLogined","false");
            log.info("未登录，无法点赞，将由前端跳转登录页");
            return resultMap;
        }
        log.info("已经登录");
        String actorId = (String)httpSession.getAttribute("userId");
        return workDetailService.thumbUpToggle(actorId,workId,httpSession);
    }

    @RequestMapping("commitSubmit")
    @ResponseBody
    public Map<String,Object> commitSubmit(String actType,String workId,String commit,HttpSession httpSession){
        log.info("----评论处理：start");
        //登录检查
        log.info("登录检查");
        if (httpSession.getAttribute("userId")==null){
            String pageNature = (String)httpSession.getAttribute("pageNature");
            String urlToJump = "/lightblog/workdetail/jumpToWorkDetail?workId="+workId+"&pageNature="+pageNature;
            httpSession.setAttribute("urlToJump",urlToJump);
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("isLogined","false");
            log.info("未登录，无法评论，将由前端跳转登录页");
            return resultMap;
        }
        log.info("已经登录");
        String actorId = (String)httpSession.getAttribute("userId");
        return workDetailService.commitSubmit(actorId,actType,workId,commit,httpSession);
    }

    @RequestMapping("reCommitSubmit")
    @ResponseBody
    public Map<String,Object> reCommitSubmit(String toActId,String commit,HttpSession httpSession){
        log.info("----回复处理：start");
        return workDetailService.reCommitSubmit(toActId,commit,httpSession);
    }

    @RequestMapping("deleteCommit")
    @ResponseBody
    public Map<String,Object> deleteCommit(String actId){
        return workDetailService.deleteCommit(actId);
    }


    @RequestMapping("editCommitSubmit")
    @ResponseBody
    public Map<String,Object> editCommitSubmit(String actId,String commit){
        return workDetailService.editCommitSubmit(actId,commit);
    }
}
