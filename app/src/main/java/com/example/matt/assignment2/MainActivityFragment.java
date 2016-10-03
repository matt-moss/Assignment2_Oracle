package com.example.matt.assignment2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Spinner sort;
    private ListView note;
    private ArrayAdapter<Note> adapter;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        //declaring UI components
        note = (ListView) root.findViewById(R.id.Note_listView);
        sort = (Spinner) root.findViewById(R.id.Sort_spinner);

        String[] sortOptions = new String[] {"Title", "Creation Date", "Category", "Reminder"}; //string array to hold sort options

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.sort_spinner_item, R.id.sort_textView, sortOptions);

        sort.setAdapter(spinnerAdapter); //array adapter to populate the sort spinner

        adapter = new NoteDataAdapter(this.getContext()); //adapter to populate the list view. extends array adapter
        final NoteDatabaseHandler dbh = new NoteDatabaseHandler(getContext()); //creates a new database handler
        final List<Note> data; //creates a new list which will contain the notes

        //throws a database exception if can't pull data from the database
        try {
            data = dbh.getNoteTable().readAll(); //reads all data from the database storing it in the list
            adapter.addAll(data); //adds all data to the adapter

            sort.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() { //when a sort option is selected
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = adapterView.getItemAtPosition(i).toString(); //gets the string value of the chosen sort option

                    if (item.equals("Title")) {
                        Collections.sort(data, new Comparator<Note>() {
                            @Override
                            public int compare(Note n1, Note n2) {
                                return n1.getTitle().compareTo(n2.getTitle()); //compares notes by title, sorting them alphabetically
                            }
                        });
                    } else if (item.equals("Creation Date")) { //compares notes by creation date, sorting them by past to present
                        Collections.sort(data, new Comparator<Note>() {
                            @Override
                            public int compare(Note n1, Note n2) {
                                return n1.getCreated().compareTo(n2.getCreated());
                            }
                        });

                    } else if (item.equals("Category")) { //compares notes by category, sorting them in any order
                        Collections.sort(data, new Comparator<Note>() {
                            @Override
                            public int compare(Note n1, Note n2) {
                                return n1.getCategory() - n2.getCategory();
                            }
                        });
                    } else if (item.equals("Reminder")) { //compares notes by reminder, notes without reminders are at the bottom of the list
                        Collections.sort(data, new Comparator<Note>() {
                            @Override
                            public int compare(Note n1, Note n2) {
                                if (n1.getReminder() == null && n2.getReminder() != null) {
                                    return 1;
                                } else if (n1.getReminder() != null & n2.getReminder() == null) {
                                    return 1;
                                } else if (n1.getReminder() != null && n2.getReminder() != null) {
                                    return n1.getReminder().compareTo(n2.getReminder());
                                } else if (n1.getReminder() == null && n2.getReminder() == null) {
                                    return 0;
                                }
                                return 0;
                            }
                        });
                    }
                    adapter.clear(); //clears the adapter after a sort
                    adapter.addAll(data); //re-adds the sorted data back to the adapter
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }

        note.setAdapter(adapter);

        note.setOnItemClickListener(new AdapterView.OnItemClickListener() { //toasts the current note when clicked
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getContext(), adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }


    private class NoteDataAdapter extends ArrayAdapter<Note> {

        public NoteDataAdapter(Context context) {  //constructor
            super(context, -1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View root;
            if (convertView != null)
                root = convertView;
            else {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                root = inflater.inflate(R.layout.list_item_note, parent, false);
            }

            final Note eachNote = getItem(position); //creates a note in the adapter

            //declaring ListView UI components
            TextView title = (TextView) root.findViewById(R.id.Title_textView);
            TextView body = (TextView) root.findViewById(R.id.Body_textView);
            ImageView category = (ImageView) root.findViewById(R.id.Category_imageView);
            final ImageView remind = (ImageView) root.findViewById(R.id.Reminder_imageView);

            //setting ListView UI components with note data
            title.setText(eachNote.getTitle());
            body.setText(eachNote.getBody());
            category.setBackgroundColor(eachNote.getCategory());

            //sets correct image based upon the particular note having a reminder or not
            if (eachNote.getReminder() != null) {
                remind.setImageDrawable(getResources().getDrawable(R.drawable.remind_on));
            }
            else {
                remind.setImageDrawable(getResources().getDrawable(R.drawable.remind_off));
            }

            ImageView.OnClickListener turnOffReminder = new ImageView.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (eachNote.isHasReminder()) { //does the note have a reminder?
                        remind.setImageDrawable(getResources().getDrawable(R.drawable.remind_off)); //change the image to 'off'
                        eachNote.setHasReminder(false); //change reminder boolean to false
                        eachNote.setReminder(null); //set the reminder date to null
                    }
                }
            };

            remind.setOnClickListener(turnOffReminder); //calls the OnClickListener event

            return root;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }
}
