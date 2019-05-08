package com.matrix.myapplication.model;

import java.util.List;

/**
 * Created by M S I of clyr on 2019/3/26.
 */

public class upDateModel {
    private String type;//android
    private List<Log> log	;//Array
    private String name	;//WPTMS.apk
    private String  version_main;//1
    private String  version_minor;//	1
    private String  version_revise;//0002
    public class Log{
        private String logContent;

        public String getLogContent() {
            return logContent;
        }

        public void setLogContent(String logContent) {
            this.logContent = logContent;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Log> getLog() {
        return log;
    }

    public void setLog(List<Log> log) {
        this.log = log;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion_main() {
        return version_main;
    }

    public void setVersion_main(String version_main) {
        this.version_main = version_main;
    }

    public String getVersion_minor() {
        return version_minor;
    }

    public void setVersion_minor(String version_minor) {
        this.version_minor = version_minor;
    }

    public String getVersion_revise() {
        return version_revise;
    }

    public void setVersion_revise(String version_revise) {
        this.version_revise = version_revise;
    }
}
