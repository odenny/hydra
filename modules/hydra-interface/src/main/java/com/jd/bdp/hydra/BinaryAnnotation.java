package com.jd.bdp.hydra;

import java.io.Serializable;
import java.util.Arrays;

/**
 * User: yfliuyu
 * Date: 13-3-18
 * Time: 下午3:36
 */
public class BinaryAnnotation implements Serializable {
    private String key;
    private byte[] value;
    private String type;
    private Integer duration;
    private Endpoint host;

    public Endpoint getHost() {
        return host;
    }

    public void setHost(Endpoint endpoint) {
        this.host = endpoint;
    }

    @Override
    public String toString() {
        return "BinaryAnnotation{" +
                "key='" + key + '\'' +
                ", value=" + Arrays.toString(value) +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                ", endpoint=" + host +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
