#!/usr/bin/env bash
_shy ()
{
    local cur
    cur=${COMP_WORDS[COMP_CWORD]}

    IFS=$'\n'
    words=($(shy completion "${COMP_WORDS[@]:1}"))
    COMPREPLY=($(compgen -W '"${words[@]}"' -- $cur))

    return 0
}

complete -F _shy shy