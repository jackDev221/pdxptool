package org.example.data;

import cn.hutool.core.util.HexUtil;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.example.data.PDXPData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PDXPDataAdapter extends TypeAdapter<PDXPData> {
    private final static String FIELD_VER_NAME = "ver";
    private final static String FIELD_MID_NAME = "mid";
    private final static String FIELD_SID_NAME = "sid";
    private final static String FIELD_DID_NAME = "did";
    private final static String FIELD_BID_NAME = "bid";
    private final static String FIELD_NO_NAME = "no";
    private final static String FIELD_FLAG_NAME = "flag";
    private final static String FIELD_RESEV_NAME = "reserved";
    private final static String FIELD_DATE_TIME_NAME = "dateTime";
    private final static String FIELD_DATA_LEN_NAME = "dataLength";
    private final static String FIELD_DATA_NAME = "data";

    @Override
    public void write(JsonWriter out, PDXPData value) throws IOException {
        out.beginObject();
        out.name(FIELD_VER_NAME).value(value.getVer());
        out.name(FIELD_MID_NAME).value(value.getMid());
        out.name(FIELD_SID_NAME).value(value.getSid());
        out.name(FIELD_DID_NAME).value(value.getDid());
        out.name(FIELD_BID_NAME).value(value.getBid());
        out.name(FIELD_NO_NAME).value(value.getNo());
        out.name(FIELD_FLAG_NAME).value(value.getFlag());
        out.name(FIELD_RESEV_NAME).value(value.getReserved());
        String dataHex = null;
        if (value.getData() != null) {
            dataHex = HexUtil.encodeHexStr(value.getData());
        }
        String dateTimeString = null;
        if (value.getDateTime() != null) {
            dateTimeString = value.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        }
        out.name(FIELD_DATE_TIME_NAME).value(dateTimeString);
        out.name(FIELD_DATA_LEN_NAME).value(value.getDataLength());
        out.name(FIELD_DATA_NAME).value(dataHex);
        out.endObject();
    }

    @Override
    public PDXPData read(JsonReader in) throws IOException {
        byte ver = -1;
        short mid = -1;
        int sid = -1;
        int did = -1;
        int bid = -1;
        int no = -1;
        byte flag = -1;
        int reserved = -1;
        LocalDateTime dateTime = null;
        byte[] data = null;
        short dataLength = -1;
        in.beginObject();
        String name;
        while (in.hasNext()) {
            name = in.nextName();
            switch (name) {
                case FIELD_VER_NAME:
                    ver = (byte) in.nextInt();
                    break;
                case FIELD_MID_NAME:
                    mid = (short) in.nextInt();
                    break;
                case FIELD_SID_NAME:
                    sid = in.nextInt();
                    break;
                case FIELD_DID_NAME:
                    did = in.nextInt();
                    break;
                case FIELD_BID_NAME:
                    bid = in.nextInt();
                    break;
                case FIELD_NO_NAME:
                    no = in.nextInt();
                    break;
                case FIELD_FLAG_NAME:
                    flag = (byte) in.nextInt();
                    break;
                case FIELD_RESEV_NAME:
                    reserved = in.nextInt();
                    break;
                case FIELD_DATE_TIME_NAME:
                    String dateTimeStr = in.nextString();
                    dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                    break;
                case FIELD_DATA_LEN_NAME:
                    dataLength = (short) in.nextInt();
                    break;
                case FIELD_DATA_NAME:
                    data = HexUtil.decodeHex(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        if (data == null) {
            dataLength = 0;
        } else {
            dataLength = (short) data.length;
        }
        return PDXPData.builder().ver(ver).mid(mid).sid(sid).did(did).bid(bid).no(no).reserved(reserved)
                .dataLength(dataLength).data(data).dateTime(dateTime).flag(flag).build();

    }
}
