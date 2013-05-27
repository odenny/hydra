/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jd.bdp.hydra.action;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * User: biandi
 * Date: 13-3-25
 * Time: 下午1:23
 */
public class JsVersionFilter implements Filter {

    private Date date;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        date = new Date();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //针对上线更改js之后的版本，用于处理缓存，每次tomcat启动刷新一下版本
        //调试阶段注释掉
//        ((HttpServletRequest)request).getSession().getServletContext().setAttribute("staticVersion", date);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
