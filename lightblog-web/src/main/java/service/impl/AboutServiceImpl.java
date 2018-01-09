package service.impl;

import mapper.AboutMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pojo.About;
import service.AboutService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class AboutServiceImpl implements AboutService{
    private  static Logger log = Logger.getLogger(WorkServiceImpl.class);
    @Autowired
    private AboutMapper aboutMapper;
    public String getAboutMarkdown(HttpSession httpSession) {
        String userId = (String) httpSession.getAttribute("userId");
        About about = aboutMapper.selectByPrimaryKey(userId);
        return  about.getAboutContentMarkdown();
    }

    public String getAboutHtmlByUserId(String userId, HttpSession httpSession) {
        About about = aboutMapper.selectByPrimaryKey(userId);
        return about.getAboutContentHtml();
    }
}
