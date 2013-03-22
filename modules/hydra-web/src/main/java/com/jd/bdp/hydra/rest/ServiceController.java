package com.jd.bdp.hydra.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * User: biandi
 * Date: 13-3-20
 * Time: 下午1:51
 */
@Controller
@RequestMapping("/rest/service")
public class ServiceController {


    @RequestMapping("/all")
    @ResponseBody
    public JSONArray getAllServices() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < 10; i++) {
            JSONObject obj = new JSONObject();
            obj.put("id", i);
            obj.put("name", "service" + i);
            array.add(obj);
        }
        return array;
    }


    @RequestMapping("/testAjax")
    @ResponseBody
    public JSONObject testAjax(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        obj.put("123", "123");
        return obj;
    }
}
