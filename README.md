# Main.SkirtL
A light-weight interpreter for a C-like scripting language, implemented in
Java.

For compiling part:

The code will be compiled to customized assembly, and run under a
virtual machine.

## To compile the program
### Linux Environment
`$ javac -d bin -sourcepath src src/*.java`

### Windows Environment
`>> javac.exe -d bin -sourcepath src src/*.java`

## To run the program
````
$ cd bin
$ java Main.SkirtL <input file>
````
