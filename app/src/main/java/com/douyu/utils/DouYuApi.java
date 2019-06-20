package com.douyu.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DouYuApi {
    public static final String DOUYU_API = "http://capi.douyucdn.cn/api/v1/";

//    public static String getDouyuLOLSubChannel() {
//        return DOUYU_API + "live/1?&limit=20&offset=0";
//    }
//
//    public static String getDouyuFurnaceStoneSubChannel() {
//        return DOUYU_API + "live/2?&limit=20&offset=0";
//    }
//
//    public static String getDouyuDota2SubChannel() {
//        return DOUYU_API + "live/3?&limit=20&offset=0";
//    }

    public static String getChannelUrl(int channelid, int offset) {
        return DOUYU_API + "live/" + channelid + "?&limit=20&offset=" + offset;
    }

    public static String getDouyuAllSubChannelsUrl() {
        return DOUYU_API + "getColumnDetail?shortName=game";
    }

    public static String getDouyuRecommendUrl(int offset) {
        return DOUYU_API + "live?&limit=20&offset=" + offset;
    }

    public static String getDouyuSubChannelBaseTag(int channelTag) {
        return DOUYU_API + "live/" + channelTag + "?&limit=20&offset=0";
    }

    public static String getDouyuRoom(int roomId) {
        return "http://capi.douyucdn.cn/api/v1/searchNew/" + roomId + "/1?limit=1&offset=0";//1374989
    }

    public static String getDouyuRoomUrl(int roomId) {
        return "http://www.douyu.com/lapi/live/getPlay/" + roomId;
    }

    public static Map<String, String> getDouyuRoomParams(int roomId) {
        Map<String, String> map = new HashMap<>();
        long time = System.currentTimeMillis() / (1000 * 60);
        String did = UUID.randomUUID().toString().toUpperCase();
        did = did.replace("-", "");
        String str = roomId + did + "A12Svb&%1UUmf@hC" + time;
        String sign = Md5.strToMd5Low32(str);

        map.put("cdn", "ws");
        map.put("rate", "0");
        map.put("tt", String.valueOf(time));
        map.put("did", did);
        map.put("sign", sign);

        return map;
    }

    public static String getDouyuSearchUrl(String keyWord) {
        return "http://capi.douyucdn.cn/api/v1/mobileSearch/1/1?sk=" + keyWord +
                "&offset=0&limit=10&client_sys=android";
    }
}
