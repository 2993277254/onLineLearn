package com.lsfly.bas.model.system.ext;

import com.lsfly.bas.model.system.SysDictData;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.system.SysDict;
import java.util.Date;
import java.util.List;

/**
 * 用作SysDict的列表查询页面的实体
 */
public class SysDictList extends SysDict{
    //统一封装分页、排序的实体
    private PageEntity page;
    private List<SysDictData> sysDictDataList;

    public List<SysDictData> getSysDictDataList() {
        return sysDictDataList;
    }

    public void setSysDictDataList(List<SysDictData> sysDictDataList) {
        this.sysDictDataList = sysDictDataList;
    }

    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}
