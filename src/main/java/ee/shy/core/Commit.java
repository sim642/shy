package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.Hash;
import java.time.OffsetDateTime;

public class Commit extends Jsonable {
    private Hash tree;
    private Hash[] parents;
    private Author author;
    private OffsetDateTime time;
    private String message;
}
