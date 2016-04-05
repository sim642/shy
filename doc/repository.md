## .shy/
* `.shy/`
    * [`storage/`](#storage)
    * [`branches/`](#branches)
    * [`tags/`](#tags)
    * [`commit/`](#commit)
    * [`author`](#author)
    * [`current`](#current)
        
### storage/
See [storage.md](storage.md).

### branches/
* `branches/`
    * `master`
    * `develop`
    * ...

JSON:
```json
{
    "commit": "###"
}
```

### tags/
* `tags/`
    * `v1.0`
    * `v2.0`
    * ...

JSON:
```json
{
    "commit": "###",
    "message": "aaa"
}
```

### commit/
Copy of current commit directory.

### author
JSON:
```json
{
    "name": "aaa",
    "email": "aaa"
}
```

### current
Plaintext: hash to currently checked out commit.
