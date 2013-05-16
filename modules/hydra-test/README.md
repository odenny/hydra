Hydra 测试
================================================

功能需求
-----------------------------------------------
### 桩测试
        hydra-test-modules 为模块间测试(桩测试)
### 集成测试
        hydra-test-integration 为集成测试，附带完成基准测试+性能测试
<table>
    <tr>
        <th>角色</th>
        <th>ip</th>
        <th>备注</th>
    </tr>
    <tr>
        <td>Manager</td>
        <td>192.168.228.81</td>
        <td>管理中心</td>
    </tr>
    <tr>
        <td>Collector</td>
        <td>192.168.227.48</td>
        <td>搜集器</td>
    </tr>
    <tr>
        <td>Benchmark/Trigger</td>
        <td>192.168.227.48</td>
        <td>基准测试/冒烟触发器</td>
    </tr>
    <tr>
        <td>A</td>
        <td>192.168.227.83</td>
        <td>A</td>
    </tr>
    <tr>
        <td>B</td>
        <td>192.168.227.86</td>
        <td>B</td>
    </tr>
    <tr>
        <td>C</td>
        <td>192.168.228.85</td>
        <td>C</td>
    </tr>

</table>
### 性能测试
        benchmark模块提供基准测试框架，集成测试中完成具体性能测试

### 接口测试
        对跟踪数据存储接口的性能测试，针对hydra-store-interface的测试（Mysql环境下)
        依赖于JMeter工具





