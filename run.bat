@echo off
setlocal enabledelayedexpansion

:: Print the OS
echo Running program on windows

cd lib
dir /b *.jar > jars.txt

cd native/windows
dir /b * > jars.txt

:: Go back to the root project directory
cd ..\..\..

:: Combine the lists of libraries into the proper format
set libraries=

:: Add jars from lib folder
for /f "delims=" %%A in (lib\jars.txt) do (
    set libraries=!libraries!lib\%%A;
)

:: Add jars from the native OS folder
for /f "delims=" %%B in (lib\native\windows\jars.txt) do (
    set libraries=!libraries!lib\native\windows\%%B;
)

:: Find all Java files recursively
for /r %%C in (*.java) do (
    set javaFiles=!javaFiles!%%C;
)

:: Compile and run the Java files
javac -cp !libraries!;src;res !javaFiles!
java -Djava.library.path=lib\native\windows\ -cp !libraries!;src;res com.countgandi.com.game.Game

pause