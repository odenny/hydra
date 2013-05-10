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
        <td>A</td>
        <td>192.168.227.83</td>
        <td></td>
    </tr>
    <tr>
        <td>B</td>
        <td>192.168.227.86</td>
        <td></td>
    </tr>
    <tr>
        <td>C</td>
        <td>192.168.228.85</td>
        <td></td>
    </tr>
    <tr>
        <td>Manager</td>
        <td>192.168.227.48</td>
        <td>管理中心</td>
    </tr>
    <tr>
        <td>Benchmark</td>
        <td>192.168.228.81</td>
        <td>基准测试</td>
    </tr>
</table>
### 性能测试
        hydra-test-benchmark 提供基准测试框架，集成测试中完成具体性能测试

##接入Hydra前
        ----------Benchmark Statistics--------------
         Concurrents: 10
         ClientNums: 1
         Runtime: 50 seconds
         Benchmark Time: 11
         Requests: 48872 Success: 100% (48872) Error: 0% (0)
         Avg TPS: 2398 Max TPS: 2639 Min TPS: 2182
         Avg RT: 4.166ms
         RT <= 0: 0% 417/48872
         RT (0,1]: 13% 6419/48872
         RT (1,5]: 76% 37171/48872
         RT (5,10]: 4% 2204/48872
         RT (10,50]: 5% 2644/48872
         RT (50,100]: 0% 17/48872
         RT (100,500]: 0% 0/48872
         RT (500,1000]: 0% 0/48872
         RT > 1000: 0% 0/48872

##接入Hydra后
       ----------Benchmark Statistics--------------
        Concurrents: 10
        ClientNums: 1
        Runtime: 50 seconds
        Benchmark Time: 11
        Requests: 39102 Success: 100% (39102) Error: 0% (0)
        Avg TPS: 1957 Max TPS: 2336 Min TPS: 1653
        Avg RT: 5.109ms
        RT <= 0: 0% 339/39102
        RT (0,1]: 13% 5267/39102
        RT (1,5]: 72% 28539/39102
        RT (5,10]: 4% 1909/39102
        RT (10,50]: 7% 2991/39102
        RT (50,100]: 0% 34/39102
        RT (100,500]: 0% 23/39102
        RT (500,1000]: 0% 0/39102
        RT > 1000: 0% 0/39102
