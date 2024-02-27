package com.hang.wiki.req;

import javax.validation.constraints.NotNull;

public class DocSaveReq {
    /**
     *
     *
     * This field corresponds to the database column doc.id
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */

    private Long id;

    /**
     *
     * This field corresponds to the database column doc.ebook_id
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    @NotNull(message = "Ebook id should not be null!")
    private Long ebookId;

    /**
     *
     * This field corresponds to the database column doc.parent
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    @NotNull(message = "Parent id should not be null!")
    private Long parent;

    /**
     *
     * This field corresponds to the database column doc.name
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    @NotNull(message = "Name should not be null!")
    private String name;

    /**
     *
     * This field corresponds to the database column doc.sort
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    @NotNull(message = "Sort should not be null!")
    private Integer sort;

    /**
     *
     * This field corresponds to the database column doc.view_count
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    private Integer viewCount;

    /**
     *
     * This field corresponds to the database column doc.vote_count
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    private Integer voteCount;

    @NotNull(message = "Content should not be null!")
    private String content;


    /**
     * This method returns the value of the database column doc.id
     *
     * @return the value of doc.id
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public Long getId() {
        return id;
    }

    /**
     * This method sets the value of the database column doc.id
     *
     * @param id the value for doc.id
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method returns the value of the database column doc.ebook_id
     *
     * @return the value of doc.ebook_id
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public Long getEbookId() {
        return ebookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * This method sets the value of the database column doc.ebook_id
     *
     * @param ebookId the value for doc.ebook_id
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    /**
     * This method returns the value of the database column doc.parent
     *
     * @return the value of doc.parent
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public Long getParent() {
        return parent;
    }

    /**
     * This method sets the value of the database column doc.parent
     *
     * @param parent the value for doc.parent
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public void setParent(Long parent) {
        this.parent = parent;
    }

    /**
     * This method returns the value of the database column doc.name
     *
     * @return the value of doc.name
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the value of the database column doc.name
     *
     * @param name the value for doc.name
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the value of the database column doc.sort
     *
     * @return the value of doc.sort
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * This method sets the value of the database column doc.sort
     *
     * @param sort the value for doc.sort
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * This method returns the value of the database column doc.view_count
     *
     * @return the value of doc.view_count
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * This method sets the value of the database column doc.view_count
     *
     * @param viewCount the value for doc.view_count
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * This method returns the value of the database column doc.vote_count
     *
     * @return the value of doc.vote_count
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public Integer getVoteCount() {
        return voteCount;
    }

    /**
     * This method sets the value of the database column doc.vote_count
     *
     * @param voteCount the value for doc.vote_count
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    /**
     * This method corresponds to the database table doc
     *
     * @mbg.generated Mon Feb 26 11:57:14 CST 2024
     */
    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", content='" + content + '\'' +
                '}';
    }
}