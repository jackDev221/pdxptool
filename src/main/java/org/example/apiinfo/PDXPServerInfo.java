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

    public static PDXPServerInfo decodeFromStr(String input) {
        PDXPServerInfo res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(input, PDXPServerInfo.class);
        } catch (Exception e) {
            log.warn("input wrong PDXPServer Info :{}", input);
            e.printStackTrace();
        }
        if (!res.isValidate()) {
            log.warn("input  PDXPServer info is not validate:{}", res);
            return null;
        }
        return res;
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }

    public boolean isValidate() {
        return isValidUrl(validateUrl) && isValidUrl(evidenceUrl) && !Strings.isNullOrEmpty(jwt);
    }


}
// "{\"evidenceUrl\":\"http://127.0.0.1:8080/api/app/orderEvidence"\",\"validateUrl\":\"http://127.0.0.1:8080/api/app/orderValidate\",\"jwt\":\"jwt:tesstttt\"}"
