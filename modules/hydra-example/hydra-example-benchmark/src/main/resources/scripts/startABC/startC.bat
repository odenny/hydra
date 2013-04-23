cd /D %~dp0
echo off
echo "C is starting..."
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../../lib" "com.jd.bdp.hydra.benchmark.startABC.StartC"  > "../../log/C.log"
echo "C is ok~"