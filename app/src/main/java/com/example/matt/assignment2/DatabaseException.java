package com.example.matt.assignment2;

/**
 * Exception class for database errors.
*/
public class DatabaseException extends Throwable {
    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(Exception e) {
        super(e);
    }
}
