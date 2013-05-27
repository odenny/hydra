cd /D %~dp0/../
SETLOCAL enabledelayedexpansion
for %%f in (%cd%\conf\*.*) do (
	set CLASSPATH=!CLASSPATH!;%%f
)
echo %CLASSPATH%
