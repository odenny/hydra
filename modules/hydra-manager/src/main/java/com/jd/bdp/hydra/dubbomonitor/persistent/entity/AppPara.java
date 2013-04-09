package com.jd.bdp.hydra.dubbomonitor.persistent.entity;

import java.io.Serializable;

/**
 * User: xiangkui
 * Date: 13-4-2
 * Time: 下午3:25
 */
public class AppPara implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppPara)) return false;

        AppPara appPara = (AppPara) o;

        if (id != null ? !id.equals(appPara.id) : appPara.id != null)
            return false;
        if (name != null ? !name.equals(appPara.name) : appPara.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    //getter and setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
