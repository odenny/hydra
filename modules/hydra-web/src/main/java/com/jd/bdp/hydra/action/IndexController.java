package com.jd.bdp.hydra.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: biandi
 * Date: 13-3-20
 * Time: 下午3:28
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

    @RequestMapping("/query.html")
    public String query() {
        return "query";
    }

    @RequestMapping("/trace.html")
    public String trace() {
        return "trace";
    }


}
