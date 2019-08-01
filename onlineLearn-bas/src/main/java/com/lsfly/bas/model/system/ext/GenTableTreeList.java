package com.lsfly.bas.model.system.ext;


import com.lsfly.bas.model.system.GenTableTree;
import com.lsfly.sys.PageEntity;

/**
 * 用作GenTableTree的列表查询页面的实体
 */
public class GenTableTreeList extends GenTableTree {
    //统一封装分页、排序的实体
    private PageEntity page;


    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
