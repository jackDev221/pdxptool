package org.example.job.info;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.job.Constants;
import org.example.job.task.IBaseTask;
import org.example.utils.CommonUtils;
import org.example.utils.TaskUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchedulePDXPJobInfo extends ScheduleJobInfo {
    private String preferField;
    private String evidenceUrl;
    private String validateUrl;
    private String jwt;

    @Override
    public boolean isValidate() {
        boolean isEvidenceUrlValidate = !Strings.isNullOrEmpty(evidenceUrl) && CommonUtils.isValidUrl(evidenceUrl);
        boolean isValidateUrlValidate = !Strings.isNullOrEmpty(validateUrl) && CommonUtils.isValidUrl(validateUrl);
        boolean paramsJobValidate = taskNum > 0 && workerNum > 0 && period > 0 && taskNumPeriod > 0;
        boolean jobTypeValidate = type.equals(Constants.JOB_TYPE_SCHEDULED_PDXP);
        return jobTypeValidate && (isValidateUrlValidate || isEvidenceUrlValidate) && paramsJobValidate;
    }

    @Override
    public IBaseTask getIBaseTask() {
        return TaskUtils.genPDXPTask(preferField, evidenceUrl, validateUrl, jwt);
    }
}
