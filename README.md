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

- pressPDXPJobInfo
>{\"evidenceUrl\":\"http://127.0.0.1:8080/api/app/orderEvidence\",\"validateUrl\":\"http://127.0.0.1:8080/api/app/orderValidate\",\"jwt\":\"jwt...\",\"type\":\"PressPDXP\",\"taskNum\":12,\"workerNum\":4,\"batchInterval\":5000}

去转义：
```
{
"evidenceUrl": "http://127.0.0.1:8080/api/app/orderEvidence",
"validateUrl": "http://127.0.0.1:8080/api/app/orderValidate",
"jwt": "jwt...",
"type": "PressPDXP",
"taskNum": 12,
"workerNum": 4,
"batchInterval": 5000
}
```
- scheduledPDXPJobInfo

>{\"evidenceUrl\":\"http://127.0.0.1:8080/api/app/orderEvidence\",\"jwt\":\"jwt...\",\"type\":\"ScheduledPDXP\",\"taskNum\":11,\"workerNum\":1,\"initialDelay\":0,\"period\":3,\"taskNumPeriod\":3}
去转义：
```
{
"evidenceUrl": "http://127.0.0.1:8080/api/app/orderEvidence",
"jwt": "jwt...",
"type": "ScheduledPDXP",
"taskNum": 11,
"workerNum": 1,
"initialDelay": 0,
"period":3,
"taskNumPeriod":3
}
其中：period 单位秒

```

其中"preferField"为可选属性，内容为JSON 字符串，例子如下：

```
 {
  "ver": 1,
  "mid": 89,
  "sid": 434,
  "did": 76,
  "bid": 118,
  "no": 106,
  "flag": 1,
  "reserved": 1,
  "dateTime": "2024-10-31 11:08:32.306",
  "dataLength": 19,
  "data": "16dcff02a8b6cd828ad65fa7913de612b9b700"
}


```

