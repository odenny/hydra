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


import com.jd.bdp.hydra.mysql.persistent.dao.AppMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.ServiceMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.AppPara;
import com.jd.bdp.hydra.mysql.persistent.entity.ServicePara;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:27
 */
public class ServiceMapperTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:/hydra-manager-db.xml"};
        return location;
    }


    /**
     * # 测试数据库 #
     * 测试前提：所操作的数据库为空
     * 测试策略：
     * 1:增加一个实体 Entity，查询确认添加成功
     * 2：修改这个实体Entity,查询这个实体，确认修改成功
     * 3：删除Entity，查询，确认为null
     * @throws Exception
     */
    @Test
    public void testDataBaseOption()throws Exception{
        try {
            AppPara app = new AppPara();
            app.setName("myApp");
            appMapper.addApp(app);

            //define option-entity and query-entiry
            ServicePara servicePara=new ServicePara();
            servicePara.setId("1");
            servicePara.setName("com.jd.car");
            servicePara.setAppId(app.getId());
            ServicePara queryPara=null;
            //add entity
            serviceMapper.addService(servicePara);
            //1:query entity
            queryPara=serviceMapper.getOneService(servicePara.getId());
            assertNotNull(queryPara);
            assertEquals(servicePara, queryPara);
            //modify entity
            servicePara.setName("com.jd.payment");

            serviceMapper.updateService(servicePara);
            //2:query entity
            queryPara=serviceMapper.getOneService(servicePara.getId());
            assertNotNull(queryPara);
            assertEquals(servicePara, queryPara);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //delete entity
            try {
                serviceMapper.deleteAll();
                appMapper.deleteAll();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    //测试根据name和appId查找
    @Test
    public void testFindByName(){
        try {
            AppPara app = new AppPara();
            app.setName("myApp");
            appMapper.addApp(app);
            Integer appId = app.getId();

            ServicePara servicePara1 = new ServicePara();
            servicePara1.setId("1");
            servicePara1.setName("myService1");
            servicePara1.setAppId(app.getId());

            serviceMapper.addService(servicePara1);

            ServicePara servicePara2 = new ServicePara();
            servicePara2.setId("2");
            servicePara2.setName("myService2");
            servicePara2.setAppId(appId);

            serviceMapper.addService(servicePara2);

            assertNotNull(servicePara1);
            assertNotNull(servicePara2);
            String id1 = servicePara1.getId();
            String id2 = servicePara2.getId();
            assertNotNull(id1);
            assertNotNull(id2);

            ServicePara s1 = serviceMapper.getService("myService1", appId);
            ServicePara s2 = serviceMapper.getService("myService2", appId);

            assertEquals(id1, s1.getId());
            assertEquals(id2, s2.getId());
            assertEquals(appId, s1.getAppId());
        }catch (Exception e){
           e.printStackTrace();
        }finally {
            //最后删除所有的测试数据
            serviceMapper.deleteAll();
            appMapper.deleteAll();
        }
    }

    private ServiceMapper serviceMapper;
    private AppMapper appMapper;

    public void setServerMapper(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    public void setAppMapper(AppMapper appMapper) {
        this.appMapper = appMapper;
    }
}
