package com.hang.wiki.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("Username exists"),
    LOGIN_USER_ERROR("Username exists or Password fails"),
    VOTE_REPEAT("You have been voted."),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
