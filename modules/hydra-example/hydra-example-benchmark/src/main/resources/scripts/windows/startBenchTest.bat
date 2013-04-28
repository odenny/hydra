cd /D %~dp0
cd ./../
echo "BenchClient is starting..."
echo off
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="conf;lib" -Dbenchmark.properties.file="conf/benchmark.properties" "com.jd.bdp.hydra.benchmark.startBenchTest.BenchmarkClient" > "log/benchmark.log"
echo "Manager start scheckule is ok,you can check the log file in /log"
pause
