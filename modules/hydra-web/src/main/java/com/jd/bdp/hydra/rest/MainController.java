package com.jd.bdp.hydra.rest;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User: biandi
 * Date: 13-3-20
 * Time: 下午1:51
 */
@Controller
@RequestMapping("/demos")
public class MainController {

    @RequestMapping("/index.shtml")
    @ResponseBody
    public JSONObject index(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        obj.put("123", "123");
        return obj;
    }
}
