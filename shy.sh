#!/bin/sh
# find script directory over symlinks - http://stackoverflow.com/a/1482133/854540
DIR="$(dirname "$(readlink -f "$0")")"
java -jar "$DIR/app/target/app-3.0.jar" "$@"
