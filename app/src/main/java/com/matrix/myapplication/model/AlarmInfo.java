package com.matrix.myapplication.model;

import java.util.List;

/**
 * Created by clyr on 2018/4/28 0028.
 */

public class AlarmInfo {
    private List<AlarmInfoSub> alarmlist;//Array
    private String totalitem;//117
    private List<Object[]> totalstatus;//Array

    public List<AlarmInfoSub> getAlarmlist() {
        return alarmlist;
    }

    public void setAlarmlist(List<AlarmInfoSub> alarmlist) {
        this.alarmlist = alarmlist;
    }

    public String getTotalitem() {
        return totalitem;
    }

    public void setTotalitem(String totalitem) {
        this.totalitem = totalitem;
    }

    public List<Object[]> getTotalstatus() {
        return totalstatus;
    }

    public void setTotalstatus(List<Object[]> totalstatus) {
        this.totalstatus = totalstatus;
    }
}
