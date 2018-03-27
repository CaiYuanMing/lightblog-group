package service;

import pojo.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface WorkService {
    int insertWorkInfo(String workUserId,String workCategory,String workTitle);
    int updateWorkInfo(Integer workId,String workCategory,String workTitle);
    int deleteWorkInfoByExample(WorkInfoExample workInfoExample);
    int deleteWorkInfoByPrimaryKey(Integer workId);
    int updateWorkInfoBrowseSum(Integer workId);
    String getCategoryTip(String query,HttpSession httpSession);
    Map<String,Object> getWorkInfoByCategory(String userId,String category,HttpSession httpSession);
    WorkTemp getWorkDetailByWorkId(String workId, HttpSession httpSession);

    int insertWorkContent(Integer workId, String workContentMarkdown, String workContentHtml);
    int updateWorkContent(Integer workId, String workContentMarkdown, String workContentHtml);
    int deleteWorkContentByExample(WorkContentExample workContentExample);
    int deleteWorkContentByPrimaryKey(Integer workId);


    int insertTagWork(String tagName, Integer workId);
    int deleteTagWorkByExample(TagWorkExample tagWorkExample);
    int deleteTagWorkByPrimaryKey(TagWorkKey tagWorkKey);
    void tagEdit(Integer workId, List<String> tagList,HttpSession httpSession);
    String getTagTip(String query,HttpSession httpSession);
    Map<String,Object> getWorkInfoByTag(String userId,String tag,HttpSession httpSession);

    int insertAbout(String aboutUserId, String aboutContentMarkdown, String aboutContentHtml);
    int updateAbout(String aboutUserId, String aboutContentMarkdown, String aboutContentHtml);
    String getAboutMarkdown(HttpSession httpSession);
}
