package pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserTemp {
   public String userName;
   public String userHeadiconPath;
   public String userIntroduction;
}
