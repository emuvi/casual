@echo off
call javac TestListOrArray.java
call java TestListOrArray
pause
del TestListOrArray.class
echo "Finished"