package com.lsfly.bas.model.course.ext;

import com.lsfly.bas.model.course.TCourseWithBLOBs;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.course.TCourse;
import java.util.Date;

/**
 * 用作TCourse的列表查询页面的实体
 */
public class TCourseList extends TCourseWithBLOBs {
    //统一封装分页、排序的实体
    private PageEntity page;


    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
