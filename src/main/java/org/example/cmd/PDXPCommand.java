package org.example.cmd;

@picocli.CommandLine.Command(name = "pdxpcmds", mixinStandardHelpOptions = true,
        subcommands = {PDXPDataSubCmd.class, PDXPTestSubCmd.class},
        version = "1.0.0", description = "PDXP data tool.")
public class PDXPCommand implements Runnable {
    @Override
    public void run() {
    }

}
