package org.example;

import org.example.cmd.ToolCommand;
import picocli.CommandLine;

public class App {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new ToolCommand()).execute(args);
        System.exit(exitCode);
    }
}
