package org.example.data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;


@Slf4j
@Data
@Builder
public class PDXPData {
    private byte ver;
    private short mid;
    private int sid;
    private int did;// 0000000e
    private int bid;
    private int no;
    private byte flag;
    private int reserved;
    private LocalDateTime dateTime;
    private byte[] data;
    private short dataLength;

    public byte[] writeShort(short data, boolean isLitterEndian) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        if (isLitterEndian) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        } else {
            buffer.order(ByteOrder.BIG_ENDIAN);
        }
        buffer.putShort(data);
        return buffer.array();
    }

    public static byte[] writeInt(int data, boolean isLitterEndian) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        if (isLitterEndian) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        } else {
            buffer.order(ByteOrder.BIG_ENDIAN);
        }
        buffer.putInt(data);
        return buffer.array();
    }

    public String toBase64Str() {
        LocalDate baseDate = LocalDate.of(2000, 1, 1);
        long dayOffset = dateTime.toLocalDate().toEpochDay() - baseDate.toEpochDay();
        int time = (dateTime.getHour() * 3600 + dateTime.getMinute() * 60 + dateTime.getSecond()) * 10000
                + dateTime.getNano() / 1000000 % 1000 * 10;
        int totalLength = 1 + 2 + 4 + 4 + 4 + 4 + 1 + 4 + 2 + 4 + 2 + data.length;
        byte[] b = new byte[totalLength];
        byte[] midBytes = writeShort(mid, false);
        byte[] sidBytes = writeInt(sid, false);
        byte[] didBytes = writeInt(did, false);
        byte[] bidBytes = writeInt(bid, false);
        byte[] noBytes = writeInt(no, false);
        byte[] reservedBytes = writeInt(reserved, false);
        byte[] dateBytes = writeShort((short) dayOffset, false);
        byte[] timeBytes = writeInt(time, false);
        byte[] dataLengthBytes = writeShort(dataLength, false);
        b[0] = ver;
        System.arraycopy(midBytes, 0, b, 1, 2);
        System.arraycopy(sidBytes, 0, b, 3, 4);
        System.arraycopy(didBytes, 0, b, 7, 4);
        System.arraycopy(bidBytes, 0, b, 11, 4);
        System.arraycopy(noBytes, 0, b, 15, 4);
        b[19] = flag;
        System.arraycopy(reservedBytes, 0, b, 20, 4);
        System.arraycopy(dateBytes, 0, b, 24, 2);
        System.arraycopy(timeBytes, 0, b, 26, 4);
        System.arraycopy(dataLengthBytes, 0, b, 30, 2);
        System.arraycopy(data, 0, b, 32, data.length);
        String base64EncodedPacket = Base64.getEncoder().encodeToString(b);
        return base64EncodedPacket;
    }

    public static PDXPData decodeFromBase64Str(String input) {
        byte[] inputBytes = Base64.getDecoder().decode(input);
        if (inputBytes.length < 32) {
           log.error("Input string decode result len < 32, wrong input");
            return null;
        }
        short mid = ByteBuffer.wrap(inputBytes, 1, 2).order(ByteOrder.BIG_ENDIAN).getShort();
        int sid = ByteBuffer.wrap(inputBytes, 3, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        int did = ByteBuffer.wrap(inputBytes, 7, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        int bid = ByteBuffer.wrap(inputBytes, 11, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        int no = ByteBuffer.wrap(inputBytes, 15, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        int reserved = ByteBuffer.wrap(inputBytes, 20, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        short dayOffset = ByteBuffer.wrap(inputBytes, 24, 2).order(ByteOrder.BIG_ENDIAN).getShort();
        int time = ByteBuffer.wrap(inputBytes, 26, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        short dataLen = ByteBuffer.wrap(inputBytes, 30, 2).order(ByteOrder.BIG_ENDIAN).getShort();
        byte[] data = new byte[dataLen];
        System.arraycopy(inputBytes, 32, data, 0, data.length);
        LocalDate actualDate = LocalDate.of(2000, 1, 1).plusDays(dayOffset);
        long totalMilliseconds = time / 10; // 将0.1ms转换为ms
        int hours = (int) (totalMilliseconds / 3600000);
        int minutes = (int) ((totalMilliseconds % 3600000) / 60000);
        int seconds = (int) ((totalMilliseconds % 60000) / 1000);
        int milliseconds = (int) (totalMilliseconds % 1000);
        LocalDateTime actualDateTime = actualDate.atTime(hours, minutes, seconds, milliseconds * 1000000);
        return PDXPData.builder().ver(inputBytes[0]).mid(mid).sid(sid).did(did).bid(bid).no(no).reserved(reserved)
                .dataLength(dataLen).data(data).dateTime(actualDateTime).flag(inputBytes[19]).build();
    }

    public static PDXPData genPDXPData() {
        Random random = new Random();
        byte ver = 1;
        short mid = (short) random.nextInt(100);
        int sid = random.nextInt(500);
        int did = random.nextInt(500);
        int bid = random.nextInt(500);
        int no = random.nextInt(500);
        byte flag = 1;
        int reserved = 1;
        short dataLength = (short) random.nextInt(50);
        byte[] data = new byte[dataLength];
        random.nextBytes(data);
        PDXPData pdxpData = PDXPData.builder().ver(ver).mid(mid).sid(sid).did(did).bid(bid).no(no).reserved(reserved)
                .dataLength(dataLength).data(data).dateTime(LocalDateTime.now()).flag(flag).build();
        return pdxpData;
    }

    public static PDXPData genPDXPData(String preferFields) {
        if (preferFields == null) {
            return genPDXPData();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(PDXPData.class, new PDXPDataAdapter()).create();
        Random random = new Random();
        PDXPData pdxpData = gson.fromJson(preferFields, PDXPData.class);
        if (pdxpData.getVer() == -1) {
            pdxpData.setVer((byte) 0);
        }
        if (pdxpData.getMid() == -1) {
            pdxpData.setMid((short) random.nextInt(100));
        }
        if (pdxpData.getSid() == -1) {
            pdxpData.setSid(random.nextInt(500));
        }
        if (pdxpData.getDid() == -1) {
            pdxpData.setDid(random.nextInt(500));
        }
        if (pdxpData.getBid() == -1) {
            pdxpData.setBid(random.nextInt(500));
        }
        if (pdxpData.getNo() == -1) {
            pdxpData.setNo(random.nextInt(500));
        }
        if (pdxpData.getSid() == -1) {
            pdxpData.setSid(random.nextInt(500));
        }
        if (pdxpData.getFlag() == -1) {
            pdxpData.setFlag((byte) 1);
        }
        if (pdxpData.getReserved() == -1) {
            pdxpData.setReserved(random.nextInt(500));
        }
        if (pdxpData.dataLength == -1) {
            short dataLength = (short) random.nextInt(50);
            byte[] data = new byte[dataLength];
            random.nextBytes(data);
            pdxpData.setData(data);
        }
        if (pdxpData.getDateTime() == null) {
            pdxpData.setDateTime(LocalDateTime.now());
        }
        return pdxpData;
    }


    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(PDXPData.class, new PDXPDataAdapter()).create();
        return gson.toJson(this);
//        String dataHex = null;
//        if (data != null) {
//            dataHex = HexUtil.encodeHexStr(data);
//        }
//        String dateTimeString = null;
//        if (dateTime != null) {
//            dateTimeString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
//        }
//        return String.format("PDXP INFO: \n" +
//                "\tVER: %d,\n " +
//                "\tMID: %d,\n" +
//                "\tSID: %d,\n" +
//                "\tDID: %d,\n" +
//                "\tBID: %d,\n" +
//                "\tNo.: %d,\n" +
//                "\tFLAG: %d,\n" +
//                "\tRESERVED: %d,\n " +
//                "\tDATE: %s,\n" +
//                "\tDATA LEN: %d,\n" +
//                "\tDATA(Hex): %s\n", ver, mid, sid, did, bid, no, flag, reserved, dateTimeString, dataLength, dataHex);
    }
}
