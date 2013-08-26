package com.source3g.hermes.utils;

import java.util.List;

public class NewPage<E> {

	private int pageSize=15;

	/** 当前页 */
	private int pageNo;

	private List<E> content;

	/** 记录的总数 */
	private long totalRecords;

	/** 总页数 */
	private int totalPages;

	public int getPageNo() {
		return pageNo;
	}

	/** 查询开始的行数 */
	private int startRow;

	
	public NewPage(int pageNo, Long totalRecords) {
		this.totalRecords = totalRecords;
		handleTotalPages(totalRecords);
		handlePageNo(pageNo);
		handleStartRow();
	}

	private void handlePageNo(int pageNo) {
		if (pageNo <= 0) {
			pageNo = 1;
		} else if (pageNo > 0 && pageNo <= totalPages) {
			this.pageNo = pageNo;
		} else {
			this.pageNo = totalPages;
		}
	}

	private void handleTotalPages(Long totalRecords) {
		totalPages = (int) (totalRecords / pageSize);
		if (totalRecords % pageSize != 0) {
			totalPages++;
		}
	}
	
	public int getTotalPages() {
		return totalPages;
	}

	/** 处理查询开始的行数 */
	private void handleStartRow() {
		startRow = (pageNo - 1) * pageSize;
	}

	public int getStartRow() {
		return startRow;
	}

	/**
	 * 请求第一页数据
	 */
	public int getFirstPageNo() {
		return 1;
	}

	/**
	 * 请求最后一页数据
	 */
	public int getLastPageNo() {
		return totalPages;
	}

	/**
	 * 请求前一页数据
	 */
	public int getPreviousPageNo() {
		return pageNo - 1;
	}

	/**
	 * 请求后一页数据
	 */
	public int getNextPageNo() {
		return pageNo + 1;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public boolean isFirstPage() {
		return pageNo == 1||pageNo==0;
	}

	public boolean isLastPage() {
		return pageNo == totalPages;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<E> getContent() {
		return content;
	}

	public void setContent(List<E> content) {
		this.content = content;
	}
}

