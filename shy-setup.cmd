@ECHO OFF
REM temporarily: http://stackoverflow.com/a/3827582/854540
SET "PATH=%PATH%;%~dp0"
SET "PATH=%PATH:~0,-1%"

REM persistently: http://serverfault.com/a/88370
SETX PATH "%PATH%" /M
