package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.Hash;

public class Branch extends Jsonable {
    private final Hash hash;

    public Branch(Hash hash) {
        this.hash = hash;
    }
}