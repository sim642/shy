package ee.shy.core;

import ee.shy.io.Jsonable;

public class Author extends Jsonable {
    private final String name;
    private final String email;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
