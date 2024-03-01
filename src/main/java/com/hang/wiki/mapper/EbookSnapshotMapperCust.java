package com.hang.wiki.mapper;

import com.hang.wiki.resp.StatisticResp;

import java.util.List;
public interface EbookSnapshotMapperCust {

    void insertIfNotExist();

    void updateSnapshotViewAndVoteCount();

    void updateSnapshotViewAndVoteIncrement();

    public List<StatisticResp> getStatistics();

}