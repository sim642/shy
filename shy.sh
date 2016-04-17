#!/bin/sh
# find script directory over symlinks - http://stackoverflow.com/a/1482133/854540
DIR="$(dirname "$(readlink -f "$0")")"
java -jar "$DIR/target/shy-2.0.jar" "$@"
