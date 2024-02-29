package com.hang.wiki.job;

import com.hang.wiki.service.DocService;
import com.hang.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    @Resource
    private SnowFlake snowFlake;

    /**
     * Update Ebook info every 10 min
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void cron() {
        // Add Log Stream ID
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));

        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("Ebook Info Updated within {} ms.", System.currentTimeMillis() - start);
    }

}
