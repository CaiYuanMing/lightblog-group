package service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

public interface UtilService {
    StringBuffer formatDataForSearchTip(List<String> dataList);
    String getFormatGenerateTime(String pattern,Date date);
    Object getBeanFromApplicationContext(HttpSession httpSession, String key);

}
