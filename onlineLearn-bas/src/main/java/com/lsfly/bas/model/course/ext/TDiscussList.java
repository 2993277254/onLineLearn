package com.lsfly.bas.model.course.ext;

import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.course.TDiscuss;
import java.util.Date;

/**
 * 用作TDiscuss的列表查询页面的实体
 */
public class TDiscussList extends TDiscuss{
    //统一封装分页、排序的实体
    private PageEntity page;


    private String userName;
    private String photoUrl;
    private String courseName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
