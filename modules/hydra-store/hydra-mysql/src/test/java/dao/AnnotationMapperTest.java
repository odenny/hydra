package dao;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.mysql.persistent.dao.AnnotationMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.Absannotation;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.ArrayList;
import java.util.List;

/**
 * User: biandi
 * Date: 13-5-9
 * Time: 下午3:37
 */
public class AnnotationMapperTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-mysql.xml"};
        return location;
    }

    @Test
    public void testGetAnnotations(){
        try {
            annotationMapper.deleteAllAnnotation();
            prepareTestAnnotations();
            List<Span> spans = new ArrayList<Span>();
            Span span1 = new Span();
            span1.setId(1368002575600L);
            spans.add(span1);
            Span span2 = new Span();
            span2.setId(1368002575601L);
            spans.add(span2);
            List<Absannotation> list = annotationMapper.getAnnotations(spans);
            assertEquals(2, list.size());
            boolean f1 = false;
            boolean f2 = false;
            for(Absannotation ann : list){
                if (ann.getKey().equalsIgnoreCase("cs") &&  ann.getValue().equalsIgnoreCase(String.valueOf(1368002575495L))){
                    f1 = true;
                    continue;
                }
                if (ann.getKey().equalsIgnoreCase("dubbo.exception") && ann.getValue().equalsIgnoreCase("abc")){
                    f2 = true;
                    continue;
                }
            }
            assertTrue(f1);
            assertTrue(f2);
        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }finally {
            annotationMapper.deleteAllAnnotation();
        }

    }

    private void prepareTestAnnotations() {
        Absannotation ann1 = new Absannotation();
        ann1.setKey("cs");
        ann1.setValue(String.valueOf(1368002575495L));
        ann1.setSpanId(1368002575600L);

        Absannotation ann2 = new Absannotation();
        ann2.setKey("dubbo.exception");
        ann2.setValue("abc");
        ann2.setSpanId(1368002575601L);

        annotationMapper.addAnnotation(ann1);
        annotationMapper.addAnnotation(ann2);
    }


    private AnnotationMapper annotationMapper;

    public void setAnnotationMapper(AnnotationMapper annotationMapper) {
        this.annotationMapper = annotationMapper;
    }
}
