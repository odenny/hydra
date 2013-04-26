cd /D %~dp0
echo "A is starting..."
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../../lib;../../conf" "com.jd.bdp.hydra.benchmark.startABC.StartA"  > "../../log/A.log"
echo "A is ok~"

