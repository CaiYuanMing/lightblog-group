package controller;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;


import org.springframework.web.servlet.ModelAndView;
import pojo.UserExample;
import service.RegisterService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("register")
public class RegisterController{
    @Autowired
    private  RegisterService registerService;

    static Logger log = Logger.getLogger(RegisterController.class);
    @RequestMapping("nameCheck")
    @ResponseBody
    public Map<String,Object> nameCheck(String userName, HttpSession httpSession){
        log.info("进入昵称校验，接受到参数 name ="+userName);

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        UserExample userExample = (UserExample) applicationContext.getBean("userExample");

        Map<String,Object> resultMap = new HashMap<String,Object>();
        userExample.or().andUserNameEqualTo(userName);
        if (!registerService.isUserExist(userExample)){
            resultMap.put("outcome","success");
            log.info("昵称校验: 昵称未被占用");
        }else {
            resultMap.put("outcome","fail");
            resultMap.put("msg","昵称已被占用！");
            log.info("昵称校验: 昵称被占用！");
        }
        return resultMap;
    }

    @RequestMapping("userIdCheck")
    @ResponseBody
    public Map<String,Object> userIdCheck(String userId,HttpSession httpSession){
        log.info("进入userId(邮箱)校验，接受到参数 userId ="+userId);

        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        UserExample userExample = (UserExample) applicationContext.getBean("userExample");

        Map<String,Object> resultMap = new HashMap<String,Object>();
        userExample.or().andUserIdEqualTo(userId);
        if (!registerService.isUserExist(userExample)){
            resultMap.put("outcome","success");
            log.info("userId(邮箱)校验: userId(邮箱)未被占用");
        }else {
            resultMap.put("outcome","fail");
            resultMap.put("msg","邮箱已被注册！");
            log.info("userId(邮箱)校验: userId(邮箱)被占用！");
        }
        return resultMap;
    }

    @RequestMapping("toRegister")
    @ResponseBody
    public Map<String,Object> toRegister(String userName,String userId,String userPassword){
            log.info("注册：进入用户添加");
            int count = registerService.addUser(userId,userName,userPassword);
            log.info(count+"条记录受到影响");
            Map<String,Object> resultMap = new HashMap<String,Object>();
            if (count>0){
                resultMap.put("outcome","success");
            }else {
                resultMap.put("outcome","fail");
                resultMap.put("msg","系统故障！");
                log.info("用户注册失败！");
            }
        return resultMap;
    }


}
