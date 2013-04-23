cd /D %~dp0

echo "ABC is starting..."
cd /D startABC/
rem Current dir is：%cd%
echo off
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../lib" "com.jd.bdp.hydra.benchmark.startManager.startManager" > "../log/Manager.log"
cd ..
echo "ABC start scheckule is ok,you can check the log file in /log"
pause