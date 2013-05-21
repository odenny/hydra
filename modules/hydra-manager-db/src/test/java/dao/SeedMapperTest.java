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


import com.jd.bdp.hydra.mysql.persistent.dao.SeedMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.SeedData;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:27
 */
public class SeedMapperTest extends AbstractDependencyInjectionSpringContextTests {
    private SeedMapper seedMapper;

    public SeedMapper getServerMseeder() {
        return seedMapper;
    }

    public void setServerMseeder(SeedMapper seedMseeder) {
        this.seedMapper = seedMseeder;
    }

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
     *
     * @throws Exception
     */
    @Test
    public void testDataBaseOption() throws Exception {
        //define option-entity and query-entiry
        SeedData seedData = new SeedData();
        seedData.setValue(0);
        SeedData queryPara = null;
        //add entity
        Integer optionId;
        seedMapper.addSeed(seedData);
        optionId = seedData.getId();
        seedData.setId(optionId);
        //1:query entity
        queryPara = seedMapper.getOneSeed(optionId);
        assertNotNull(queryPara);
        assertEquals(seedData, queryPara);
        //modify entity
        seedData.setValue(1);
        seedMapper.updateSeed(seedData);
        //2:query entity
        queryPara = seedMapper.getOneSeed(seedData.getId());
        assertNotNull(queryPara);
        assertEquals(seedData, queryPara);
        //delete entity
        seedMapper.deleteSeed(seedData);
        //3:query entity
        queryPara = seedMapper.getOneSeed(seedData.getId());
        assertNull(queryPara);
    }

    @Test
    public void testGetMaxValue() throws Exception {
        //define option-entity and query-entiry
        SeedData seedData = new SeedData();
        seedData.setValue(0);
        Integer max_value = seedMapper.getMaxSeedValue();
        if (max_value != null) {
            seedData.setValue(max_value + 1);
            seedMapper.addSeed(seedData);
            assertEquals(new Integer(max_value + 1), seedMapper.getMaxSeedValue());
        } else {
            System.out.println("数据库数据有可能为空！");
        }
    }

    @Test
    public void testHasSeed() throws Exception {
        //define option-entity and query-entiry
        SeedData seedData = new SeedData();
        seedData.setValue(0);
        //add entity
        seedMapper.addSeed(seedData);
        //1:query entity
        assertTrue(seedMapper.hasSeed(0));
        //delete entity
        seedMapper.deleteSeed(seedData);
    }

}
