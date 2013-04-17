package com.jd.bdp.hydra.collector.service.imp;

import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Span;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yfliuyu
 * Date: 13-4-12
 * Time: 下午4:19
  */
public class HbaseUtils {
    static Annotation getSsAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("ss",a.getValue())){
                return a;
            }
        }
        return null;
    }

    static Annotation getSrAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("sr",a.getValue())){
                return a;
            }
        }
        return null;
    }

    static long byteArray2Long(byte[] value){
        long s = 0;
        long s0 = value[0] & 0xff;
        long s1 = value[1] & 0xff;
        long s2 = value[2] & 0xff;
        long s3 = value[3] & 0xff;
        long s4 = value[4] & 0xff;
        long s5 = value[5] & 0xff;
        long s6 = value[6] & 0xff;
        long s7 = value[7] & 0xff;
        s1 <<= 8;
        s2 <<= 8*2;
        s3 <<= 8*3;
        s4 <<= 8*4;
        s5 <<= 8*5;
        s6 <<= 8*6;
        s7 <<= 8*7;
        s = s0|s1|s2|s3|s4|s5|s6|s7;
        return s;
    }


    static byte[] long2ByteArray(Long value){
        long v = value.longValue();
        byte[] b = new byte[8];
        for(int i = 0 ; i < b.length;i++){
            b[i] = new Long(v & 0xff).byteValue();
            v = v >> 8;
        }
        return b;
    }

    static boolean isTopAnntation(Span span){
        List<Annotation> alist = span.getAnnotations();
        boolean isfirst = false;
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs",a.getValue())){
                isfirst = true;
            }
        }
        return isfirst;
    }

    static Annotation getCsAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs", a.getValue())){
                return a;
            }
        }
        return null;
    }

    static Annotation getCrAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs",a.getValue())){
                return a;
            }
        }
        return null;
    }

}
