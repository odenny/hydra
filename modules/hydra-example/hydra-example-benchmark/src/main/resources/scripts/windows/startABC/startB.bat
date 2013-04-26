cd /D %~dp0
echo off
echo "B is starting..."
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../../lib;../../conf" "com.jd.bdp.hydra.benchmark.startABC.StartB"  > "../../log/B.log"
echo "B is ok~"