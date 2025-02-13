OS_RUN="linux"
if [[ $OSTYPE == 'linux'* ]]; then
	OS_RUN="linux"
elif [[ $OSTYPE == 'darwin'* ]]; then
	OS_RUN="macos"
fi
echo "Running program on" $OS_RUN

cd lib
ls --file-type *.jar > jars.txt
cd native
cd $OS_RUN
ls --file-type * > jars.txt

cd ..
cd ..
cd ..

libraries=`awk '{printf "lib/"$1":",$0}' lib/jars.txt``awk -v os_run=$OS_RUN '{printf "lib/native/"os_run"/"$1":",$0}' lib/native/$OS_RUN/jars.txt`

javaFiles=`find . -name "*.java" -print`

javac -cp $libraries:src/:res/ $javaFiles
java -Djava.library.path=lib/native/$OS_RUN/ -cp $libraries:src/:res/ com/countgandi/com/game/Game
