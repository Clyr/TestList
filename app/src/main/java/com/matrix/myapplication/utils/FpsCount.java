package com.matrix.myapplication.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by clyr on 2018/4/12 0012.
 */

public class FpsCount {
    //采样频率，单位ms
    private static int WAITTIME = 1600;
    //采样次数
    private static int RUNTIMES = 10;
    //gfxinfo用到的command
    private static String gfxCMD = "adb shell dumpsys gfxinfo com.huajiao";
    //需要监控的层
    private static String layerName = "SurfaceView";
    private static String[] command = {"cmd", "/c", "adb shell dumpsys SurfaceFlinger --latency " + layerName};
    //清空之前采样的数据，防止统计重复的时间
    private static String[] clearCommand = {"cmd", "/c", "adb shell dumpsys SurfaceFlinger --latency-clear"};
    private static String[] comd_getUpTime = {"cmd", "/c", "adb shell cat /proc/uptime"};

    private static double MillSecds = 1000000.0;
    private static double NANOS = 1000000000.0;

    public FpsCount() {
        try {
            for (int i = 0; i < RUNTIMES; i++) {
                if (layerName == null || "".equals(layerName)) {
                    new RuntimeException("图层获取失败！");
                } else {
                    Runtime.getRuntime().exec(clearCommand);
                    Thread.sleep(WAITTIME);
                    getFps(layerName);
                }
                System.out.println("<================第" + (i + 1) + "次测试完成！===============>");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //计算fps 通过SurfaceFlinger --latency获取
    @SuppressWarnings("unused")
    static void getFps(String layer) {
        BufferedReader br = null, br2 = null, br3 = null;
        java.text.DecimalFormat df1 = new java.text.DecimalFormat("#.0");
        java.text.DecimalFormat df2 = new java.text.DecimalFormat("#.00");
        java.text.DecimalFormat df3 = new java.text.DecimalFormat("#.000");
        double refreshPriod = 0; //设备刷新周期
        //这段是使用gfxinfo统计fps，可以删掉
        try {
            Process prt = Runtime.getRuntime().exec(gfxCMD);
            br3 = new BufferedReader(new InputStreamReader(prt.getInputStream()));
            String line;
            boolean flag = false;
            int frames2 = 0, jankCount = 0, vsync_overtime = 0;
            float countTime = 0;
            while ((line = br3.readLine()) != null) {
                if (line.length() > 0) {
                    if (line.contains("Execute")) {
                        flag = true;
                        continue;
                    }
                    if (line.contains("View hierarchy:")) {
                        flag = false;
                        continue;
                    }
                    if (flag) {
                        if (!line.contains(":") && !line.contains("@")) {
                            String[] timeArray = line.trim().split("    ");
                            float oncetime = Float.parseFloat(timeArray[0]) + Float.parseFloat(timeArray[1])
                                    + Float.parseFloat(timeArray[2]) + Float.parseFloat(timeArray[3]);
                            frames2 += 1;
                            countTime = countTime + oncetime;
                            if (oncetime > 16.67) {
                                jankCount += 1;
                                if (oncetime % 16.67 == 0) {
                                    vsync_overtime += oncetime / 16.67 - 1;
                                } else {
                                    vsync_overtime += oncetime / 16.67;
                                }
                            }
                        }
                    }
                }
            }
            if ((frames2 + vsync_overtime) > 0) {
                float ffps = frames2 * 60 / (frames2 + vsync_overtime);
                //System.out.println("gfxinfo方式 | 总帧数："+frames2+" fps："+ffps+" 跳帧数："+jankCount);
            }
            //下面代码是利用制定层获取fps的代码
            //get device uptime
            String upTime = "";
            Process pt = Runtime.getRuntime().exec(comd_getUpTime);
            br2 = new BufferedReader(new InputStreamReader(pt.getInputStream()));
            String uptmeLine;
            while ((uptmeLine = br2.readLine()) != null) {
                if (uptmeLine.length() > 0) {
                    upTime = uptmeLine.split(" ")[0];
                }
            }
            Process p = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String returnInfo = null;
            double b = 0;
            int frames = 0, jank = 0;
            double totalCountPeriod = 0;
            String beginRenderTime = "0.0", endRenderTime = "0.0";
            double r = 0;
            int count = 1;
            while ((returnInfo = br.readLine()) != null) {
                if (!"".equals(returnInfo) && returnInfo.length() > 0) {
                    count++;
                    int frameSize = returnInfo.split("\\s{1,}").length;
                    if (frameSize == 1) {
                        refreshPriod = Double.parseDouble(returnInfo) / MillSecds;
                        b = 0;
                        frames = 0;
                        r = refreshPriod;
                    } else {

                        if (frameSize == 3) {
                            String[] timeStamps = returnInfo.split("\\s{1,}");
                            double t0 = Double.parseDouble(timeStamps[0]);
                            double t1 = Double.parseDouble(timeStamps[1]);
                            double t2 = Double.parseDouble(timeStamps[2]);
                            if (t1 > 0 && !"9223372036854775807".equals(timeStamps[1])) {
                                if (b == 0) {
                                    b = t1;
                                    jank = 0;
                                } else {
                                    double countPeriod = (t1 - b) / MillSecds; //统计周期，大于500ms重新置为0
                                    if (countPeriod > 500) {
                                        if (frames > 0) {
                                            System.out.println(totalCountPeriod / 1000);
                                            System.out.println("SurfaceFlinger方式(超时了) | 开始采样时间点：" + beginRenderTime + "s   "
                                                    + "|结束采样时间点：" + df3.format(b / NANOS) + "s   "
                                                    + "|fps：" + df2.format(frames * 1000 / totalCountPeriod)
                                                    + "   |Frames：" + frames
                                                    + "   |单帧平均渲染时间：" + df2.format(totalCountPeriod / frames) + "ms");
                                        }
                                        b = t1;
                                        frames = 0;
                                        totalCountPeriod = 0;
                                        jank = 0;
                                    } else {
                                        frames += 1;
                                        if (countPeriod > r) {
                                            totalCountPeriod += countPeriod;
                                            if ((t2 - t0) / MillSecds > r) {
                                                jank += 1;
                                            }
                                            b = t1;
                                        } else {
                                            totalCountPeriod += r;
                                            b = Double.parseDouble(df1.format(b + r * MillSecds));
                                        }
                                    }
                                }
                                if (frames == 0) {
                                    beginRenderTime = df3.format(t1 / NANOS);
                                }
                            }
                        }
                    }
                }
            }
            if (frames > 0) {
                System.out.println("SurfaceFlinger方式 | 开始采样时间点：" + beginRenderTime + "s   "
                        + "|结束采样时间点：" + df3.format(b / NANOS) + "s   "
                        + "|fps：" + df2.format(frames * 1000 / totalCountPeriod)
                        + "   |Frames：" + frames
                        + "   |单帧平均渲染时间：" + df2.format(totalCountPeriod / frames) + "ms");
            } else {
                System.out.println("获取的层不正确  or 当前没有渲染操作,请拖动屏幕");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    Runtime.getRuntime().exec(clearCommand);
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
