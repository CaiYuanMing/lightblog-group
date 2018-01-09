package service;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface AboutService {
   String getAboutMarkdown(HttpSession httpSession);
   String getAboutHtmlByUserId(String userId,HttpSession httpSession);
}
