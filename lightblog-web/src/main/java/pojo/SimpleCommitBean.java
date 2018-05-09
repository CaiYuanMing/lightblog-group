package pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SimpleCommitBean {
    private int actId;
    private String actorHeadIconPath;
    private String actorId;
    private String actorName;
    private String actGenerateTime;
    private String commitContent;
    private String commitEditAuthority;

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public String getActorHeadIconPath() {
        return actorHeadIconPath;
    }

    public void setActorHeadIconPath(String actorHeadIconPath) {
        this.actorHeadIconPath = actorHeadIconPath;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActGenerateTime() {
        return actGenerateTime;
    }

    public void setActGenerateTime(String actGenerateTime) {
        this.actGenerateTime = actGenerateTime;
    }

    public String getCommitContent() {
        return commitContent;
    }

    public void setCommitContent(String commitContent) {
        this.commitContent = commitContent;
    }

    public String getCommitEditAuthority() {
        return commitEditAuthority;
    }

    public void setCommitEditAuthority(String commitEditAuthority) {
        this.commitEditAuthority = commitEditAuthority;
    }

    @Override
    public String toString() {
        return "SimpleCommitBean{" +
                "actId=" + actId +
                ", actorHeadIconPath='" + actorHeadIconPath + '\'' +
                ", actorId='" + actorId + '\'' +
                ", actorName='" + actorName + '\'' +
                ", actGenerateTime='" + actGenerateTime + '\'' +
                ", commitContent='" + commitContent + '\'' +
                ", commitEditAuthority='" + commitEditAuthority + '\'' +
                '}';
    }


}
