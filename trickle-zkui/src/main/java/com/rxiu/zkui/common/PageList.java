package com.rxiu.zkui.common;

import java.io.Serializable;
import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/12
 */
public class PageList<T> implements Serializable {

    private int page;

    private int total;

    private int records;

    private List<T> rows;

    public PageList() {

    }

    public PageList(int page, int total, int records, List<T> rows) {
        this.page = page;
        this.total = total;
        this.records = records;
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
