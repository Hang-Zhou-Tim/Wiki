package com.hang.wiki.mapper;

public interface EbookSnapshotMapperCust {

    void insertIfNotExist();

    void updateSnapshotViewAndVoteCount();

    void updateSnapshotViewAndVoteIncrement();

}