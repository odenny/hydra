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
 * Date: 13-4-3
 * Time: 下午3:53
 */
public class SeedData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer value;

    //getter and setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeedData)) return false;

        SeedData seedData = (SeedData) o;

        if (!id.equals(seedData.id)) return false;
        if (!value.equals(seedData.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
