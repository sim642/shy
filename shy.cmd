@ECHO OFF
SETLOCAL
SET "DIR=%~dp0"
java -jar "%DIR%\app\target\app-3.1.jar" %*
