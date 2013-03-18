package com.jd.bdp.hydra;

/**
  * User: yfliuyu
 * Date: 13-3-18
 * Time: 下午3:36
 */
public class Annotation {
    public final String CLIENT_SEND = "cs";
    public final String CLIENT_RECEIVE = "cr";
    public final String SERVER_SEND = "ss";
    public final String SERVER_RECEIVE = "sr";
    private Long timestamp;
    private String value;
    private Endpoint host;
    private Integer duration;

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
}
