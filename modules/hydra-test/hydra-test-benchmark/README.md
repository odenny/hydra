基准测试
================================================

功能需求
-----------------------------------------------
### 触发模拟应用场景一，快速演示Hydra实际效果。
### 接入hydra和不接入hydra应用系统性能对比。


### 应答表
<table>
    <tr>
        <th>索引</th>
        <th>类型</th>
        <th>内容</th>
    </tr>
    <tr>
        <td>0</td>
        <td>long[]</td>
        <td>responseSpreads</td>
    </tr>
    <tr>
        <td>1</td>
        <td>long[]</td>
        <td>tps</td>
    </tr>
    <tr>
        <td>2</td>
        <td>long[]</td>
        <td>responseTimes</td>
    </tr>
    <tr>
        <td>3</td>
        <td>long[]</td>
        <td>errorTPS</td>
    </tr>
    <tr>
        <td>4</td>
        <td>long[]</td>
        <td>errorResponseTimes</td>
    </tr>
</table>

### 使用说明
        1:mvn package -Dmaven.test.skip=true
        2:cd hydra-example-benchmark*
        3:cd conf
           config the benchmark.properties and dubbo.properties
        4:cd bin
        5:startABC.sh and startBenchmark.sh