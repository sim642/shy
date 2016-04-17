@ECHO OFF
SETLOCAL
SET "DIR=%~dp0"
java -jar "%DIR%\target\shy-3.0-SNAPSHOT.jar" %*
