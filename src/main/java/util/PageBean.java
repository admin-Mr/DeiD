package util;

import java.util.List;

public class PageBean<T> {

	/**
	 * 工具类，保存一次分页查询结果的基本信息
	 */
	private int totalSize; // 总记录数

	private int pageCount; // 总页数，通过计算得出

	private int currentPageIndex; // 当前页码

	private int pageSize; // 每页记录数

	private List<T> dataList; // 当前页记录

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize <= 0 ? 0 : totalSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex <= 0 ? 0 : currentPageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 0 : pageSize;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public PageBean(int totalSize, int currentPageIndex, int pageSize, List<T> dataList) {
		super();
		setTotalSize(totalSize);
		setPageSize(pageSize);
		setCurrentPageIndex(currentPageIndex);
		setDataList(dataList);
		this.pageCount = totalSize / pageSize + (totalSize % pageSize == 0 ? 0 : 1);
	}

	/**
	 * 打印PageBean的信息
	 */
	public void showInfo() {
		System.out.println("TotalSize: " + totalSize);
		System.out.println("PageCount: " + pageCount);
		System.out.println("CurrentPageIndex: " + currentPageIndex);
		System.out.println("PageSize: " + pageSize);
		System.out.println("Data:");
		for (T obj : dataList) {
			System.out.println(obj);
		}
	}
}
