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
JSON based on checked out state:

* branch (can be committed to)

    ```json
    {
        "branch": "aaa",
        "commit": "###"
    }
    ```

* tag (cannot be committed to)

    ```json
    {
        "tag": "aaa",
        "commit": "###"
    }
    ```

* commit (cannot be committed to)

    ```json
    {
        "commit": "###"
    }
    ```
