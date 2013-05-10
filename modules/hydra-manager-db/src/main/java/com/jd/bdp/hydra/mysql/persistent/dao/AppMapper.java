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

package com.jd.bdp.hydra.mysql.persistent.dao;


import com.jd.bdp.hydra.mysql.persistent.entity.AppPara;

import java.util.List;

/**
 * User: xiangkui
 * Date: 13-4-2
 * Time: 下午3:28
 */
public interface AppMapper {
    /*新增加一个应用*/
    void addApp(AppPara appPara);

    //根据name查找AppPara
    AppPara getApp(String name);

    /*删除一个应用*/
    void deleteApp(AppPara appPara);

    /*更新应用信息*/
    void updateApp(AppPara appPara);

    /*获得一个应用实体*/
    AppPara getOneApp(Integer id);

    //删除所有
    void deleteAll();

    //获得所有
    List<AppPara> getAll();
}
