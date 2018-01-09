package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.AboutService;
import service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("about")
public class AboutController {
    private  static Logger log = Logger.getLogger(AboutController.class);
    @Autowired
    private AboutService aboutService;
    @Autowired
    private UserService userService;

    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> init(HttpSession httpSession){
        log.info("-----处理关于页初始化：start");
        Map<String,Object> resultMap = (Map<String,Object>) httpSession.getAttribute("aboutPageMapForInit");
        log.info("-----处理关于页初始化：end");
        return resultMap;
    }

    @RequestMapping("jumpToAbout")
    public void  jumpToWorkDetail(String ownerId, HttpServletResponse response, HttpSession httpSession) throws IOException {
        log.info("-----关于页跳转处理：start");
        Map<String,Object> aboutPageMapForInit = new HashMap<String, Object>();
        if (ownerId==null||ownerId.equals("")){
            log.info("确认是master访问关于页");
            ownerId = (String)httpSession.getAttribute("userId");
            aboutPageMapForInit.put("visitType","master");
        }else {
            log.info("确认是visitor访问关于页");
            aboutPageMapForInit.put("visitType","visitor");
        }
        aboutPageMapForInit.put("aboutContentHtml",aboutService.getAboutHtmlByUserId(ownerId,httpSession));
        aboutPageMapForInit.put("ownerId",ownerId);
        aboutPageMapForInit.put("userTemp",userService.getUserTempByUserId(ownerId,httpSession));
        httpSession.setAttribute("aboutPageMapForInit",aboutPageMapForInit);
        log.info("-----关于页跳转处理：end");
        response.sendRedirect("../aboutPage.html");
    }
}
