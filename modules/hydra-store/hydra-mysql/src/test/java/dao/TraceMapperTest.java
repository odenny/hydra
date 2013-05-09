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

import com.jd.bdp.hydra.mysql.persistent.dao.TraceMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.Trace;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.Date;
import java.util.List;

/**
 * User: biandi
 * Date: 13-5-8
 * Time: 下午4:35
 */
public class TraceMapperTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-mysql.xml"};
        return location;
    }

    @Test
    public void testFindTraces(){
        List<Trace> list = traceMapper.findTraces("161148", new Date(1368002575499L), 3);
        assertEquals(1, list.size());
    }

    @Test
    public void testFindTracesByDuration(){
        List<Trace> list = traceMapper.findTracesByDuration("161148", new Date(1368002575499L), 10, 20, 1);
        assertEquals(1, list.size());
    }

    @Test
    public void testFindTracesEx(){
        List<Trace> list = traceMapper.findTracesEx("161148", new Date(1368002575499L), 3);
        assertEquals(1, list.size());
    }

    private TraceMapper traceMapper;

    public void setTraceMapper(TraceMapper traceMapper) {
        this.traceMapper = traceMapper;
    }


}
