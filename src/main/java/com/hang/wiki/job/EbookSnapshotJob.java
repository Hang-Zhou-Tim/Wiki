package com.hang.wiki.job;

import com.hang.wiki.service.EbookSnapshotService;
import com.hang.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @Resource
    private SnowFlake snowFlake;

    /**
     *
     * Generate Snapshot every 2 hour.
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void doSnapshot() {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("Generate Snapshot");
        Long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("Finished Generation, Timing: {}ms", System.currentTimeMillis() - start);
    }

}
