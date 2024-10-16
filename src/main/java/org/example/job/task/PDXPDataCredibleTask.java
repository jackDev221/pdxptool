package org.example.job.task;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import okhttp3.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PDXPDataCredibleTask implements IRequestTask {
    public static final int STEP_DEFAULT = 0;
    public static final int STEP_EVIDENCE = 1;
    public static final int STEP_VALIDATE = 2;
    public static final String FILED_H_AUTH_NAME = "jwt";
    public static final String FILED_PDXP = "pdxp";
    public static final String FILED_ACTION = "action";
    public static final String FILED_ACTION_V_SEND = "send";
    public static final String FILED_ACTION_V_RECEIVE = "receive";
    public static final String FILED_EXTRA = "extra";
    public static final String FILED_TASK_ID = "taskId";
    public static final String FILED_DEST = "destination";
    private String pdxpStr;
    private String currentUrl;
    private List<String> nextUrls;
    private String jwtToken;
    private int step;

    @Override
    public Request getRequest() {
        if ((step != STEP_EVIDENCE && step != STEP_VALIDATE) || Strings.isNullOrEmpty(currentUrl)) {
            return null;
        }

        JsonObject params = new JsonObject();
        if (step == STEP_EVIDENCE) {
            params.addProperty(FILED_PDXP, pdxpStr);
            params.addProperty(FILED_ACTION, FILED_ACTION_V_SEND);
            params.addProperty(FILED_EXTRA, "");
        } else {
            params.addProperty(FILED_PDXP, pdxpStr);
            params.addProperty(FILED_TASK_ID, "");
            params.addProperty(FILED_DEST, "");
        }
        RequestBody requestBody = RequestBody.create(params.toString(),
                MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader(FILED_H_AUTH_NAME, jwtToken)
                .url(currentUrl)
                .post(requestBody).build();

        return request;
    }

    @Override
    public boolean isResponseExpect(Response response) {
        return response.code() == 200;
    }

    @Override
    public IRequestTask getNextTask(Response currenResponse) {
        int step = getStep() + 1;
        if (!isResponseExpect(currenResponse) || step > STEP_VALIDATE) {
            return null;
        }
        String currentUrl = nextUrls.remove(0);
        return new PDXPDataCredibleTaskBuilder()
                .jwtToken(jwtToken)
                .step(step)
                .nextUrls(nextUrls)
                .currentUrl(currentUrl)
                .pdxpStr(pdxpStr).build();
    }

}
