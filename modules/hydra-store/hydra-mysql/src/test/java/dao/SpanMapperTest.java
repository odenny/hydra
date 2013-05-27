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
        String[] location = {"classpath:hydra-mysql.xml"};
        return location;
    }

    @Test
    public void testFindSpanByTraceId(){
        try {
            spanMapper.deleteAllSpans();
            prepareTestSpans();
            List<Span> list = spanMapper.findSpanByTraceId(1368002575495L);
            assertEquals(3, list.size());
            boolean f1 = false;
            boolean f2 = false;
            boolean f3 = false;
            for(Span span : list){
                if (span.getSpanName().equalsIgnoreCase("span1")){
                    f1 = true;
                    continue;
                }
                if (span.getSpanName().equalsIgnoreCase("span2")){
                    f2 = true;
                    continue;
                }
                if (span.getSpanName().equalsIgnoreCase("span3")){
                    f3 = true;
                    continue;
                }
            }
            assertTrue(f1);
            assertTrue(f2);
            assertTrue(f3);
        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }finally {
            spanMapper.deleteAllSpans();
        }
    }

    private void prepareTestSpans() {
        Span s1 = new Span();
        s1.setSpanName("span1");
        s1.setServiceId("10001");
        s1.setTraceId(1368002575495L);
        s1.setId(1368002575600L);

        Span s2 = new Span();
        s2.setSpanName("span2");
        s2.setServiceId("10002");
        s2.setTraceId(1368002575495L);
        s2.setParentId(s1.getId());
        s2.setId(1368002575601L);

        Span s3 = new Span();
        s3.setSpanName("span3");
        s3.setServiceId("10002");
        s3.setTraceId(1368002575495L);
        s3.setParentId(s1.getId());
        s3.setId(1368002575602L);

        spanMapper.addSpan(s1);
        spanMapper.addSpan(s2);
        spanMapper.addSpan(s3);
    }


    private SpanMapper spanMapper;

    public void setSpanMapper(SpanMapper spanMapper) {
        this.spanMapper = spanMapper;
    }
}
