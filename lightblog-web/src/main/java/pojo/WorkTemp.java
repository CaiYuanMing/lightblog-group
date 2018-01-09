package pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class WorkTemp {
    public Integer workId;
    public String workUserId;
    public String authorName;
    public String workGeneratesTime;
    public String workCategory;
    public Integer workBrowseSum;
    public String workTitle;
    public String workContentHtml;
    public String workContentMarkdown;
    public List<String> tagList = new ArrayList<String>();
}
