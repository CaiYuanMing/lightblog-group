package pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class ComitListItemBean {
    private SimpleCommitBean simpleCommitBean;
    private List<RecomitBean> recomitBeanList;

    public SimpleCommitBean getSimpleCommitBean() {
        return simpleCommitBean;
    }

    public void setSimpleCommitBean(SimpleCommitBean simpleCommitBean) {
        this.simpleCommitBean = simpleCommitBean;
    }

    public List<RecomitBean> getRecomitBeanList() {
        return recomitBeanList;
    }

    public void setRecomitBeanList(List<RecomitBean> recomitBeanList) {
        this.recomitBeanList = recomitBeanList;
    }

    @Override
    public String toString() {
        return "ComitListItemBean{" +
                "simpleCommitBean=" + simpleCommitBean +
                ", recomitBeanList=" + recomitBeanList +
                '}';
    }
}
