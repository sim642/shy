#!/bin/bash

shy init
shy author name "git2shy importer"

IFS=$'\n'
for line in $(git rev-list --first-parent --reverse --pretty=oneline HEAD); do
	sha1=$(echo $line | cut -d " " -f 1)
	msg=$(echo $line | cut -d " " -f 2-)
	
	git checkout $sha1
	shy add .
	shy commit "$msg (import $sha1 from git)"
done
