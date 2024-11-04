package org.example.job.info;

import kotlin.random.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static org.example.job.Constants.JOB_TYPE_PRESS_PDXP;
import static org.example.job.Constants.JOB_TYPE_SCHEDULED_PDXP;

@AllArgsConstructor
public enum JobInfoType {
    PressPDXPJobInfo(JOB_TYPE_PRESS_PDXP, PressPDXPJobInfo.class.getName()),
    SchedulePDXPJobInfo(JOB_TYPE_SCHEDULED_PDXP, SchedulePDXPJobInfo.class.getName());
//    NullJobInfo("null");
    @Getter
    public final String name;

    public final String className;
    public static Map<String, JobInfoType> nameToType = new HashMap<>();

    static {
        for (JobInfoType type : JobInfoType.values()) {
            nameToType.put(type.name, type);
        }
    }

    public static JobInfoType getByName(String name) {
        return nameToType.get(name);
    }


}
