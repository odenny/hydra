cd /D %~dp0
CALL env.bat
echo "BenchmarkClient is starting..."
echo off
java -Xms512m -Xmx512m -Xmn128m -XX:+PrintGCDetails  -Xloggc:Trigger-gc.log -Dwrite.statistics=true -Djava.ext.dirs="conf;lib"  -Ddubbo.properties.file="conf/dubbo.properties" "com.jd.bdp.hydra.benchmark.startTrigger.StartTrigger" > "log/Trigger.log"
PAUSE