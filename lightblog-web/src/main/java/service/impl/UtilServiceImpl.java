package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;
import service.UtilService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service
public class UtilServiceImpl implements UtilService {
    public StringBuffer formatDataForSearchTip(List<String> dataList) {
        StringBuffer searchTips = new StringBuffer("");
        for (int i = 0; i < dataList.size(); i++) {
            if (i==0){
                if (i==dataList.size()-1){
                    searchTips.append("[\"");
                    searchTips.append(dataList.get(i));
                    searchTips.append("\"]");
                }else {
                    searchTips.append("[\"");
                    searchTips.append(dataList.get(i));
                    searchTips.append("\",");
                }
            }else  if (i==dataList.size()-1) {
                searchTips.append("\"");
                searchTips.append(dataList.get(i));
                searchTips.append("\"]");
            }else{
                searchTips.append("\"");
                searchTips.append(dataList.get(i));
                searchTips.append("\",");
            }
        }
        return searchTips;
    }

    public String getFormatGenerateTime(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public Object getBeanFromApplicationContext(HttpSession httpSession, String key) {
        ServletContext sc = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        return  applicationContext.getBean(key);
    }
}
