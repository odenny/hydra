package com.jd.bdp.hydra.dubbomonitor.persistent.dao;

import com.jd.bdp.hydra.dubbomonitor.persistent.entity.AppPara;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.ServicePara;
import junit.framework.Assert;
import junit.framework.Assert.*;
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
        String[] location = {"classpath:/hydra-manager.xml"};
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
        AppPara app = new AppPara();
        app.setName("myApp");
        appMapper.addApp(app);
        Integer appId = app.getId();

        ServicePara servicePara1 = new ServicePara();
        servicePara1.setName("myService1");
        servicePara1.setAppId(app.getId());

        serviceMapper.addService(servicePara1);

        ServicePara servicePara2 = new ServicePara();
        servicePara2.setName("myService2");
        servicePara2.setAppId(appId);

        serviceMapper.addService(servicePara2);

        assertNotNull(servicePara1);
        assertNotNull(servicePara2);
        Integer id1 = servicePara1.getId();
        Integer id2 = servicePara2.getId();
        assertTrue(id2 > id1);

        ServicePara s1 = serviceMapper.getService("myService1", appId);
        ServicePara s2 = serviceMapper.getService("myService2", appId);

        assertEquals(id1, s1.getId());
        assertEquals(id2, s2.getId());
        assertEquals(appId, s1.getAppId());

        //最后删除所有的测试数据

        serviceMapper.deleteService(servicePara1);
        serviceMapper.deleteService(servicePara2);

        appMapper.deleteApp(app);

        assertNull(serviceMapper.getOneService(id1));
        assertNull(serviceMapper.getOneService(id2));
        assertNull(appMapper.getOneApp(appId));
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
