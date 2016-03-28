#!/usr/bin/env bash
_shy ()
{
    local cur
    cur=${COMP_WORDS[COMP_CWORD]}

    IFS=$'\n'
    words=($(shy completion "${COMP_WORDS[@]:1}"))
    if [ ${#words[@]} -eq 0 ]; then
        COMPREPLY=($(compgen -f ${cur}))
    else
        COMPREPLY=($(compgen -W '"${words[@]}"' -- ${cur}))
    fi

    return 0
}

complete -F _shy shy