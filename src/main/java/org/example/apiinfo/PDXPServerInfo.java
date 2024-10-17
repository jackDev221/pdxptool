package org.example.apiinfo;

import cn.hutool.core.net.url.UrlPath;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

@Slf4j
@Data
public class PDXPServerInfo {
    private String evidenceUrl;
    private String validateUrl;
    private String jwt;

}
// "{\"evidenceUrl\":\"http://127.0.0.1:8080/api/app/orderEvidence"\",\"validateUrl\":\"http://127.0.0.1:8080/api/app/orderValidate\",\"jwt\":\"jwt:tesstttt\"}"
