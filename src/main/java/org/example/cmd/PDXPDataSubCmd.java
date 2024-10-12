package org.example.cmd;

import org.example.data.PDXPData;
import picocli.CommandLine;

@picocli.CommandLine.Command(name = "datatool", mixinStandardHelpOptions = true,  description = "PDXP data tool.")
public class PDXPDataSubCmd implements Runnable{
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
            System.out.println(pdxpData);
            String base64Str = pdxpData.toBase64Str();
            System.out.println("base64Str: " + base64Str);
            return;
        }
        if (data != null) {
            String docodeString = data.trim();
            System.out.println("Decode input: " + docodeString);
            PDXPData newData = PDXPData.decodeFromBase64Str(docodeString);
            System.out.println(newData);
        }
    }
}
