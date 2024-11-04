package org.example.job;

import okhttp3.Response;
import org.example.job.info.IJobInfo;
import org.example.job.info.PressJobInfo;
import org.example.job.info.SchedulePDXPJobInfo;
import org.example.job.task.IBaseTask;
import org.example.job.worker.PDXPTaskWork;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JobFactory {
    public static BaseJob getJob(IJobInfo jobInfo) {
        BaseJob res = null;
        switch (jobInfo.getType()) {
            case Constants.JOB_TYPE_PRESS_PDXP:
                res = new PressJob((PressJobInfo) jobInfo);
                break;
            case Constants.JOB_TYPE_SCHEDULED_PDXP:
                res = genScheduledJob((SchedulePDXPJobInfo) jobInfo);
                break;
            default:
                break;
        }
        return res;

    }

    @NotNull
    private static BaseJob genScheduledJob(SchedulePDXPJobInfo schedulePDXPJobInfo) {
        BaseJob res;
        ExecuteInfo executeInfo = new ExecuteInfo();
        executeInfo.setTaskNum(schedulePDXPJobInfo.getTaskNum());
        PDXPTaskWork taskWork = new PDXPTaskWork("scheduled_job", null, null, new CountDownLatch(1), executeInfo);
        ScheduledJob.ITaskProvider iTaskProvider = new ScheduledJob.ITaskProvider() {
            @Override
            public List<IBaseTask> genTaskList(int num) {
                List<IBaseTask> iRequestTaskList = new ArrayList<>();
                for (int i = 0; i < num; i++) {
//                    PDXPData pdxpData = PDXPData.genPDXPData(schedulePDXPJobInfo.getPreferField());
//                    PDXPServerInfo pdxpServerInfo = schedulePDXPJobInfo.getPdxpServerInfo();
//                    List<String> nextUrls = new ArrayList<>(Arrays.asList(pdxpServerInfo.getValidateUrl()));
//                    PDXPDataCredibleTask task = new PDXPDataCredibleTask(pdxpData.toBase64Str(), pdxpServerInfo.getEvidenceUrl(),
//                            nextUrls, pdxpServerInfo.getJwt(), PDXPDataCredibleTask.STEP_EVIDENCE);
                    iRequestTaskList.add(schedulePDXPJobInfo.getIBaseTask());
                }
                return iRequestTaskList;
            }
        };
        res = new ScheduledJob<Response>(schedulePDXPJobInfo, taskWork, iTaskProvider);
        return res;
    }
}
