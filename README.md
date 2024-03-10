# Pit

Pit is a personal implementation of the most common commands from git.

## Installation

You need to install jdk to you file system and add it to path. 
After that you build jar artifact from the project.

```bash
cd src
javac Main.java
jar cf pit.jar Main.class
```

After this you copy the jar file in the directory you want versioning.

## Usage

From the directory where you have the jar you can run the following commands in cmd.  

- This will prepare the directory for versioning and will create a directory *.pit*
```bash
java -jar pit.jar init
```
- You can add files or directories with the following commands:

```bash
java -jar pit.jar add .
```
or
```bash
java -jar pit.jar add example.txt
```
or
```bash
java -jar pit.jar add DirectoryExample
```

or
```bash
java -jar pit.jar add DirectoryExample\SubDirectory
```

- You can always check the status of which files will be added or are not tracked with:

```bash
java -jar pit.jar status
```
- You can commit and save the tracked files with :
```bash
java -jar pit.jar commit
```


## How it works

When *init* commands is run, I create two directories **.pit** and **.pit/save**.  

---

When *add* command is run, I create a directory **.pit/temp** and a txt file **.pit/temp/directoryMap.txt** which has all the relative paths of the files tracked.  

---

When *commit* command is run, I create a directory in **.pit/save/** with the name being the order of the commit. Let's say that when we first run the commit command we create the directory **.pit/save/00**. After this it will be **.pit/save/01** and so on. In these directories we save all the files that were previously added with  the add command and a directory **.info** which has all the files with their respective last modified date.  

---

When *status* command is run, I keep the relative paths of all the files in **temp/MapStatus.txt**. After this, I look if an add command was already run or if a commit command was run and I check all the files last modified date and compare them with the information from the last **.info**, or if they are not present in the add command.