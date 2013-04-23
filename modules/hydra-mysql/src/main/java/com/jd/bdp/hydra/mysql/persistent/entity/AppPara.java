/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jd.bdp.hydra.mysql.persistent.entity;

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
