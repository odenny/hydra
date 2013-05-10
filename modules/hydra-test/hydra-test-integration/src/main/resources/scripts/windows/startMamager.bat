cd /D %~dp0
CALL env.bat
echo "Manager is starting..."
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="conf;lib" -Ddubbo.properties.file="conf/dubbo.properties" "com.jd.bdp.hydra.benchmark.startManager.StartManager" > "log/Manager.log"
echo "Manager start scheckule is ok,you can check the log file in /log"
PAUSE
