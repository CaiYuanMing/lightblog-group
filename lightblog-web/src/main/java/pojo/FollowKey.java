package pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FollowKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column follow.follower_id
     *
     * @mbggenerated Wed May 09 11:39:51 CST 2018
     */
    private String followerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column follow.followed_user_id
     *
     * @mbggenerated Wed May 09 11:39:51 CST 2018
     */
    private String followedUserId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column follow.follower_id
     *
     * @return the value of follow.follower_id
     *
     * @mbggenerated Wed May 09 11:39:51 CST 2018
     */
    public String getFollowerId() {
        return followerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column follow.follower_id
     *
     * @param followerId the value for follow.follower_id
     *
     * @mbggenerated Wed May 09 11:39:51 CST 2018
     */
    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column follow.followed_user_id
     *
     * @return the value of follow.followed_user_id
     *
     * @mbggenerated Wed May 09 11:39:51 CST 2018
     */
    public String getFollowedUserId() {
        return followedUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column follow.followed_user_id
     *
     * @param followedUserId the value for follow.followed_user_id
     *
     * @mbggenerated Wed May 09 11:39:51 CST 2018
     */
    public void setFollowedUserId(String followedUserId) {
        this.followedUserId = followedUserId;
    }
}