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

package dao;

import com.jd.bdp.hydra.mysql.persistent.dao.ServiceIdGenMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.ServiceIdGen;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: biandi
 * Date: 13-4-16
 * Time: 上午10:34
 */
public class ServiceIdGenMapperTest extends AbstractDependencyInjectionSpringContextTests {


    @Test
    public void testGet(){
        ServiceIdGen serviceIdGen = serviceIdGenMapper.getServiceIdGen();
        assertNotNull(serviceIdGen);
        assertNotNull(serviceIdGen.getMaxId());
        assertNotNull(serviceIdGen.getMaxHead());
        assertNotNull(serviceIdGen.getHead());
        assertTrue(serviceIdGen.getMaxHead() >= serviceIdGen.getHead());
    }

    @Test
    public void testUpdate() {
        ServiceIdGen serviceIdGen = serviceIdGenMapper.getServiceIdGen();
        int tempId = serviceIdGen.getMaxId() + 1;
        serviceIdGen.setMaxId(tempId + 1);
        serviceIdGenMapper.updateServiceIdGen(serviceIdGen);
        serviceIdGen = serviceIdGenMapper.getServiceIdGen();
        assertTrue(serviceIdGen.getMaxId() > tempId);
    }

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-manager-db.xml"};
        return location;
    }

    private ServiceIdGenMapper serviceIdGenMapper;

    public void setServiceIdGenMapper(ServiceIdGenMapper serviceIdGenMapper) {
        this.serviceIdGenMapper = serviceIdGenMapper;
    }


}
