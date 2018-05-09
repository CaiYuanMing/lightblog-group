package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.UserTemp;
import service.SearchService;
import service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("search")
public class SearchController {
    private  static Logger log = Logger.getLogger(SearchController.class);
    @Autowired
    private SearchService searchService;
    @Autowired
    private UserService userService;

    @RequestMapping("jumpToSearch")
    public void  jumpToSearch(String ownerId, HttpServletResponse response, HttpSession httpSession) throws IOException {
        log.info("--搜索页跳转处理：start");
        Map<String, Object> mapForSearchInit = new HashMap<String, Object>();
        if (ownerId==null){
            log.info("确认是master访问");
            mapForSearchInit.put("visitType","master");
            ownerId = (String)httpSession.getAttribute("userId");
        }else {
            log.info("确认是visitor访问");
            mapForSearchInit.put("visitType","visitor");
        }
        UserTemp userTemp = userService.getUserTempByUserId(ownerId,httpSession);
        mapForSearchInit.put("ownerId",ownerId);
        mapForSearchInit.put("ownerName",userTemp.userName);
        mapForSearchInit.put("ownerHeadIconPath",userTemp.userHeadiconPath);
        mapForSearchInit.put("ownerIntroduction",userTemp.userIntroduction);

        httpSession.setAttribute("mapForSearchInit",mapForSearchInit);

        log.info("--搜素页跳转处理：end");
        response.sendRedirect("../searchPage.html");
    }

    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> init(HttpSession httpSession){
        log.info("-----搜素页初始化：start");
        Map<String,Object> resultMap = ( Map<String,Object>)httpSession.getAttribute("mapForSearchInit");
        log.info("-----搜素页初始化：end");
        return resultMap;
    }

    @RequestMapping("searchTip")
    @ResponseBody
    public String searchTip(String query,String userId,HttpSession httpSession){
        log.info("接收参数 query = "+query);
        return searchService.getSearchTips(query,"personal",userId,httpSession);
    }

    @RequestMapping("searchWorkListByTitle")
    @ResponseBody
    public  Map<String,Object> searchWorkListByTitle(String title,String userId,HttpSession httpSession){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("workListByTitle",searchService.getWorkListByTitle(title,userId,httpSession));
        return  resultMap;
    }
}
