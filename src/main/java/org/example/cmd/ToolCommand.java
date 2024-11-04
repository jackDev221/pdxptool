package org.example.cmd;

@picocli.CommandLine.Command(name = "pdxpcmds", mixinStandardHelpOptions = true,
        subcommands = {SubPDXPDataCmd.class, SubTestToolCmd.class},
        version = "1.0.0", description = "PDXP data tool.")
public class ToolCommand implements Runnable {
    @Override
    public void run() {
    }

}
