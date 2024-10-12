package org.example;

import org.example.data.PDXPData;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Wrong input");
            return;
        }
        if (args[0].equals("gen")) {
            PDXPData pdxpData = PDXPData.genPDXPData();
            System.out.println(pdxpData);
            String base64Str = pdxpData.toBase64Str();
            System.out.println("base64Str: " + base64Str);
        } else {
            String docodeString = args[0].trim();
            System.out.println("Decode input: " + docodeString);
            PDXPData newData = PDXPData.decodeFromBase64Str(docodeString);
            System.out.println(newData);
        }
    }
}