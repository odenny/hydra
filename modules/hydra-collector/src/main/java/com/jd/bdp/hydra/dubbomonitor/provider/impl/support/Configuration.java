package com.jd.bdp.hydra.dubbomonitor.provider.impl.support;

import java.util.ArrayList;
import java.util.List;

/**
 * User: xiangkui
 * Date: 13-5-16
 * Time: 下午5:58
 */
public class Configuration {
    private Integer queueSize;
    private Integer taskCount;

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }
}
