# shy

[![Travis](https://img.shields.io/travis/sim642/shy/develop.svg)](https://travis-ci.org/sim642/shy)
[![Codecov](https://img.shields.io/codecov/c/github/sim642/shy/develop.svg)](https://codecov.io/github/sim642/shy)

Version Control System made as UT OOP course project.

#### Contributors
* Tiit Oja
* Simmo Saan
* Gromet Spaal

## Usage
### Downloading
* `git clone --recursive https://github.com/sim642/shy.git`  
  Clones git repository with its submodules.

### Building
* `mvn package`  
  Builds and packages shy into a single `.jar` file with all of its dependencies.

### Setup
#### Linux
* `sudo ./shy-setup.sh`  
  Creates a symlink into `/usr/local/bin/` to make `shy` command globally accessible.
  Creates a symlink into `/etc/bash_completion.d/` to provide bash tab-completion for `shy`.

#### Windows
* `shy-setup` in "Command Prompt (Run as Administrator)"  
  Adds the directory to `PATH` to make `shy` command globally accessible.

### Getting help
shy has extensive help available directly from the command line. Simply run `shy help` followed by the subcommand name
for which the help should be shown, e.g. `shy help add`. If a command has subcommands, they will be listed in the help
and their help can also be viewed, e.g. `shy help author name`.

### Author information setup
`shy gauthor` command can be used to set author's details globally which will be stored in a config file under home directory.
`shy author` sets repository's author details for a specific repository.

### Initialization
#### New repository
Run `shy init` in any directory to initialize a shy repository in that directory.

#### Existing repository
Run `shy clone` with a supported URI argument to clone a shy repository.
Currently only SSH URIs are supported, e.g. `ssh://shy@example.com/home/shy/test`.


All of the following commands can be run in that directory or any of its subdirectories.

It is highly recommended to set up your information (name, email) with `shy author` as that will be shown as the author
of the commits made.

### Committing
Use `shy add` to add files and directories from working directory to the commit currently being created.
Use `shy remove` to remove files and directories from the commit currently being created.
Once everything necessary is added, run `shy commit` with a suitable commit message to create the commit.

### Branching
Run `shy branch add` to create a new branch at the latest commit. All branches can be listed with `shy branch list` and
excess ones deleted with `shy branch remove`.

### Tagging
Run `shy tag add` to create a new tag at the latest commit with given message. All tags can be listed with `shy tag list` and
excess ones deleted with `shy tag remove`.

### Viewing history
Use `shy log` to see the commit history for any branch or commit. Optionally it's possible to give a second argument
to filter out only some commits.

### Viewing differences
It's possible to display the differences of any two files in the working directory using `shy diff`.
The same command can also be used to compare any two branches and commits entirely.

### Checking out
Any branch and commit can be checked out using `shy checkout`.
**NB! Any uncommited files may be overwritten.**

### Merging
Use `shy merge` with a branch name as argument to merge that branch into the currently checked out one.
Conflicting changes will be renamed to respectively `.OLD` and `.REV` files to indicate the old file from
current branch and the file from the merged branch.
Merged changes will be automatically added but not commited to allow for pre-commit testing and changes, thus
`shy commit` has to be executed separately.
**NB! Merging may completely fail and make unexpected changes.**

### Pushing and pulling
Use `shy pull` to get updates from a remote repository and `shy push` to send your updates to a remote repository.
Remote repositories (remotes) can be managed via `shy remote` commands.
Unless specified the default remote "origin", which is set up automatically when cloning, is used for pushin and pulling.
**NB! Any uncommited files may be overwritten.**

### Searching content
The `shy search` command can be used to recursively search content in a specific commit.

### Ignoring files
A `.shyignore` file can be created to ignore files which match the glob patterns specified in the ignore file.