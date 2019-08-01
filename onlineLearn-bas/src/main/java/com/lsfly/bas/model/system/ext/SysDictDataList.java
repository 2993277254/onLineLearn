package com.lsfly.bas.model.system.ext;

import com.lsfly.bas.model.system.SysDict;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.system.SysDictData;
import java.util.Date;

/**
 * 用作SysDictData的列表查询页面的实体
 */
public class SysDictDataList extends SysDictData{
    //统一封装分页、排序的实体
    private PageEntity page;

    //关联的sys_dict表
    private SysDict sysDict;

    public SysDict getSysDict() {
        return sysDict;
    }

    public void setSysDict(SysDict sysDict) {
        this.sysDict = sysDict;
    }

    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
