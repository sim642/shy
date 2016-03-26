#!/usr/bin/env bash
_shy ()
{
    local cur
    cur=${COMP_WORDS[COMP_CWORD]}

    COMPREPLY=($(compgen -W '$(shy completion "${COMP_WORDS[@]:1}")' -- $cur))

    return 0
}

complete -F _shy shy