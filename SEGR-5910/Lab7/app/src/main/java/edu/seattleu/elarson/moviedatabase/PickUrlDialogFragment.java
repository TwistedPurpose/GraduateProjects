package edu.seattleu.elarson.moviedatabase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 *
 */
public class PickUrlDialogFragment extends DialogFragment {

    /**
     * List of sample urls
     */
    private final CharSequence[] urlList = {
            "http://www.starwars.com", "http://www.disney.com"
    };

    public PickUrlDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Setup and create dialog box for picking urls of canned list of top
     * viewed movie websites
     *
     * @param savedInstanceState saved instance of the page
     * @return created dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // Create a dialog builder using the current activity to be used to generate
        // a popup
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title bar of the popup
        builder.setTitle(R.string.dialog_url_title);

        // Generate a list of urls to be picked from, if one is selected
        // send it back to the movie list
        builder.setItems(urlList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(), urlList[item] + "", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();

                // If in tablet view, use a different id for finding the fragment
                if (fm.findFragmentById(R.id.detailFragmentContainer) != null) {
                    MovieDetailFragment movieDetailFragment = (MovieDetailFragment)
                            fm.findFragmentById(R.id.detailFragmentContainer);
                    movieDetailFragment.selectUrl(urlList[item] + "");
                } else {
                    // in the the phone, get the list with a different fragment id
                    MovieDetailFragment movieDetailFragment = (MovieDetailFragment)
                            fm.findFragmentById(R.id.fragmentContainer);
                    movieDetailFragment.selectUrl(urlList[item] + "");

                }

            }
        });

        //When cancel pressed, just do the default behavior of closing
        //the dialog
        builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Return the created dialog
        return builder.create();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PickUrlDialogFragment.
     */
    public static PickUrlDialogFragment newInstance() {
        return new PickUrlDialogFragment();
    }

    /**
     * Interface for communicating with Movie Details fragment
     */
    public interface OnPickUrlDialogListener {
        void selectUrl(String url);
    }

}
