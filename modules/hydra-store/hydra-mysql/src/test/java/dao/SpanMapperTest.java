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

package dao;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.mysql.persistent.dao.SpanMapper;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

/**
 * User: biandi
 * Date: 13-5-8
 * Time: 下午4:55
 */
public class SpanMapperTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-mysql-test.xml"};
        return location;
    }

    @Test
    public void testFindSpanByTraceId(){
        List<Span> list = spanMapper.findSpanByTraceId("161148");
        assertEquals(1, list.size());
    }

    private SpanMapper spanMapper;

    public void setSpanMapper(SpanMapper spanMapper) {
        this.spanMapper = spanMapper;
    }
}
