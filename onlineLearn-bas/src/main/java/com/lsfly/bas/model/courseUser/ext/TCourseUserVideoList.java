package com.lsfly.bas.model.courseUser.ext;

import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.courseUser.TCourseUserVideo;
import java.util.Date;

/**
 * 用作TCourseUserVideo的列表查询页面的实体
 */
public class TCourseUserVideoList extends TCourseUserVideo{
    //统一封装分页、排序的实体
    private PageEntity page;


    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
