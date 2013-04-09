package com.jd.bdp.trigger;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:42
 */

import org.junit.Test;

/**
 * 冒烟测试，简单的触发A服务，开始调用B服务
 */
public interface MockTest {
    //触发A服务工作
    @Test
    public void testTriggerService() throws Exception;
}
