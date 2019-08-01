package com.lsfly.bas.model.courseUser.ext;

import com.lsfly.bas.model.course.TCourse;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.courseUser.TCourseUser;
import java.util.Date;

/**
 * 用作TCourseUser的列表查询页面的实体
 */
public class TCourseUserList extends TCourseUser{
    //统一封装分页、排序的实体
    private PageEntity page;

    private TCourse tCourse;

    public TCourse gettCourse() {
        return tCourse;
    }

    public void settCourse(TCourse tCourse) {
        this.tCourse = tCourse;
    }

    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
