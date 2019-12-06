package com.dansudz.simpledl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.HashMap;

public class OptionsDialogFragment extends DialogFragment {
    private EditText playlistStart;
    private EditText playlistEnd;
    private EditText playlistItem;
    private EditText outputTemplate;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface OptionsDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private OptionsDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (OptionsDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(MainActivity.class.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.options_dialog, null))
                .setTitle("Options")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(OptionsDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(OptionsDialogFragment.this);

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * Store view components for later use and set initial data
     */
    @Override
    public void onStart() {
        super.onStart();
        playlistStart = getDialog().findViewById(R.id.optPlaylistStart);
        playlistEnd = getDialog().findViewById(R.id.optPlaylistEnd);
        playlistItem = getDialog().findViewById(R.id.optPlaylistItem);
        outputTemplate = getDialog().findViewById(R.id.optOutputTemplate);

        playlistStart.setText(getArguments().getString(getResources().getString(R.string.playlistStart), ""));
        playlistEnd.setText(getArguments().getString(getResources().getString(R.string.playlistEnd), ""));
        playlistItem.setText(getArguments().getString(getResources().getString(R.string.playlistItems), ""));
        outputTemplate.setText(getArguments().getString(getResources().getString(R.string.outputTemplate), "%(title)s.%(ext)s"));
    }

    /**
     *
     * @return HashMap having the non-empty options and its values
     */
    public HashMap<String, Object> getOptions() {
        HashMap<String, Object> options = new HashMap<>();

        if(!playlistStart.getText().toString().equals("")) {
            options.put(getResources().getString(R.string.playlistStart), Integer.parseInt(playlistStart.getText().toString()));
        }

        if(!playlistEnd.getText().toString().equals("")) {
            options.put(getResources().getString(R.string.playlistEnd), Integer.parseInt(playlistEnd.getText().toString()));
        }

        if(!playlistItem.getText().toString().equals("")) {
            options.put(getResources().getString(R.string.playlistItems), playlistItem.getText().toString());
        }

        if(!outputTemplate.getText().toString().equals("")) {
            options.put(getResources().getString(R.string.outputTemplate), outputTemplate.getText().toString());
        }

        return options;
    }

    /**
     *
     * @return String having the options as one would provide them in the command line
     * but without the link
     */
    public String getOptionsAsCliString() {
        StringBuilder args = new StringBuilder();

        if(!playlistStart.getText().toString().equals("")) {
            args.append("--playlist-start");
            args.append(playlistStart.getText().toString());
        }

        if(!playlistEnd.getText().toString().equals("")) {
            args.append("--playlist-end");
            args.append(playlistEnd.getText().toString());
        }

        if(!playlistItem.getText().toString().equals("")) {
            args.append("--playlist-items");
            args.append(playlistItem.getText().toString());
        }

        if(!playlistItem.getText().toString().equals("")) {
            args.append("-o");
            args.append(outputTemplate.getText().toString());
        }

        return args.toString();
    }
}
