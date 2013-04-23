cd /D %~dp0

echo "BenchmarkClient is starting..."
rem Current dir is：%cd%
echo off
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../lib" "com.jd.bdp.hydra.benchmark.BenchmarkClient" > "../log/Benchmark.log"
cd ..
echo "BenchmarkClient start scheckule is ok,you can check the log file in /log"
pause