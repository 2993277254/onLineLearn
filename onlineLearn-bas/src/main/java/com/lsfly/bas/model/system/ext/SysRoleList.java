package com.lsfly.bas.model.system.ext;

import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.system.SysRole;
import java.util.Date;

/**
 * 用作SysRole的列表查询页面的实体
 */
public class SysRoleList extends SysRole{
    //统一封装分页、排序的实体
    private PageEntity page;


    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
