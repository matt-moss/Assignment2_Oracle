package com.example.matt.assignment2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by MattyIce on 24/09/2016.
 */
public class NoteTable extends Table<Note> {

    /**
     * ISO 8601 standard date format.
     */
    private static final SimpleDateFormat isoISO8601 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.sss");

    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_DATE_CREATED = "dateCreated";
    private static final String COLUMN_REMINDER = "reminder";
    private static final String COLUMN_CATEGORY = "category";

    /**
     * Create a ContactTable with the DB handler.
     *
     * @param dbh
     */
    public NoteTable(SQLiteOpenHelper dbh) {
        super(dbh, "notes");

        // create table structure
        addColumn(new Column(COLUMN_TITLE, "TEXT").notNull());
        addColumn(new Column(COLUMN_BODY, "TEXT").notNull());
        addColumn(new Column(COLUMN_REMINDER, "DATE"));
        addColumn(new Column(COLUMN_DATE_CREATED, "DATE").notNull());
        addColumn(new Column(COLUMN_CATEGORY, "INTEGER").notNull());
    }

    @Override
    public ContentValues toContentValues(Note element) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, element.getTitle());
        values.put(COLUMN_BODY, element.getBody());
        values.put(COLUMN_CATEGORY, element.getCategory());
        if (element.getReminder() != null) {
            values.put(COLUMN_REMINDER, isoISO8601.format(element.getReminder()));
            element.setHasReminder(true);
        }
        else {
            element.setHasReminder(false);
        }
        values.put(COLUMN_DATE_CREATED, isoISO8601.format(element.getCreated()));

        return values;
    }

    @Override
    public String[] getSelectAll() {
        return new String[] {"_id", COLUMN_TITLE, COLUMN_BODY, COLUMN_CATEGORY, COLUMN_REMINDER, COLUMN_DATE_CREATED};
    }

    @Override
    public Note fromCursor(Cursor cursor) throws DatabaseException {
        Note note = new Note(cursor.getLong(0));

        // get title, body and category
        note.setTitle(cursor.getString(1));
        note.setBody(cursor.getString(2));
        note.setCategory(cursor.getInt(3));

        // if current note has a reminder set true and get the date
        if(!cursor.isNull(4)) {
            try {
                note.setReminder(isoISO8601.parse(cursor.getString(4)));
                note.setHasReminder(true);
            }
            catch (ParseException e) {
                // package a ParseException as a generic DatabaseException
                throw new DatabaseException(e);
            }
        }
        else {
            note.setHasReminder(false); //no reminder
        }

        //
        if(!cursor.isNull(5))
            try {
                note.setCreated(isoISO8601.parse(cursor.getString(5))); //get date created
            } catch (ParseException e) {
                e.printStackTrace();
            }


        return note;
    }

    @Override
    public String getId(Note element) {
        return String.valueOf(element.getId());
    }

    @Override
    public boolean hasInitialData() {
        return true;
    }

    @Override
    public void initialize(SQLiteDatabase database) {
        for(Note note : NoteData.getData())
            database.insertOrThrow(getName(), null, toContentValues(note));
    }

    @Override
    public Long create(Note element) throws DatabaseException {
        long id = super.create(element);
        element.setId(id);
        return id;
    }




}
