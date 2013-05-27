package com.jd.bdp.hydra.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.store.inter.QueryService;
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


    @RequestMapping("/list/{serviceId}/{startTime}/{durationMin}/{durationMax}/{sum}")
    @ResponseBody
    public JSONArray getTraces(@PathVariable String serviceId, @PathVariable long startTime, @PathVariable int durationMin, @PathVariable int durationMax, @PathVariable int sum) {
        try {
            return queryService.getTracesByDuration(serviceId, startTime, sum, durationMin, durationMax);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/list/ex/{serviceId}/{startTime}/{sum}")
    @ResponseBody
    public JSONArray getTraces(@PathVariable String serviceId, @PathVariable long startTime, @PathVariable int sum) {
        try {
            return queryService.getTracesByEx(serviceId, startTime, sum);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/one/{traceId}")
    @ResponseBody
    public JSONObject getTrace(@PathVariable String traceId) {
        try {
            return queryService.getTraceInfo(Long.parseLong(traceId));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Autowired
    private QueryService queryService;
}
