package com.flurry.pageplugin.mybatis;

/**
 * 
 * @author weichao<gorilla@aliyun.com>
 *
 */
public final class Limits {
	
	/**
	 * offset
	 */
	private long offset;
	
	/**
	 * limit
	 */
	private int limit;

	/**
	 * size
	 */
	private int size;
	
	/**
	 * 
	 * @param offset
	 * @param size
	 */
	private Limits(long offset, int size) {
		this.offset = offset;
		this.size = size;
	}

	private Limits(long offset, int size, int limit) {
		this.offset = offset;
		this.size = size;
		this.limit = limit;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isEmpty() {
		return (limit <= 0);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public static Limits of(Integer pageNo, Integer size){
	    if(pageNo == null){
	        pageNo = 1;
	    }
	    if(size == null){
	        size = 50;
	    }
	    return new Limits((pageNo > 0 ? --pageNo : pageNo) * size, size, pageNo * size + size);
	}
}
