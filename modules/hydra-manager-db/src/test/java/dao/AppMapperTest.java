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
import com.jd.bdp.hydra.mysql.persistent.entity.AppPara;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:27
 */
public class AppMapperTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-manager-db.xml"};
        return location;
    }

    /**
     * # 测试数据库 #
     * 测试前提：所操作的数据库为空
     * 测试策略：
     * 1:增加一个实体 Entity，查询确认添加成功
     * 2：修改这个实体Entity,查询这个实体，确认修改成功
     * 3：删除Entity，查询，确认为null
     *
     * @throws Exception
     */
    @Test
    public void testDataBaseOption() throws Exception {
        //define option-entity and query-entiry
        AppPara appPara = new AppPara();
        appPara.setName("payment");
        AppPara queryPara = null;
        try {
            //add entity
            appMapper.addApp(appPara);
            //1:query entity
            queryPara = appMapper.getOneApp(appPara.getId());
            assertNotNull(queryPara);
            assertEquals(appPara, queryPara);
            //modify entity
            appPara.setName("transmit");
            appMapper.updateApp(appPara);
            //2:query entity
            queryPara = appMapper.getOneApp(appPara.getId());
            assertNotNull(queryPara);
            assertEquals(appPara, queryPara);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //delete entity
            appMapper.deleteAll();

            //3:query entity
            queryPara = appMapper.getOneApp(appPara.getId());
            assertNull(queryPara);
        }

    }


    //测试根据名称查找App
    @Test
    public void testFindByName(){
        AppPara appPara = new AppPara();
        appPara.setName("payment");
        AppPara queryPara = null;
        try {
            //add entity
            appMapper.addApp(appPara);
            //1:query entity
            AppPara queryApp = appMapper.getApp("payment");
            assertNotNull(queryApp);
            assertEquals(appPara, queryApp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //delete entity
            appMapper.deleteAll();

            //3:query entity
            queryPara = appMapper.getOneApp(appPara.getId());
            assertNull(queryPara);
        }
    }

    private AppMapper appMapper;

    public void setAppMapper(AppMapper appMapper) {
        this.appMapper = appMapper;
    }
}
