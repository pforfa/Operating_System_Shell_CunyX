Design
The source code contains two separate Java files. The main file is called CunyX,
which should be called whenever the program is to be initiated.

The overall design of the project is fairly simple. The main class, CunyX is the
driver of the program. When the program is initiated without any arguments, then
it goes into interactive mode, otherwise it reads from a specified batch file. If there
is more than one argument passed in at execution time, then the program catches this error.
In interactive mode, the helper class "InputParser" is instantiated and used to partition the
user's input, so that it is in an acceptable format as a parameter for ProcessBuilder.

Complete Specification
One thing to note is that if the user specifies the following input:
"ls -hal;  ;;;;quit;pwd" then the part of the string with ';   ;' will
not even be passed into ProcessBuilder. Instead, a warning message will be printed
that a blank command was detected. The rest of the commands such as quit and pwd will be processed.

In interactive mode, the user can type in any number of system commands they please. System commands
will vary based on windows/unix environments. An example of an interactive command on a unix system such as
Ubuntu/Mint is: ls -al; pwd;
Note that each command is seperated by a semicolon (';').
If the user types in a command such as ls -al; pwd; quit; who;
then the only commands that will actually run are the first two. 
Whenever the program catches the string 'quit', then it stops all execution 
of commands after the quit command. The previous commands will finish their execution however.

In batch mode, the same principle follows, however all commands are read from a specified input file.
Batch mode, with an input file called 'commands.txt' for example, can be executed by the following command: 
java CunyX commands.txt
**Note that CunyX must be a .class file, not the given .java file. To successfuly compile, simply run 'javac CunyX.java' (without '')

Bugs
Currently there are no known bugs in this version of the program. All instances of tested errors
are caught during execution and the program continues execution without crashing. One feature that
was not implemented is the 'cd' command, however since that in itself it not a program, but a feature of a certain shell, therefore it has not been implemented in this version of the CunyX shell yet. Another important feature is also missing, and this is concurrent program output. Even though the processes run concurrently, the output is not intermixed; the output is sequential.
