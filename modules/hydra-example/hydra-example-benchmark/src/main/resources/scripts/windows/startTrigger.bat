cd /D %~dp0

echo "BenchmarkClient is starting..."
echo off
java -Xms512m -Xmx512m -Xmn128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:Benchmark-gc.log -Dwrite.statistics=true -Djava.ext.dirs="../conf;../lib"  "com.jd.bdp.hydra.benchmark.startBenchTest.BenchmarkClient" > "../log/Benchmark.log"
cd ..
echo "BenchmarkClient start scheckule is ok,you can check the log file in /log"
pause