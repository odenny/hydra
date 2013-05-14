package com.jd.bdp.hydra.mysql.service.impl;

import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Span;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * User: yfliuyu
 * Date: 13-5-10
 * Time: 上午11:02
 */
public class Utils {

    public static boolean isTopAnntation(Span span){
        List<Annotation> alist = span.getAnnotations();
        boolean isfirst = false;
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs", a.getValue())){
                isfirst = true;
            }
        }
        return isfirst;
    }

    public static Annotation getCsAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs", a.getValue())){
                return a;
            }
        }
        return null;
    }

    public static Annotation getCrAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cr",a.getValue())){
                return a;
            }
        }
        return null;
    }

    public static boolean isRoot(Span span) {
        return span.getParentId() == null;
    }
}
