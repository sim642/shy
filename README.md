# shy
Version Control System made as UT OOP course project.

#### Contributors
* Tiit Oja
* Simmo Saan
* Gromet Spaal

## Usage
### Setup
1. `mvn package`  
   Builds and packages shy into a single `.jar` file with all of its dependencies.
2. `sudo ./shy-setup.sh`  
   Creates a symlink into `/usr/local/bin/` to make `shy` command globally accessible.
   Creates a symlink into `/etc/bash_completion.d/` to provide bash tab-completion for `shy`.
