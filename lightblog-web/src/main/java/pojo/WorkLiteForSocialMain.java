package pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
@Scope("prototype")
public class WorkLiteForSocialMain {
    private int workId;
    private String authorId;
    private String authorHeadIconPath;
    private String authorName;
    private String workGenerateTime;
    private String title;
    private String summary;
    private String category;
    private List<String> tagList;
    private int browserSum;
    private int praiseSum;
    private int comitSum;
    private int shareSum;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorHeadIconPath() {
        return authorHeadIconPath;
    }

    public void setAuthorHeadIconPath(String authorHeadIconPath) {
        this.authorHeadIconPath = authorHeadIconPath;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getWorkGenerateTime() {
        return workGenerateTime;
    }

    public void setWorkGenerateTime(String workGenerateTime) {
        this.workGenerateTime = workGenerateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public int getBrowserSum() {
        return browserSum;
    }

    public void setBrowserSum(int browserSum) {
        this.browserSum = browserSum;
    }

    public int getPraiseSum() {
        return praiseSum;
    }

    public void setPraiseSum(int praiseSum) {
        this.praiseSum = praiseSum;
    }

    public int getComitSum() {
        return comitSum;
    }

    public void setComitSum(int comitSum) {
        this.comitSum = comitSum;
    }

    public int getShareSum() {
        return shareSum;
    }

    public void setShareSum(int shareSum) {
        this.shareSum = shareSum;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    @Override
    public String toString() {
        return "WorkLiteForSocialMain{" +
                "workId=" + workId +
                ", authorId='" + authorId + '\'' +
                ", authorHeadIconPath='" + authorHeadIconPath + '\'' +
                ", authorName='" + authorName + '\'' +
                ", workGenerateTime='" + workGenerateTime + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", category='" + category + '\'' +
                ", tagList=" + tagList +
                ", browserSum=" + browserSum +
                ", praiseSum=" + praiseSum +
                ", comitSum=" + comitSum +
                ", shareSum=" + shareSum +
                '}';
    }
}
