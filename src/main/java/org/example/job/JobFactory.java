package org.example.job;

import okhttp3.Response;
import org.example.apiinfo.PDXPServerInfo;
import org.example.data.PDXPData;
import org.example.job.task.IRequestTask;
import org.example.job.task.PDXPDataCredibleTask;
import org.example.job.worker.PDXPTaskWork;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JobFactory {
    public static BaseJob getJob(JobInfo jobInfo) {
        BaseJob res = null;
        switch (jobInfo.getType()) {
            case Constants.JOB_TYPE_PRESS:
                res = new PressJob(jobInfo);
                break;
            case Constants.JOB_TYPE_SCHEDULED:
                res = genScheduledJob(jobInfo);

                break;
            default:
                break;
        }
        return res;

    }

    @NotNull
    private static BaseJob genScheduledJob(JobInfo jobInfo) {
        BaseJob res;
        ExecuteInfo executeInfo = new ExecuteInfo();
        executeInfo.setTaskNum(jobInfo.getTaskNum());
        PDXPTaskWork taskWork = new PDXPTaskWork("scheduled_job", null, null, new CountDownLatch(1), executeInfo);
        ScheduledJob.ITaskProvider iTaskProvider = new ScheduledJob.ITaskProvider() {
            @Override
            public List<IRequestTask> genTaskList(int num) {
                List<IRequestTask> iRequestTaskList = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    PDXPData pdxpData = PDXPData.genPDXPData(jobInfo.getPreferField());
                    PDXPServerInfo pdxpServerInfo = jobInfo.getPdxpServerInfo();
                    List<String> nextUrls = new ArrayList<>(Arrays.asList(pdxpServerInfo.getValidateUrl()));
                    PDXPDataCredibleTask task = new PDXPDataCredibleTask(pdxpData.toBase64Str(), pdxpServerInfo.getEvidenceUrl(),
                            nextUrls, pdxpServerInfo.getJwt(), PDXPDataCredibleTask.STEP_EVIDENCE);
                    iRequestTaskList.add(task);
                }
                return iRequestTaskList;
            }
        };
        res = new ScheduledJob<Response>(jobInfo, taskWork, iTaskProvider);
        return res;
    }
}
