#!/bin/sh
# readlink: http://superuser.com/a/424826/73371
ln -i -s "$(readlink -f .)/shy.sh" /usr/local/bin/shy
# bash completion
ln -i -s "$(readlink -f .)/shy-completion.sh" /etc/bash_completion.d/shy