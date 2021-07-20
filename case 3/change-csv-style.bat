@echo off
set JARBOX=%USERPROFILE%\.m2\repository\br\com\pointel\JarBox\0.1.0\JarBox-0.1.0.jar
call groovy -cp %JARBOX% change-csv-style.groovy "D:\Temp\CSVMorto"