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
        List<Span> spans = new ArrayList<Span>();
        Span span1 = new Span();
        span1.setId(1L);
        spans.add(span1);
        Span span2 = new Span();
        span2.setId(2L);
        spans.add(span2);
        List<Absannotation> list = annotationMapper.getAnnotations(spans);
        assertEquals(2, list.size());
    }

    private AnnotationMapper annotationMapper;

    public void setAnnotationMapper(AnnotationMapper annotationMapper) {
        this.annotationMapper = annotationMapper;
    }
}
