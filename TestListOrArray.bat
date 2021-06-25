@echo off
call javac TestListOrArray.java
call java TestListOrArray
del TestListOrArray.class
echo "Finished"