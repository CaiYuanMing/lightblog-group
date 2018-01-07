package pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class WorkListItem {
    public Integer workId;
    public String workGeneratesTime;
    public Integer workBrowseSum;
    public String workTitle;
}
