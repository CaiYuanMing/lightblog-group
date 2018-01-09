package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.SearchService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("search")
public class SearchController {
    private  static Logger log = Logger.getLogger(SearchController.class);
    @Autowired
    private SearchService searchService;
    @RequestMapping("searchTip")
    @ResponseBody
    public String searchTip(String query,String userId,HttpSession httpSession){
        log.info("接收参数 query = "+query);
        return searchService.getSearchTips(query,userId,httpSession);
    }

    @RequestMapping("searchWorkListByTitle")
    @ResponseBody
    public  Map<String,Object> searchWorkListByTitle(String title,String userId,HttpSession httpSession){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("workListByTitle",searchService.getWorkListByTitle(title,userId,httpSession));
        return  resultMap;
    }
}
