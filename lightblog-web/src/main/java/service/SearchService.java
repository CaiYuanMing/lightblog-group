package service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface SearchService {
    String getSearchTips(String query,String userId,HttpSession httpSession);
    List<Map<String,Object>> getWorkListByTitle(String title,String userId,HttpSession httpSession);
}
