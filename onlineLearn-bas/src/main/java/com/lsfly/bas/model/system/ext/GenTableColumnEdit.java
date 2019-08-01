package com.lsfly.bas.model.system.ext;



import com.lsfly.bas.model.system.GenTable;
import com.lsfly.bas.model.system.GenTableColumn;
import com.lsfly.bas.model.system.GenTableTree;

import java.util.List;

/**
 * 创建GenTableColumn的编辑实体，可增加一些编辑页面需要的实体
 * @date 2018/8/24.
 */
public class GenTableColumnEdit extends GenTableColumn {

    private List<GenTableColumn> genTableColumnList;

    private GenTable genTable;

    private GenTableTree genTableTree;

    private String isCover;

    private Boolean isPackage=false;  //是否分包，默认是false

    public List<GenTableColumn> getGenTableColumnList() {
        return genTableColumnList;
    }

    public void setGenTableColumnList(List<GenTableColumn> genTableColumnList) {
        this.genTableColumnList = genTableColumnList;
    }

    public GenTable getGenTable() {
        return genTable;
    }

    public void setGenTable(GenTable genTable) {
        this.genTable = genTable;
    }

    public String getIsCover() {
        return isCover;
    }

    public void setIsCover(String isCover) {
        this.isCover = isCover;
    }

    public GenTableTree getGenTableTree() {
        return genTableTree;
    }

    public void setGenTableTree(GenTableTree genTableTree) {
        this.genTableTree = genTableTree;
    }

    public Boolean getPackage() {
        return isPackage;
    }

    public void setPackage(Boolean aPackage) {
        isPackage = aPackage;
    }
}
