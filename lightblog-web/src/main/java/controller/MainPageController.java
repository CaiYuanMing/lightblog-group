package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.UserTemp;
import pojo.WorkTemp;
import service.MainPageService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("mainpage")
public class MainPageController {
    private  static Logger log = Logger.getLogger(RegisterController.class);
    private  int count = 0;
    @Autowired
    private MainPageService mainPageService;

    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> categoryTip(HttpSession httpSession){
        log.info("-----主页初始化：start");

        Map<String,Object> resultMap = new HashMap<String,Object>();
        String visitType = (String) httpSession.getAttribute("visitType");
        List<WorkTemp> workTempList = ( List<WorkTemp>)httpSession.getAttribute("workTempList");
        UserTemp userTemp = (UserTemp)httpSession.getAttribute("userTemp");
        resultMap.put("visitType",visitType);
        resultMap.put("workTempList",workTempList);
        resultMap.put("userTemp",userTemp);

        log.info("-----主页初始化：end");
        return resultMap;
    }

    @RequestMapping("jumpToMianPage")
    public void  jumpToMianPage(String ownerId,HttpServletResponse response,HttpSession httpSession) throws IOException {
        List<WorkTemp> workTempList;
        UserTemp userTemp;
        if(ownerId==null){
            log.info("--访问类型为：master");
            ownerId = (String)httpSession.getAttribute("userId");
            httpSession.setAttribute("visitType","master");
            workTempList = mainPageService.getWorkListByOwnerId(ownerId,httpSession);
            userTemp = mainPageService.getUserTempByUserId(ownerId,httpSession);

        }else {
            log.info("--访问类型为：visitor");
            httpSession.setAttribute("visitType","visitor");
            workTempList = mainPageService.getWorkListByOwnerId(ownerId,httpSession);
            userTemp = mainPageService.getUserTempByUserId(ownerId,httpSession);
        }
        httpSession.setAttribute("workTempList",workTempList);
        httpSession.setAttribute("userTemp",userTemp);
        response.sendRedirect("../mainPage.html");
    }
}
