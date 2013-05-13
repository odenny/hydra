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

package com.jd.bdp.hydra.mysql.persistent.dao.impl;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.mysql.persistent.dao.AnnotationMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.Absannotation;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-5-8
 * Time: 下午3:29
 */
public class AnnotationMapperImpl implements AnnotationMapper {

    @Override
    public void addAnnotation(Absannotation absannotation){
        sqlSession.insert("addAnnotation", absannotation);
    }

    @Override
    public List<Absannotation> getAnnotations(List<Span> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("spans", list);
        return (List<Absannotation>)sqlSession.selectList("getAnnotations", map);
    }

    @Override
    public void deleteAllAnnotation() {
        sqlSession.delete("deleteAllAnnotation");
    }

    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }
}
