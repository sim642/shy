package ee.shy.core;

import ee.shy.io.Jsonable;

/**
 * Class representing a commit author.
 */
public class Author implements Jsonable {
    /**
     * Author's full name.
     */
    private final String name;

    /**
     * Author's email address.
     */
    private final String email;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (name != null)
            sb.append(name);

        if (name != null && email != null)
            sb.append(" ");

        if (email != null)
            sb.append("<").append(email).append(">");

        return sb.toString();
    }
}
