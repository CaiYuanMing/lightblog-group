package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.UserTemp;
import service.TagListService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("taglist")
public class TagListController {
    private  static Logger log = Logger.getLogger(WorkListController.class);
    @Autowired
    private TagListService tagListService;

    @RequestMapping("jumpToTagList")
    public void  jumpToTagPage(String ownerId, HttpServletResponse response, HttpSession httpSession) throws IOException {
        log.info("--标签页跳转处理：start");
        Map<String, Object> tagMap;
        UserTemp userTemp;
        if(ownerId==null){
            log.info("--访问类型为：master");
            ownerId = (String)httpSession.getAttribute("userId");
            httpSession.setAttribute("visitType","master");
        }else {
            log.info("--访问类型为：visitor");
            httpSession.setAttribute("visitType","visitor");
            httpSession.setAttribute("ownerId",ownerId);
        }
        tagMap = tagListService.getTagListByOwnerId(ownerId,httpSession);
        userTemp = tagListService.getUserTempByUserId(ownerId,httpSession);

        httpSession.setAttribute("tagMap",tagMap);
        httpSession.setAttribute("userTemp",userTemp);

        log.info("--标签页跳转处理：end");
        response.sendRedirect("../tagList.html");
    }

    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> init(HttpSession httpSession){
        log.info("-----标签页初始化：start");
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String visitType = (String) httpSession.getAttribute("visitType");
        String ownerId = (String)httpSession.getAttribute("ownerId");
        Map<String, Object> tagMap = (Map<String, Object>)httpSession.getAttribute("tagMap");
        UserTemp userTemp = (UserTemp)httpSession.getAttribute("userTemp");

        resultMap.put("visitType",visitType);
        resultMap.put("ownerId",ownerId);
        resultMap.put("tagMap",tagMap);
        resultMap.put("userTemp",userTemp);

        log.info("-----标签页初始化：end");
        return resultMap;
    }
}
