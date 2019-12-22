# YCompiler
A light-weight interpreter/interpreter for C-like scripting language, base on Java. 
Unit testing involved.

The code will be compiled to customized MIPS like assembly, and run under a
virtual machine.

## To compile the program
### Linux Environment
`$ javac -d bin -sourcepath src src/*.java`

### Windows Environment
`>> javac.exe -d bin -sourcepath src src/*.java`

## To run the program
````
$ cd bin
$ java Main <input file>
````
