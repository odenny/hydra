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

import com.jd.bdp.hydra.mysql.persistent.dao.AnnotationMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.TraceMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.Absannotation;
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
        try {
            traceMapper.deleteAllTraces();
            annotationMapper.deleteAllAnnotation();
            prepareTestTraces();
            List<Trace> list = traceMapper.findTraces("161148", 1368002575490L, 2);
            assertEquals(2, list.size());
            for (Trace trace : list) {
                assertEquals("161148", trace.getService());
            }
        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }finally {
            traceMapper.deleteAllTraces();
            annotationMapper.deleteAllAnnotation();
        }

    }

    @Test
    public void testFindTracesByDuration(){
        try {
            traceMapper.deleteAllTraces();
            annotationMapper.deleteAllAnnotation();
            prepareTestTraces();
            List<Trace> list = traceMapper.findTracesByDuration("161148", 1368002575499L, 10, 20, 2);
            assertEquals(2, list.size());
            for(Trace trace : list){
                assertEquals("161148", trace.getService());
            }
        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }finally {
            traceMapper.deleteAllTraces();
            annotationMapper.deleteAllAnnotation();
        }
    }

    @Test
    public void testFindTracesEx(){
        try {
            traceMapper.deleteAllTraces();
            annotationMapper.deleteAllAnnotation();
            prepareTestTraces();
            List<Trace> list = traceMapper.findTracesEx("161148", 1368002575495L, 3);
            assertEquals(1, list.size());
            for(Trace trace : list){
                assertEquals("161148", trace.getService());
            }
        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }finally {
            traceMapper.deleteAllTraces();
            annotationMapper.deleteAllAnnotation();
        }

    }

    private void prepareTestTraces(){
        Trace t1 = new Trace();
        t1.setTraceId(1368002575499L);
        t1.setDuration(10);
        t1.setService("161148");
        t1.setTime(1368002575499L);

        Trace t2 = new Trace();
        t2.setTraceId(1368002575498L);
        t2.setDuration(15);
        t2.setService("161148");
        t2.setTime(1368002575499L);

        Trace t3 = new Trace();
        t3.setTraceId(1368002575497L);
        t3.setDuration(15);
        t3.setService("161149");
        t3.setTime(1368002575499L);

        Trace t4 = new Trace();
        t4.setTraceId(1368002575496L);
        t4.setDuration(15);
        t4.setService("161149");
        t4.setTime(1368002575499L);

        Trace t5 = new Trace();
        t5.setTraceId(1368002575495L);
        t5.setDuration(20);
        t5.setService("161148");
        t5.setTime(1368002575499L);

        traceMapper.addTrace(t1);
        traceMapper.addTrace(t2);
        traceMapper.addTrace(t3);
        traceMapper.addTrace(t4);
        traceMapper.addTrace(t5);

        Absannotation annotation = new Absannotation();
        annotation.setIp("192.168.0.1");
        annotation.setPort(88);
        annotation.setKey("dubbo.exception");
        annotation.setValue("abc");
        annotation.setTraceId(1368002575495L);
        annotation.setService("161148");
        annotationMapper.addAnnotation(annotation);
    }

    private TraceMapper traceMapper;

    private AnnotationMapper annotationMapper;

    public void setTraceMapper(TraceMapper traceMapper) {
        this.traceMapper = traceMapper;
    }


    public void setAnnotationMapper(AnnotationMapper annotationMapper) {
        this.annotationMapper = annotationMapper;
    }
}
