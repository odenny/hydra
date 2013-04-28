#!/bin/bash
cd $PWD
java -Xms512m -Xmx512m -Xmn128m -Djava.ext.dirs="../../lib:../../conf" "com.jd.bdp.hydra.benchmark.startABC.StartB" > "../../log/B.log" 2>&1 &