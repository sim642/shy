## Special formats

### tree
Format representing a directory. Can refer to other directories to produce a tree.

JSON:
```json
{
    "items": {
        "myfile": {
            "type": "file",
            "hash": "###"
        },
        "mydir": {
            "type": "tree",
            "hash": "###"
        },
        ...
    }
}
```
        
### commit
Format representing a commit.

JSON:
```json
{
    "tree": "###",
    "parents": [
        "###",
        ...
    ],
    
    "author": {
        "name": "aaa",
        "email": "aaa"
    },
    "time": "2016-03-16T05:07:19+02:00",
    "message": "aaa"
}
```
