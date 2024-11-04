package org.example.utils;

import com.google.common.base.Strings;
import org.example.data.PDXPData;
import org.example.job.task.IBaseTask;
import org.example.job.task.PDXPDataCredibleTask;

import java.util.ArrayList;
import java.util.List;

public class TaskUtils {
    public static PDXPDataCredibleTask genPDXPTask(String preferField, String evidenceUrl, String validateUrl, String jwt) {
        PDXPData pdxpData = PDXPData.genPDXPData(preferField);
        List<String> urls = new ArrayList<>();
        int step = PDXPDataCredibleTask.STEP_DEFAULT;
        if (!Strings.isNullOrEmpty(evidenceUrl)) {
            urls.add(evidenceUrl);
            step = PDXPDataCredibleTask.STEP_EVIDENCE;
        }
        if (!Strings.isNullOrEmpty(validateUrl)) {
            urls.add(validateUrl);
            if (step == PDXPDataCredibleTask.STEP_DEFAULT) {
                step = PDXPDataCredibleTask.STEP_VALIDATE;
            }
        }
        if (step == PDXPDataCredibleTask.STEP_DEFAULT) {
            return null;
        }
        String firstBatchUrl = urls.remove(0);
        return new PDXPDataCredibleTask(pdxpData.toBase64Str(), firstBatchUrl,
                urls, jwt, step);
    }
}
