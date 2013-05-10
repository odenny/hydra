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

package service;

import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.mysql.service.QueryService;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: biandi
 * Date: 13-5-10
 * Time: 上午10:27
 */
public class QueryServiceTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-mysql.xml"};
        return location;
    }


    @Test
    public void testGetTraceInfo(){
        JSONObject obj = queryService.getTraceInfo(13661784465389L);
        System.out.println(obj.toString());
    }

    private QueryService queryService;

    public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }
}
