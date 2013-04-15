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

package com.jd.bdp.sending;

import com.jd.bdp.hydra.Span;

import java.util.ArrayList;
import java.util.List;

/**
 * User: biandi
 * Date: 13-4-12
 * Time: 上午10:04
 */
public class TestCollectSpanServiceImpl implements TestCollectSpanService{

    @Override
    public synchronized void setSpans(List<Span> list) {
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        result.addAll(list);
    }

    @Override
    public List<Span> getAllSpan() {
        return result;
    }

    @Override
    public void clear() {
        result.clear();
    }

    private List<Span> result = new ArrayList<Span>();
}
