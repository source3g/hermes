package com.source3g.wcb.utils;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Page {
	private int pageSize;

	/** 当前页 */
	private int currentPage;
	
	private Object data;
	/** 记录的总数 */
	private long totalRecords;
	/** 总页数 */
	@JsonIgnore
	private long totalPageCount;
	
	/** 查询开始的行数 */
	@JsonIgnore
	private int startRow;

	public long getTotalPageCount() {
		totalPageCount = (long) (totalRecords / pageSize);
		if (totalRecords % pageSize != 0) {
			totalPageCount++;
		}
		return totalPageCount;
	}

	public long getCurrentPage() {
		if (currentPage > getTotalPageCount() && getTotalPageCount() != 0) {
			currentPage--;
		}
		return currentPage;
	}

	public Page() {
		pageSize = 15;
		currentPage = 1;
	}

	/** 处理查询开始的行数 */
	private void handleStartRow() {
		startRow = (currentPage - 1) * pageSize;
		if (startRow < 0) {
			startRow = 0;
		}
	}

	public int getStartRow() {
		handleStartRow();
		return startRow;
	}

	/**
	 * 请求第一页数据
	 */
	@JsonIgnore
	public long getFirstPageNo() {
		return 1L;
	}

	/**
	 * 请求最后一页数据
	 */
	@JsonIgnore
	public long getLastPageNo() {
		return totalPageCount;
	}

	/**
	 * 请求前一页数据
	 */
	@JsonIgnore
	public long getPreviousPageNo() {
		return currentPage - 1;
	}

	/**
	 * 请求后一页数据
	 */
	@JsonIgnore
	public long getNextPageNo() {
		return currentPage + 1;
	}

	/**
	 * 请求转到某页
	 */
	
	public void gotoPage(int pageNo) {
		totalPageCount = (long) (totalRecords / pageSize);
		if (totalRecords % pageSize != 0) {
			totalPageCount++;
		}
		
		if (pageNo > 0 && pageNo <= totalPageCount) {
			currentPage = pageNo;
		}
	}

	/**
	 * @param totalRecords
	 *            - 记录的总数
	 */
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public long getTotalRecords() {
		return totalRecords;
	}
	
	@JsonIgnore
	public boolean isFirstPage() {
		return currentPage == 1;
	}
	@JsonIgnore
	public boolean isLastPage() {
		return currentPage == totalPageCount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
