package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.UserExample;
import service.RegisterService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private  RegisterService registerService;
    @Autowired
    private UserExample userExample;

    static Logger log = Logger.getLogger(RegisterController.class);
    @RequestMapping("nameCheck")
    @ResponseBody
    public Map<String,Object> nameCheck(String userName){

        log.info("进入昵称校验，接受到参数 name ="+userName);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        userExample.createCriteria().andUserNameEqualTo(userName);
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
}
