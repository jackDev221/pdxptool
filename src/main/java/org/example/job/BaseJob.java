package org.example.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public abstract class BaseJob {
    public void doJobs() {
        log.info("start {} job.", getType());
    }

    public abstract String getType();

}
