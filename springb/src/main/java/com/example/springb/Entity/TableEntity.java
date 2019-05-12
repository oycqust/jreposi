package com.example.springb.Entity;

import java.util.List;

public class TableEntity {
    private int total;
    private List<Activity> rows;

    public void setRows(List<Activity> rows) {
        this.rows = rows;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public List<Activity> getRows() {
        return rows;
    }
}
