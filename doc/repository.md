## .shy/
* `.shy/`
    * [`storage/`](#storage)
    * [`branches/`](#branches)
    * [`tags/`](#tags)
    * [`commit/`](#commit)
    * [`author`](#author)
    * [`current`](#current)
        
### storage/
*TODO*

### branches/
* `brances/`
    * `master`
    * `develop`
    * ...

Plaintext: hash to branch commit.

### tags/
* `tags/`
    * `v1.0`
    * `v2.0`
    * ...

Plaintext: hash to tag commit.

### commit/
Copy of current commit directory.

### author
JSON:
```
{
    "name": "...",
    "email": "..."
}
```

### current
Plaintext: hash to currently checked out commit.
