package controller;

        import org.apache.log4j.Logger;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.ResponseBody;
        import pojo.WorkLiteForSocialMain;
        import service.InteractService;
        import service.SearchService;
        import service.SocialMainService;
        import service.UserService;
        import javax.servlet.http.HttpSession;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

@Controller
@RequestMapping("socialMain")
public class SocialMainController {
    private  static Logger log = Logger.getLogger(SocialMainController.class);
    @Autowired
    private SocialMainService socialMainService;
    @Autowired
    private InteractService interactService;
    @Autowired
    private SearchService searchService;

    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> init(HttpSession httpSession){
        log.info("-----社交主页初始化：start");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        if (httpSession.getAttribute("userId")!= null){
            String userId = (String) httpSession.getAttribute("userId");
            log.info("---用户已登录，userId = "+userId);
            //访问类型
            resultMap.put("visitType","user");
            //用户名
            resultMap.put("userName", (String)httpSession.getAttribute("userName"));
            //未读通知数量
            resultMap.put("toReadInterActSum",interactService.getToReadInterActSumByUser(httpSession));
            //未读私信数量

            //订阅用户及自己发表的文章，时间由近及远
            List<WorkLiteForSocialMain> workTempList = socialMainService.getWorkTempListForSocialMainByUser(httpSession);
            resultMap.put("workTempList",workTempList);
        }else {
            log.info("---确定为游客访问");
            //访问类型
            resultMap.put("visitType","visitor");
            //热度文章推荐
            List<WorkLiteForSocialMain> workTempList = socialMainService.getWorkTempListForSocialMain(httpSession);
            resultMap.put("workTempList",workTempList);
        }
        log.info("-----社交主页初始化：end");
        return resultMap;
    }

    @RequestMapping("searchTip")
    @ResponseBody
    public String searchTip(String query,HttpSession httpSession){
        log.info("接收参数 query = "+query);
        return searchService.getSearchTips(query,"all",null,httpSession);
    }

    @RequestMapping("searchWorkListByTitleKey")
    @ResponseBody
    public  Map<String,Object> searchWorkListByTitle(String titleKey,HttpSession httpSession){
        log.info("searchWorkListByTitle:start");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("outcome","to aviod ajax error");
        resultMap.put("workTempList",socialMainService.getWorkTempListForSocialMainByTitleKey(titleKey,httpSession));
        log.info(resultMap);
        log.info("searchWorkListByTitle:end");
        return  resultMap;
    }
}
