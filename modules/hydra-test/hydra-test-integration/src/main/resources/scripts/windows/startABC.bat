cd /D %~dp0

echo "ABC is starting..."
cd /D startABC/
START  /MIN startA.bat
START  /MIN startB.bat
START  /MIN startC.bat
cd ..
echo "ABC start scheckule is ok,you can check the log file in /log"
PAUSE