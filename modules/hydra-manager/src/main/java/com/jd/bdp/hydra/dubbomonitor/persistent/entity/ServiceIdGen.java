package com.jd.bdp.hydra.dubbomonitor.persistent.entity;

/**
 * User: biandi
 * Date: 13-4-16
 * Time: 上午9:49
 */
public class ServiceIdGen {

    private Integer maxId;
    private Integer head;
    private Integer maxHead;
    private Integer idScope;

    public Integer getMaxId() {
        return maxId;
    }

    public void setMaxId(Integer maxId) {
        this.maxId = maxId;
    }

    public Integer getHead() {
        return head;
    }

    public void setHead(Integer head) {
        this.head = head;
    }

    public Integer getMaxHead() {
        return maxHead;
    }

    public Integer getIdScope() {
        return idScope;
    }
}
