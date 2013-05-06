package com.jd.bdp.hydra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13-3-18
 * Time: 下午3:29
 */
public class Span implements Serializable {
    private Long traceId;
    private Long id;
    private Long parentId; //optional
    private String spanName;
    private String serviceId;
    private List<Annotation> annotations;
    private List<BinaryAnnotation> binaryAnnotations;
    private boolean isSample;

    public boolean isSample() {
        return isSample;
    }

    public void setSample(boolean sample) {
        isSample = sample;
    }

    public Span(){
        annotations = new ArrayList<Annotation>();
        binaryAnnotations = new ArrayList<BinaryAnnotation>();
    }


    public void addAnnotation(Annotation a){
        annotations.add(a);
    }

    public void addBinaryAnnotation(BinaryAnnotation a){
        binaryAnnotations.add(a);
    }


    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getSpanName() {
        return spanName;
    }

    public void setSpanName(String spanName) {
        this.spanName = spanName;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<BinaryAnnotation> getBinaryAnnotations() {
        return binaryAnnotations;
    }

    public void setBinaryAnnotations(List<BinaryAnnotation> binaryAnnotations) {
        this.binaryAnnotations = binaryAnnotations;
    }

    @Override
    public String toString() {
        return "Span{" +
                "traceId=" + traceId +
                ", id=" + id +
                ", parentId=" + parentId +
                ", serviceId=" + serviceId +
                ", spanName='" + spanName + '\'' +
                ", annotations=" + annotations +
                ", binaryAnnotations=" + binaryAnnotations +
                ", isSample=" + isSample +
                '}';
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
