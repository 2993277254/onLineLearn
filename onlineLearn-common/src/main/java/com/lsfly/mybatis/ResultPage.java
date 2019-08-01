package com.lsfly.mybatis;

import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis分页参数及查询结果封装.	
 * 外部controller\service调用的时候，用 pageNo、pageSize。 
 * 拦截器使用的还是 父类ResultPage的  offset、limit参数
 **/

public class ResultPage<T> extends RowBounds {

	//父类 ：offset 偏移量；    limit 每页的数量
	
	//每页数量
	protected int pageNo = 1; //默认 第一页
	//每页数量
	protected int pageSize = 15; //默认 一页15条数据
    //总条数
    protected int totalCount;
    //偏移量 : 第一条数据在表中的位置    LIMIT offset, limit;
    protected int offset;
    //每页数量
    protected int limit;
    
    //结果数据
    protected List<T> result = new ArrayList<T>();
    
    /**
     * 计算偏移量  LIMIT offset, limit;
     */
    private void calcOffset() {
        this.offset = ((pageNo - 1) * pageSize);
    }
    
    /**
     * 计算限定数
     */
    private void calcLimit() {
        this.limit = pageSize;
    }

    //构造函数
    public ResultPage(int pageNo, int pageSize) {
    	this.pageNo = pageNo;
    	this.pageSize = pageSize;
    	this.calcOffset();
    	this.calcLimit();
    }
    
    //构造函数
    public ResultPage(int pageNo, int pageSize, int totalCount) {
    	this.pageNo = pageNo;
    	this.pageSize = pageSize;
    	this.calcOffset();
    	this.calcLimit();
    	
        this.totalCount = totalCount;
    }

    //--------------------------get set--------------------------//


	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

    
    

}
