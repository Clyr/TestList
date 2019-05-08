package com.matrix.myapplication.model;

/**
 * Created by clyr on 2018/4/3 0003.
 * jsonModel 模型 业务系统和设备类型数组扩展类
 */

public class ArrayListBT {
    private String alarm;   //0  报警数
    private String code;    //INSTANCE0000105
    private String icon;    //device-INSTANCE0000105
    private String name;    //路由器32.1
    private String total;   //1  总数

    private String deviceicon;//server
    private String id;//INSTANCE0000089
    private String ip;//192.168.32.7
//    private String name;//windows服务器32.7
    private String status;//2

    public String getDeviceicon() {
        return deviceicon;
    }

    public void setDeviceicon(String deviceicon) {
        this.deviceicon = deviceicon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

