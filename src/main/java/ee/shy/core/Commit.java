package ee.shy.core;


import ee.shy.storage.Hash;

public class Commit {
    private Hash tree;
    private String[] parents;
    private Author author;
    private String time;
    private String message;
}
