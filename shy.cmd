@ECHO OFF
SETLOCAL
SET "DIR=%~dp0"
java -jar "%DIR%\target\shy-2.0-SNAPSHOT.jar" %*
