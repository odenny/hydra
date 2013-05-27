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

package service;

import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.mysql.persistent.dao.AnnotationMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.SpanMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.Absannotation;
import com.jd.bdp.hydra.store.inter.QueryService;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: biandi
 * Date: 13-5-10
 * Time: 上午10:27
 */
public class QueryServiceTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-mysql.xml"};
        return location;
    }

    @Test
    public void testGetTraceInfo(){
        try {
            spanMapper.deleteAllSpans();
            annotationMapper.deleteAllAnnotation();
            prepareAllTestData();
            JSONObject obj = queryService.getTraceInfo(1368002575495L);
            assertEquals("1368002575495", obj.getString("traceId"));
            assertEquals("1368002575495", obj.getString("minTimestamp"));
            assertEquals("1368002575590", obj.getString("maxTimestamp"));
            assertTrue( obj.getBoolean("available"));
            assertNotNull(obj.get("rootSpan"));
            JSONObject rootSpan = obj.getJSONObject("rootSpan");
            assertEquals("1368002575600", rootSpan.getString("id"));
            assertEquals("95", rootSpan.getString("durationClient"));
            assertEquals("70", rootSpan.getString("durationServer"));
            assertEquals(2, rootSpan.getJSONArray("children").size());
            JSONObject span1 = null;
            JSONObject span2 = null;
            for(Object temp : rootSpan.getJSONArray("children")){
                if (((JSONObject)temp).getString("id").equals("1368002575601")){
                    span1 = (JSONObject) temp;
                }
                if (((JSONObject)temp).getString("id").equals("1368002575602")){
                    span2 = (JSONObject) temp;
                }
            }
            assertNotNull(span1);
            assertNotNull(span2);
            assertEquals("30", span1.getString("durationClient"));
            assertEquals("15", span1.getString("durationServer"));
            assertEquals("25", span2.getString("durationClient"));
            assertEquals("15", span2.getString("durationServer"));
            assertNotNull(span2.get("exception"));
            JSONObject e = span2.getJSONObject("exception");
            assertEquals("dubbo.exception", e.getString("key"));
            assertEquals("abc", e.getString("value"));
        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }finally {
            spanMapper.deleteAllSpans();
            annotationMapper.deleteAllAnnotation();
        }
    }

    private void prepareAllTestData() {
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


        Absannotation ann1 = new Absannotation();
        ann1.setKey("cs");
        ann1.setTimestamp(1368002575495L);
        ann1.setSpanId(1368002575600L);
        ann1.setTraceId(1368002575495L);

        Absannotation ann2 = new Absannotation();
        ann2.setKey("cr");
        ann2.setTimestamp(1368002575590L);
        ann2.setSpanId(1368002575600L);
        ann2.setTraceId(1368002575495L);

        Absannotation ann3 = new Absannotation();
        ann3.setKey("sr");
        ann3.setTimestamp(1368002575510L);
        ann3.setSpanId(1368002575600L);
        ann3.setTraceId(1368002575495L);

        Absannotation ann4 = new Absannotation();
        ann4.setKey("ss");
        ann4.setTimestamp(1368002575580L);
        ann4.setSpanId(1368002575600L);
        ann4.setTraceId(1368002575495L);


        Absannotation ann5 = new Absannotation();
        ann5.setKey("cs");
        ann5.setTimestamp(1368002575520L);
        ann5.setSpanId(1368002575601L);
        ann5.setTraceId(1368002575495L);


        Absannotation ann6 = new Absannotation();
        ann6.setKey("cr");
        ann6.setTimestamp(1368002575550L);
        ann6.setSpanId(1368002575601L);
        ann6.setTraceId(1368002575495L);


        Absannotation ann7 = new Absannotation();
        ann7.setKey("sr");
        ann7.setTimestamp(1368002575525L);
        ann7.setSpanId(1368002575601L);
        ann7.setTraceId(1368002575495L);

        Absannotation ann8 = new Absannotation();
        ann8.setKey("ss");
        ann8.setTimestamp(1368002575540L);
        ann8.setSpanId(1368002575601L);
        ann8.setTraceId(1368002575495L);

        Absannotation ann9 = new Absannotation();
        ann9.setKey("cs");
        ann9.setTimestamp(1368002575555L);
        ann9.setSpanId(1368002575602L);
        ann9.setTraceId(1368002575495L);

        Absannotation ann10 = new Absannotation();
        ann10.setKey("cr");
        ann10.setTimestamp(1368002575580L);
        ann10.setSpanId(1368002575602L);
        ann10.setTraceId(1368002575495L);

        Absannotation ann11 = new Absannotation();
        ann11.setKey("sr");
        ann11.setTimestamp(1368002575560L);
        ann11.setSpanId(1368002575602L);
        ann11.setTraceId(1368002575495L);

        Absannotation ann12 = new Absannotation();
        ann12.setKey("ss");
        ann12.setTimestamp(1368002575575L);
        ann12.setSpanId(1368002575602L);
        ann12.setTraceId(1368002575495L);

        Absannotation annEx = new Absannotation();
        annEx.setKey("dubbo.exception");
        annEx.setValue("abc");
        annEx.setSpanId(1368002575602L);
        annEx.setTraceId(1368002575495L);

        annotationMapper.addAnnotation(ann1);
        annotationMapper.addAnnotation(ann2);
        annotationMapper.addAnnotation(ann3);
        annotationMapper.addAnnotation(ann4);
        annotationMapper.addAnnotation(ann5);
        annotationMapper.addAnnotation(ann6);
        annotationMapper.addAnnotation(ann7);
        annotationMapper.addAnnotation(ann8);
        annotationMapper.addAnnotation(ann9);
        annotationMapper.addAnnotation(ann10);
        annotationMapper.addAnnotation(ann11);
        annotationMapper.addAnnotation(ann12);
        annotationMapper.addAnnotation(annEx);
    }

    private QueryService queryService;
    private SpanMapper spanMapper;
    private AnnotationMapper annotationMapper;

    public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }

    public void setSpanMapper(SpanMapper spanMapper) {
        this.spanMapper = spanMapper;
    }

    public void setAnnotationMapper(AnnotationMapper annotationMapper) {
        this.annotationMapper = annotationMapper;
    }
}
