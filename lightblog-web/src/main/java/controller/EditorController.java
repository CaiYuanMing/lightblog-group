package controller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pojo.WorkTemp;
import service.WorkService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("editor")
public class EditorController {
    private  static Logger log = Logger.getLogger(RegisterController.class);
    private  int count = 0;
    @Autowired
    private WorkService workService;
    @RequestMapping("saveWork")
    @ResponseBody
    public Map<String,Object> saveWork(String workTitle, String workCategory, String tag_string, String workContentMarkdown, String workContentHtml, HttpSession httpSession){
        log.info("------进入文章保存处理");
        int workId;
        //标签字符串转换为List
        String[] tagArray = new String[] {};// 字符数组
        List<String> tagList = new ArrayList<String>();// list
        tagArray = tag_string.split("&");// 字符串转字符数组
        tagList = java.util.Arrays.asList(tagArray);// 字符数组转list

        String editType =  (String) httpSession.getAttribute("editType");
        log.info("--从session获取得到保存类型editType = "+editType);

        Map<String,Object> resultMap = new HashMap<String,Object>();

        if (editType == null){
            log.info("--新建文章保存处理：start");
            workId = workService.insertWorkInfo((String) httpSession.getAttribute("userId"),workCategory,workTitle);
            count = workService.insertWorkContent(workId,workContentMarkdown,workContentHtml);
            workService.tagEdit(workId,tagList,httpSession);
            if (count>0){
                resultMap.put("outcome","success");
            }else{
                resultMap.put("outcome","fail");
                resultMap.put("msg","系统故障，无法保存完整文章信息");
            }
            log.info("--新建文章保存处理：end");

        }else if (editType.equals("reEdit")){
            log.info("--再编辑文章处理:start");
            workId = Integer.parseInt(httpSession.getAttribute("workId").toString());
            count = workService.updateWorkInfo(workId,workCategory,workTitle);
            count += workService.updateWorkContent(workId,workContentMarkdown,workContentHtml);
            workService.tagEdit(workId,tagList,httpSession);
            if (count>1){
                resultMap.put("outcome","success");
            }else{
                resultMap.put("outcome","fail");
                resultMap.put("msg","系统故障，无法保存完整文章信息");
            }
            log.info("--再编辑文章处理:end");
        }
        return resultMap;
    }


    @RequestMapping("imageUpload")
    public void imageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "editormd-image-file", required = false) MultipartFile attach){
        try {
            request.setCharacterEncoding( "utf-8" );
            response.setHeader( "Content-Type" , "text/html" );
            String rootPath = request.getSession().getServletContext().getRealPath("/resources/upload/");

            /**
             * 文件路径不存在则需要创建文件路径
             */
            File filePath=new File(rootPath);
            if(!filePath.exists()){
                filePath.mkdirs();
            }

            //最终文件名
            File realFile=new File(rootPath+File.separator+attach.getOriginalFilename());
            FileUtils.copyInputStreamToFile(attach.getInputStream(), realFile);

            //下面response返回的json格式是editor.md所限制的，规范输出就OK
            response.getWriter().write( "{\"success\": 1, \"message\":\"上传成功\",\"url\":\"lightblog/resources/upload/" + attach.getOriginalFilename() + "\"}" );
        } catch (Exception e) {
            try {
                response.getWriter().write( "{\"success\":0}" );
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RequestMapping("categoryTip")
    @ResponseBody
    public String categoryTip(String query,HttpSession httpSession){
        log.info("接收参数 query = "+query);
        return workService.getCategoryTip(query,httpSession);
    }

    @RequestMapping("tagTip")
    @ResponseBody
    public String tagTip(String query,HttpSession httpSession){
        log.info("接收参数 query = "+query);
        return workService.getTagTip(query,httpSession);
    }



    @RequestMapping("saveAbout")
    @ResponseBody
    public Map<String,Object> saveAbout(String workContentMarkdown,String workContentHtml,HttpSession httpSession){

        log.info("--关于页再编辑处理:start");
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String userId = (String) httpSession.getAttribute("userId");
        count = workService.updateAbout(userId,workContentMarkdown,workContentHtml);

        if (count>1){
            resultMap.put("outcome","success");
        }else{
            resultMap.put("outcome","fail");
            resultMap.put("msg","系统故障，无法保存完整文章信息");
        }
        log.info("--关于页再编辑处理:end");
        return resultMap;
    }
}
