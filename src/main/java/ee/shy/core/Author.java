package ee.shy.core;

import ee.shy.io.Jsonable;

/**
 * Class representing a commit author.
 */
public class Author extends Jsonable {
    /**
     * Author's full name.
     */
    private String name;

    /**
     * Author's email address.
     */
    private String email;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
