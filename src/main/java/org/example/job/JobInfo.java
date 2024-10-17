package org.example.job;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.apiinfo.PDXPServerInfo;

import java.net.URL;

@Slf4j
@Data
public class JobInfo {
    private String type;
    private String preferField;
    private int taskNum;
    private int workerNum;
    private PDXPServerInfo pdxpServerInfo;

    public static JobInfo decodeFromJsonStr(String input) {
        JobInfo jobInfo = null;
        try {
            Gson gson = new Gson();
            jobInfo = gson.fromJson(input, JobInfo.class);
        } catch (Exception e) {
            log.warn("input wrong PDXPServer Info :{}", input);
            e.printStackTrace();
        }
        if (!jobInfo.isValidate()) {
            log.warn("job info is not validated, detail:{}", jobInfo);
            return null;
        }
        return jobInfo;
    }

    public boolean isValidate() {
        boolean serverValidate = isValidUrl(pdxpServerInfo.getValidateUrl())
                && isValidUrl(pdxpServerInfo.getEvidenceUrl()
        ) && !Strings.isNullOrEmpty(pdxpServerInfo.getEvidenceUrl());
        boolean pressJobValidate = taskNum > 0 && workerNum > 0;
        boolean jobTypeValidate = type.equals(Constants.JOB_TYPE_SCHEDULED) || type.equals(Constants.JOB_TYPE_PRESS);
        return jobTypeValidate && serverValidate && pressJobValidate;
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }
}
