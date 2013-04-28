cd /D %~dp0

echo "Manager is starting..."
echo off
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../conf;../lib"  "com.jd.bdp.hydra.benchmark.startManager.StartManager"
cd ..
echo "Manager start scheckule is ok,you can check the log file in /log"
pause
