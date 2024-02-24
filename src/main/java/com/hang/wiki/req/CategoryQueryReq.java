package com.hang.wiki.req;

public class CategoryQueryReq extends PageReq{


    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{} " + super.toString();
    }
}
