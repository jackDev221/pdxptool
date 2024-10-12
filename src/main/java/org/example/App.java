package org.example;

import org.example.cmd.PDXPCommand;
import picocli.CommandLine;

public class App {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new PDXPCommand()).execute(args);
        System.exit(exitCode);
    }
}
