cd /D %~dp0
CALL env-bench.bat
echo "BenchmarkClient is starting..."
java -Xms512m -Xmx512m -Xmn128m -XX:+PrintGCDetails -Xloggc:log/Benchmark-gc.log -Dwrite.statistics=true -Ddubbo.properties.file="conf/dubbo.properties" -Dbenchmark.properties.file=conf/benchmark.properties -Djava.ext.dirs="conf;lib"  "com.jd.bdp.hydra.benchmark.startBenchTest.StartBenchmark" > "log/Benchmark.log"
cd ..
echo "BenchmarkClient start scheckule is ok,you can check the log file in /log"
PAUSE