package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.UserTemp;
import pojo.WorkListItem;
import service.WorkListService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("worklist")
public class WorkListController {
    private  static Logger log = Logger.getLogger(WorkListController.class);
    @Autowired
    private WorkListService workListService;

    @RequestMapping("jumpToWorkList")
    public void  jumpToMianPage(String ownerId, HttpServletResponse response, HttpSession httpSession) throws IOException {
        log.info("--归档页跳转处理：start");
        Map<String,Object> workListMap;
        UserTemp userTemp;
        if(ownerId==null){
            log.info("--访问类型为：master");
            ownerId = (String)httpSession.getAttribute("userId");
            httpSession.setAttribute("visitType","master");
            workListMap = workListService.getWorkListMapByOwnerId(ownerId,httpSession);
            userTemp = workListService.getUserTempByUserId(ownerId,httpSession);
        }else {
            log.info("--访问类型为：visitor");
            httpSession.setAttribute("visitType","visitor");
            httpSession.setAttribute("ownerId",ownerId);
            workListMap = workListService.getWorkListMapByOwnerId(ownerId,httpSession);
            userTemp = workListService.getUserTempByUserId(ownerId,httpSession);
        }
        httpSession.setAttribute("workListMap",workListMap);
        httpSession.setAttribute("userTemp",userTemp);
        log.info("--归档页跳转处理：end");
        response.sendRedirect("../workList.html");
    }

    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> init(HttpSession httpSession){
        log.info("-----归档页初始化：start");

        Map<String,Object> resultMap = new HashMap<String,Object>();

        String visitType = (String) httpSession.getAttribute("visitType");
        String ownerId = (String)httpSession.getAttribute("ownerId");
        Map<String,Object> workListMap = (Map<String,Object>)httpSession.getAttribute("workListMap");
        UserTemp userTemp = (UserTemp)httpSession.getAttribute("userTemp");

        resultMap.put("visitType",visitType);
        resultMap.put("ownerId",ownerId);
        resultMap.put("workListMap",workListMap);
        resultMap.put("userTemp",userTemp);

        log.info("-----归档页初始化：end");
        return resultMap;
    }

    @RequestMapping("listByCategory")
    @ResponseBody
    public Map<String,Object> listByCategory(String userId,String category,HttpSession httpSession){
        if(userId==null||userId.equals("")) {
            log.info("--访问类型为：master");
            userId = (String) httpSession.getAttribute("userId");
        }
        return workListService.getWorkInfoByCategory(userId,category,httpSession);
    }
}
