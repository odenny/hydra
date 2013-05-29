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
        <td>192.168.228.81</td>
        <td>搜集器</td>
    </tr>
    <tr>
        <td>Benchmark/Trigger</td>
        <td>192.168.200.113</td>
        <td>基准测试/冒烟触发器</td>
    </tr>
    <tr>
        <td>A </td>
        <td>192.168.200.110</td>
        <td>A</td>
    </tr>
    <tr>
        <td>B</td>
        <td>192.168.200.111</td>
        <td>B</td>
    </tr>
    <tr>
        <td>C</td>
        <td>192.168.200.112</td>
        <td>C</td>
    </tr>
    <tr>
        <td>A-exp2</td>
        <td>192.168.200.110</td>
        <td>A</td>
    </tr>
    <tr>
        <td>B-exp2</td>
        <td>192.168.200.111</td>
        <td>B</td>
    </tr>
    <tr>
        <td>C1-exp2</td>
        <td>192.168.200.112</td>
        <td>C1</td>
    </tr>
    <tr>
        <td>C2-exp2</td>
        <td>192.168.200.112</td>
        <td>C2</td>
    </tr>
    <tr>
        <td>E-exp2</td>
        <td>192.168.200.113</td>
        <td>E</td>
    </tr>
    <tr>
        <td>D1-exp2</td>
        <td>192.168.200.113</td>
        <td>D1</td>
    </tr>
    <tr>
        <td>D2-exp2</td>
        <td>192.168.200.113</td>
        <td>D2</td>
    </tr>


</table>
### 性能测试
        benchmark模块提供基准测试框架，集成测试中完成具体性能测试

### 接口测试
        对跟踪数据存储接口的性能测试，针对hydra-store-interface的测试（Mysql环境下)
        依赖于JMeter工具
