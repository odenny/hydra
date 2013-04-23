cd /D %~dp0
echo off
echo "A is starting..."
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../../lib" "com.jd.bdp.hydra.benchmark.startABC.StartA"  > "../../log/A.log"
echo "A is ok~"

