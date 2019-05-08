package com.matrix.myapplication.model;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by clyr on 2018/3/15 0015.
 * json模型 尽量使用一个 使用其他类进行拓展如：User
 */

public class JsonModel {
    public JsonModel() {
    }

    public JsonModel(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public String name;
    public String state;
    //public User user;
    public String msg;
    public String message;
    public String error;
    public String ip;
    public int icon = 0;
    private String instancenum;//监控资源总数
    private String instancealarmnum;//监控资源报警数
    private String itemnum;//监控点总数
    private String itemalarmnum;//监控点报警数
    private List<ArrayListBT> instancestatusbusiness;//业务系统
    private List<ArrayListBT> instancestatustype;//设备类型
    private List<BusinessSystem> instancelist;//业务系统 二级目录
    //private List<ItemList> itemlist;//业务系统 二级目录
    private List<TreeMap<String, String[]>> datalist;
    //private List<TwoList> mTwoLists;
    //private AlarmInfo mAlarmInfo;
    private String alarmcontent;//文件:D:/:使用率(%):81.24
    private String alarmlasttime;//486小时19分9秒
    private String alarmtime;//2018-05-02 14:59:59
    private String finish;//-
    private String processing;//	-
//    private String instancenum	;//17 //监控资源总数
    private List<Object[]> statusnumlist	;//Array
    private String totalhealth	;//95.88
    private List<ArrayListBT> devicelist	;//Array




    public List<ArrayListBT> getDevicelist() {
        return devicelist;
    }

    public void setDevicelist(List<ArrayListBT> devicelist) {
        this.devicelist = devicelist;
    }

    public List<Object[]> getStatusnumlist() {
        return statusnumlist;
    }

    public void setStatusnumlist(List<Object[]> statusnumlist) {
        this.statusnumlist = statusnumlist;
    }

    public String getTotalhealth() {
        return totalhealth;
    }

    public void setTotalhealth(String totalhealth) {
        this.totalhealth = totalhealth;
    }

    public String getAlarmcontent() {
        return alarmcontent;
    }

    public void setAlarmcontent(String alarmcontent) {
        this.alarmcontent = alarmcontent;
    }

    public String getAlarmlasttime() {
        return alarmlasttime;
    }

    public void setAlarmlasttime(String alarmlasttime) {
        this.alarmlasttime = alarmlasttime;
    }

    public String getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(String alarmtime) {
        this.alarmtime = alarmtime;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }



    public List<TreeMap<String, String[]>> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<TreeMap<String, String[]>> datalist) {
        this.datalist = datalist;
    }

    public List<BusinessSystem> getInstancelist() {
        return instancelist;
    }

    public void setInstancelist(List<BusinessSystem> instancelist) {
        this.instancelist = instancelist;
    }

    public String getInstancenum() {
        return instancenum;
    }

    public void setInstancenum(String instancenum) {
        this.instancenum = instancenum;
    }

    public String getInstancealarmnum() {
        return instancealarmnum;
    }

    public void setInstancealarmnum(String instancealarmnum) {
        this.instancealarmnum = instancealarmnum;
    }

    public String getItemnum() {
        return itemnum;
    }

    public void setItemnum(String itemnum) {
        this.itemnum = itemnum;
    }

    public String getItemalarmnum() {
        return itemalarmnum;
    }

    public void setItemalarmnum(String itemalarmnum) {
        this.itemalarmnum = itemalarmnum;
    }

    public List<ArrayListBT> getInstancestatusbusiness() {
        return instancestatusbusiness;
    }

    public void setInstancestatusbusiness(List<ArrayListBT> instancestatusbusiness) {
        this.instancestatusbusiness = instancestatusbusiness;
    }

    public List<ArrayListBT> getInstancestatustype() {
        return instancestatustype;
    }

    public void setInstancestatustype(List<ArrayListBT> instancestatustype) {
        this.instancestatustype = instancestatustype;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
