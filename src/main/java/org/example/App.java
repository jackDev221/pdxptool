package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.cmd.ToolCommand;
import org.example.job.info.IJobInfo;
import org.example.job.info.PressPDXPJobInfo;
import org.example.serialize.InterfaceAdapter;
import picocli.CommandLine;

public class App {
    public static void main(String[] args) {
        PressPDXPJobInfo pressJobInfo = new PressPDXPJobInfo();
        pressJobInfo.setType("PressPDXP");
        pressJobInfo.setWorkerNum(1);
        pressJobInfo.setTaskNum(12);
        pressJobInfo.setJwt("jwt...");
        pressJobInfo.setPreferField("preferField");
        pressJobInfo.setEvidenceUrl("evidenceUrl");
        pressJobInfo.setValidateUrl("validaUrl");
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(
                IJobInfo.class, new InterfaceAdapter<IJobInfo>()).create();
        String jsonStr = gson.toJson(pressJobInfo);
        System.out.println(jsonStr);
        PressPDXPJobInfo iJobInfo =(PressPDXPJobInfo) gson.fromJson(jsonStr, IJobInfo.class);
        System.out.println(iJobInfo.getJwt());

        int exitCode = new CommandLine(new ToolCommand()).execute(args);
        System.exit(exitCode);
    }
}
