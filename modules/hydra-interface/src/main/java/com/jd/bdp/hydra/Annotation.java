package com.jd.bdp.hydra;

import java.io.Serializable;

/**
 * Date: 13-3-18
 * Time: 下午3:36
 */
public class Annotation implements Serializable {
    public static final String CLIENT_SEND = "cs";
    public static final String CLIENT_RECEIVE = "cr";
    public static final String SERVER_SEND = "ss";
    public static final String SERVER_RECEIVE = "sr";
    private Long timestamp;
    private String value;
    private Endpoint host;
    private Integer duration;

    public Annotation(){

    }
    public Annotation(Long timestamp, String value, Endpoint host) {
        this.timestamp = timestamp;
        this.value = value;
        this.host = host;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Endpoint getHost() {
        return host;
    }

    public void setHost(Endpoint host) {
        this.host = host;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "timestamp=" + timestamp +
                ", value='" + value + '\'' +
                ", host=" + host +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;

        Annotation that = (Annotation) o;

        if (duration!=null&&!duration.equals(that.duration)) return false;
        if (!host.equals(that.host)) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + host.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }
}
