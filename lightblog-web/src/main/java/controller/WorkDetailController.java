package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.WorkTemp;
import service.WorkDetailService;
import service.WorkService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("workdetail")
public class WorkDetailController {
    private  static Logger log = Logger.getLogger(WorkDetailController.class);
    @Autowired
    private WorkDetailService workDetailService;
    @Autowired
    private WorkService workService;
    @RequestMapping("init")
    @ResponseBody
    public Map<String,Object> init(HttpSession httpSession){
        log.info("-----处理文章详情页初始化：start");
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String visitType = (String) httpSession.getAttribute("visitType");
       WorkTemp workTemp = (WorkTemp)httpSession.getAttribute("workTemp") ;
        resultMap.put("visitType",visitType);
        resultMap.put("workTemp",workTemp);

        log.info("-----处理文章详情页初始化：：end");
        return resultMap;
    }

    @RequestMapping("jumpToWorkDetail")
    public void  jumpToWorkDetail(String ownerId,String workId, HttpServletResponse response, HttpSession httpSession) throws IOException {

        WorkTemp workTemp = workDetailService.getWorkDetailByWorkId(workId,httpSession);
        if(ownerId==null){
            log.info("--访问类型为：master");
            httpSession.setAttribute("visitType","master");
        }else {
            log.info("--访问类型为：visitor");
            log.info("ownerId = "+ownerId);
            httpSession.setAttribute("visitType","visitor");
          }
        httpSession.setAttribute("workTemp",workTemp);
        response.sendRedirect("../workDetail.html");
    }

    @RequestMapping("deleteWorkById")
    @ResponseBody
    public Map<String,Object> userIdCheck(String workId,HttpSession httpSession){
        log.info("----文章删除处理：start");
        log.info("workId = "+workId);
        Map<String,Object> resultMap = workDetailService.deleteWorkById(workId,httpSession);
        log.info("----文章删除处理：end");
        return resultMap;
    }
}
