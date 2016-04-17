# shy

[![Travis](https://img.shields.io/travis/sim642/shy.svg)](https://travis-ci.org/sim642/shy)

Version Control System made as UT OOP course project.

#### Contributors
* Tiit Oja
* Simmo Saan
* Gromet Spaal

## Usage
### Setup
#### Linux
1. `mvn package`  
   Builds and packages shy into a single `.jar` file with all of its dependencies.
2. `sudo ./shy-setup.sh`  
   Creates a symlink into `/usr/local/bin/` to make `shy` command globally accessible.
   Creates a symlink into `/etc/bash_completion.d/` to provide bash tab-completion for `shy`.

#### Windows
1. `mvn package`  
   Builds and packages shy into a single `.jar` file with all of its dependencies.
2. `shy-setup` in "Command Prompt (Run as Administrator)"  
   Adds the directory to `PATH` to make `shy` command globally accessible.
