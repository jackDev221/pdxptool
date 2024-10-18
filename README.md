# CMD IFNO #
## 1.HELP ##


example output

- help

   input

   `java -jar ./pdxptool-1.0.0.jar -h`
- datatool

  - help
  
    input `java -jar ./pdxptool-1.0.0.jar datatool -h`
  - generate data

    input `java -jar ./pdxptool-1.0.0.jar datatool -g`
  
  - parase data

    input `java -jar ./pdxptool-1.0.0.jar datatool -d AQBUAAAAsQAAAT....`

- test
  input `java -jar ./pdxptool-1.0.0.jar test -j testJobInfo`

`testJobInfo` detail:

>{\"type\":\"scheduled\",\"taskNum\":12,\"workerNum\":1,\"initialDelay\":0,\"period\":5,\"taskNumPeriod\":3,\"pdxpServerInfo\":{\"evidenceUrl\":\"http://127.0.0.1:8080/api/app/orderEvidence\",\"validateUrl\":\"http://127.0.0.1:8080/api/app/orderValidate\",\"jwt\":\"jwt:tesstttt\"}}

>{
"type": "scheduled",
"taskNum": 12,
"workerNum": 1,
"initialDelay": 0,
"period": 5,
"taskNumPeriod": 3,
"pdxpServerInfo": {
"evidenceUrl": "http://127.0.0.1:8080/api/app/orderEvidence",
"validateUrl": "http://127.0.0.1:8080/api/app/orderValidate",
"jwt": "jwt:tesstttt"
}
}
