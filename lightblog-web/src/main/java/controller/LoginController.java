package controller;

import com.sun.deploy.net.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pojo.UserExample;
import service.LoginService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;


    static Logger log = Logger.getLogger(LoginService.class);

    @RequestMapping("userIdCheck")
    @ResponseBody
    public Map<String,Object> userIdCheck(String userId,HttpSession httpSession){

        log.info("进入userId(邮箱)校验，接受到参数 userId ="+userId);
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        UserExample userExample = (UserExample) applicationContext.getBean("userExample");

        Map<String,Object> resultMap = new HashMap<String,Object>();
        userExample.createCriteria().andUserIdEqualTo(userId);
        if (loginService.isUserExistForLg(userExample)){
            resultMap.put("outcome","success");
            log.info("userId(邮箱)校验: userId(邮箱)已注册");
        }else {
            resultMap.put("outcome","fail");
            resultMap.put("msg","邮箱未注册！");
            log.info("userId(邮箱)校验: userId(邮箱)未注册！");
        }
        return resultMap;
    }

    @RequestMapping("check_match_userid_password")
    @ResponseBody
    public Map<String,Object> userIdPasswordMatchCheck(String userId, String password, HttpSession httpSession){

        log.info("进入用户id密码匹配校验，接受到参数 userId = "+userId+" password ="+password);
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        UserExample userExample = (UserExample) applicationContext.getBean("userExample");

        Map<String,Object> resultMap = new HashMap<String,Object>();
        userExample.createCriteria().andUserIdEqualTo(userId).andUserPasswordEqualTo(password);
        if (loginService.isUserExistForLg(userExample)){
            resultMap.put("outcome","success");
            httpSession.setAttribute("userId",userId);
            log.info("用户id密码匹配校验: 匹配");
        }else {
            resultMap.put("outcome","fail");
            resultMap.put("msg","邮箱或密码错误！");
            log.info("用户id密码匹配校验: 不匹配！");
        }
        return resultMap;
    }

    @RequestMapping("initMainPage")

    public void  initMainPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("../editPage.html");
    }
}
