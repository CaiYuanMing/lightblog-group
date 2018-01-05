package pojo;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

public class WorkTemp {
    private Integer workId;
    private String workUserId;
    private Date workGeneratesTime;
    private String workCategory;
    private Integer workBrowseSum;
    private String workTitle;
    private String workContentMarkdown;
    private String workContentHtml;
    private List<String> tagList;

    public WorkTemp(Integer workId, String workUserId, Date workGeneratesTime, String workCategory, Integer workBrowseSum, String workTitle, String workContentMarkdown, String workContentHtml, List<String> tagList) {
        this.workId = workId;
        this.workUserId = workUserId;
        this.workGeneratesTime = workGeneratesTime;
        this.workCategory = workCategory;
        this.workBrowseSum = workBrowseSum;
        this.workTitle = workTitle;
        this.workContentMarkdown = workContentMarkdown;
        this.workContentHtml = workContentHtml;
        this.tagList = tagList;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public String getWorkUserId() {
        return workUserId;
    }

    public void setWorkUserId(String workUserId) {
        this.workUserId = workUserId;
    }

    public Date getWorkGeneratesTime() {
        return workGeneratesTime;
    }

    public void setWorkGeneratesTime(Date workGeneratesTime) {
        this.workGeneratesTime = workGeneratesTime;
    }

    public String getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(String workCategory) {
        this.workCategory = workCategory;
    }

    public Integer getWorkBrowseSum() {
        return workBrowseSum;
    }

    public void setWorkBrowseSum(Integer workBrowseSum) {
        this.workBrowseSum = workBrowseSum;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getWorkContentMarkdown() {
        return workContentMarkdown;
    }

    public void setWorkContentMarkdown(String workContentMarkdown) {
        this.workContentMarkdown = workContentMarkdown;
    }

    public String getWorkContentHtml() {
        return workContentHtml;
    }

    public void setWorkContentHtml(String workContentHtml) {
        this.workContentHtml = workContentHtml;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
