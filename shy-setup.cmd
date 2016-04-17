@ECHO OFF
REM %~dp0 gives the script location but with a twist!. It has the directory separator \ as last character.
REM A sane person would not consider it to be a problem but Windows is not sane. As wasted time suggests arguments
REM "%PATH%" /M produce exciting results because cmd.exe is so intelligent that it simply replaces the %PATH% part with
REM the string ending in \. This however causes that very same \ to escape the " following it and making it part of the
REM argument, along with whatever follows (in this case /M). The workaround is to remove the directory separator blindly
REM by cutting off the last character (because we can totally be sure it is the \ and never-ever anything else!).

REM A sane person would now start to think (yes, think) about it:
REM 1. Why does Windows use the very same character for both directory separators and escaping?
REM 2. Why does cmd.exe variable expansion in quoted context not get properly escaped itself to avoid such side-effect?
REM Hereby we provide answers for the lesser mentally developed:
REM 1. The decision was taken by the same people to whom these answers are aimed for. Not only does it produce this
REM    issue but it also makes for extremely ugly paths in quoted strings, e.g. "C:\\Windows\\system32\\".
REM 2. Once you've gotten this far, you already are informed about your mental state, which hopefully answers the
REM    question. In the likely case of it not having done so, do some research about SQL injections. At this point you
REM    should realize that SQL injections and this problem are exactly the same issue: string values being able to
REM    escape outside of the context they're meant to be, causing havoc all around. Constructing a PATH variable which
REM    deletes content of C:\Windows\ via cmd.exe injection is left as an exercise for the reader.

REM temporarily: http://stackoverflow.com/a/3827582/854540
SET "PATH=%PATH%;%~dp0"
SET "PATH=%PATH:~0,-1%"

REM persistently: http://serverfault.com/a/88370
SETX PATH "%PATH%" /M
