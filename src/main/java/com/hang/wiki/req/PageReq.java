package com.hang.wiki.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PageReq {
    private int page;
    @NotNull(message="Page Size should Not Null!")
    @Max(value= 100, message = "Query size should not exceed 100")
    private int size;

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
