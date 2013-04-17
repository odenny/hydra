package com.jd.bdp.hydra.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.collector.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: biandi
 * Date: 13-3-21
 * Time: 上午11:05
 */
@Controller
@RequestMapping("/rest/trace")
public class TraceController {


    @RequestMapping("/{traceId}")
    @ResponseBody
    public JSONObject getTrace(@PathVariable String traceId) {
        JSONArray array = queryService.getTraceInfo(Long.parseLong(traceId));

        return null;
//        JSONObject obj = new JSONObject();
//        obj.put("traceId", traceId);
//        obj.put("serviceName", "testService|getUser");
//
//        //root
//
//        JSONObject root = new JSONObject();
//        root.put("spanId", "span0001");
//        root.put("spanName", "getSpan1");
//
//        JSONArray rootAnnArray = new JSONArray();
//        JSONObject rootAnn1 = new JSONObject();
//        rootAnn1.put("cs", "1363910400123");
//        rootAnnArray.add(rootAnn1);
//
//        JSONObject rootAnn2 = new JSONObject();
//        rootAnn2.put("ss", "1363910400582");
//        rootAnnArray.add(rootAnn2);
//
//        JSONObject rootAnn3 = new JSONObject();
//        rootAnn3.put("sr", "1363910400170");
//        rootAnnArray.add(rootAnn3);
//
//        JSONObject rootAnn4 = new JSONObject();
//        rootAnn4.put("cr", "1363910400602");
//        rootAnnArray.add(rootAnn4);
//
//        root.put("annotations", rootAnnArray);
//
//        JSONArray rootChildren = new JSONArray();
//
//        //spanA
//
//
//        JSONObject spanA = new JSONObject();
//        spanA.put("spanId", "span0002");
//        spanA.put("spanName", "getSpanA");
//
//        JSONArray spanAAnnArray = new JSONArray();
//        JSONObject spanAAnn1 = new JSONObject();
//        spanAAnn1.put("cs", "1363910400150");
//        spanAAnnArray.add(spanAAnn1);
//
//        JSONObject spanAAnn2 = new JSONObject();
//        spanAAnn2.put("ss", "1363910400278");
//        spanAAnnArray.add(spanAAnn2);
//
//        JSONObject spanAAnn3 = new JSONObject();
//        spanAAnn3.put("sr", "1363910400155");
//        spanAAnnArray.add(spanAAnn3);
//
//        JSONObject spanAAnn4 = new JSONObject();
//        spanAAnn4.put("cr", "1363910400300");
//        spanAAnnArray.add(spanAAnn4);
//
//        spanA.put("annotations", spanAAnnArray);
//
//        spanA.put("children", new JSONArray());
//
//        rootChildren.add(spanA);
//
//        JSONArray spanAChildren = new JSONArray();
//
//        //spanD
//        JSONObject spanD = new JSONObject();
//        spanD.put("spanId", "span0006");
//        spanD.put("spanName", "getSpanD");
//
//
//        JSONArray spanDAnnArray = new JSONArray();
//        JSONObject spanDAnn1 = new JSONObject();
//        spanDAnn1.put("cs", "1363910400158");
//        spanDAnnArray.add(spanDAnn1);
//
//        JSONObject spanDAnn2 = new JSONObject();
//        spanDAnn2.put("ss", "1363910400262");
//        spanDAnnArray.add(spanDAnn2);
//
//        JSONObject spanDAnn3 = new JSONObject();
//        spanDAnn3.put("sr", "1363910400180");
//        spanDAnnArray.add(spanDAnn3);
//
//        JSONObject spanDAnn4 = new JSONObject();
//        spanDAnn4.put("cr", "1363910400270");
//        spanDAnnArray.add(spanDAnn4);
//
//        spanD.put("annotations", spanDAnnArray);
//
//        spanAChildren.add(spanD);
//        spanA.put("children", spanAChildren);
//
//        //spanB
//
//
//        JSONObject spanB = new JSONObject();
//        spanB.put("spanId", "span0003");
//        spanB.put("spanName", "getSpanB");
//
//        JSONArray spanBAnnArray = new JSONArray();
//        JSONObject spanBAnn1 = new JSONObject();
//        spanBAnn1.put("cs", "1363910400310");
//        spanBAnnArray.add(spanBAnn1);
//
//        JSONObject spanBAnn2 = new JSONObject();
//        spanBAnn2.put("ss", "1363910400560");
//        spanBAnnArray.add(spanBAnn2);
//
//        JSONObject spanBAnn3 = new JSONObject();
//        spanBAnn3.put("sr", "1363910400335");
//        spanBAnnArray.add(spanBAnn3);
//
//        JSONObject spanBAnn4 = new JSONObject();
//        spanBAnn4.put("cr", "1363910400570");
//        spanBAnnArray.add(spanBAnn4);
//
//        spanB.put("annotations", spanBAnnArray);
//
//        rootChildren.add(spanB);
//
//        JSONArray spanBChildren = new JSONArray();
//
//        //spanC1
//        JSONObject spanC1 = new JSONObject();
//        spanC1.put("spanId", "span0004");
//        spanC1.put("spanName", "getSpanC1");
//
//
//        JSONArray spanC1AnnArray = new JSONArray();
//        JSONObject spanC1Ann1 = new JSONObject();
//        spanC1Ann1.put("cs", "1363910400365");
//        spanC1AnnArray.add(spanC1Ann1);
//
//        JSONObject spanC1Ann2 = new JSONObject();
//        spanC1Ann2.put("ss", "1363910400390");
//        spanC1AnnArray.add(spanC1Ann2);
//
//        JSONObject spanC1Ann3 = new JSONObject();
//        spanC1Ann3.put("sr", "1363910400375");
//        spanC1AnnArray.add(spanC1Ann3);
//
//        JSONObject spanC1Ann4 = new JSONObject();
//        spanC1Ann4.put("cr", "1363910400400");
//        spanC1AnnArray.add(spanC1Ann4);
//
//        spanC1.put("annotations", spanC1AnnArray);
//
//        spanBChildren.add(spanC1);
//
//        //spanC2
//        JSONObject spanC2 = new JSONObject();
//        spanC2.put("spanId", "span0005");
//        spanC2.put("spanName", "getSpanC2");
//
//        JSONArray spanC2AnnArray = new JSONArray();
//        JSONObject spanC2Ann1 = new JSONObject();
//        spanC2Ann1.put("cs", "1363910400405");
//        spanC2AnnArray.add(spanC2Ann1);
//
//        JSONObject spanC2Ann2 = new JSONObject();
//        spanC2Ann2.put("ss", "1363910400525");
//        spanC2AnnArray.add(spanC2Ann2);
//
//        JSONObject spanC2Ann3 = new JSONObject();
//        spanC2Ann3.put("sr", "1363910400412");
//        spanC2AnnArray.add(spanC2Ann3);
//
//        JSONObject spanC2Ann4 = new JSONObject();
//        spanC2Ann4.put("cr", "1363910400545");
//        spanC2AnnArray.add(spanC2Ann4);
//
//        spanC2.put("annotations", spanC2AnnArray);
//
//
//        spanBChildren.add(spanC2);
//
//        spanB.put("children", spanBChildren);
//
//        root.put("children", rootChildren);
//
//        obj.put("rootSpan", root);
//
//        return obj;
    }


    @Autowired
    private QueryService queryService;



}
