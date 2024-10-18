package org.example.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseJob {
    protected JobInfo jobInfo;

    public void doJobs() {
        log.info("start {} job.", getType());
    }

    public abstract String getType();

}
