package service;
import pojo.WorkInfo;
import pojo.WorkLiteForSocialMain;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface SocialMainService {
    List<WorkLiteForSocialMain> getWorkTempListForSocialMainByUser(HttpSession httpSession);
    List<WorkLiteForSocialMain> getWorkTempListForSocialMain(HttpSession httpSession);
    List<WorkLiteForSocialMain> getWorkTempListForSocialMainByTitleKey(String titleKey,HttpSession httpSession);
    List<WorkLiteForSocialMain> getWorkTempListForSocialMainByWorkInfoList(List<WorkInfo>workInfoList, HttpSession httpSession);
}
