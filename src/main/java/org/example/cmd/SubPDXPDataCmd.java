package org.example.cmd;

import lombok.extern.slf4j.Slf4j;
import org.example.data.PDXPData;
import picocli.CommandLine;

@Slf4j
@picocli.CommandLine.Command(name = "datatool", mixinStandardHelpOptions = true, description = "PDXP data tool.")
public class SubPDXPDataCmd implements Runnable {
    @CommandLine.Option(names = {"-g", "--generate"},
            description = "Generate PDXP data.")
    private boolean[] gen = new boolean[0];

    @CommandLine.Option(names = {"-d", "--data"}, description = "Decode PDXP data.")
    private String data;

    @CommandLine.Option(names = {"-p", "--preference"}, description = "preference token field json string.")
    private String preferFieldsJson;

    @Override
    public void run() {
        if (gen.length > 0) {
            PDXPData pdxpData = PDXPData.genPDXPData(preferFieldsJson);
            log.info("{}", pdxpData);
            String base64Str = pdxpData.toBase64Str();
            log.info("base64Str:{}", base64Str);
            return;
        }
        if (data != null) {
            String docodeString = data.trim();
            log.info("Decode input:{}", docodeString);
            PDXPData newData = PDXPData.decodeFromBase64Str(docodeString);
            log.info("{}", newData);
        }
    }
}
