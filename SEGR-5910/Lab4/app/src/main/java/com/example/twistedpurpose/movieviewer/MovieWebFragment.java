package com.example.twistedpurpose.movieviewer;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A class for enabling viewing movie web information
 */
public class MovieWebFragment extends Fragment {

    // Non-user facing string for key to fetch url string from intent
    public static final String URL_KEY = "url";

    // URL of the movie to be viewed in web view
    private String mUrl;

    public MovieWebFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of MovieWebFragment and stores url
     *
     * @param url Url sent by intent to view a movie url
     * @return A new instance of fragment MovieWebFragment.
     */

    public static MovieWebFragment newInstance(String url) {
        MovieWebFragment fragment = new MovieWebFragment();
        Bundle args = new Bundle();

        args.putString(URL_KEY, url);
        fragment.setArguments(args);

        return fragment;
    }

    // Fetches url on create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUrl = getArguments().getString(URL_KEY);
        }

    }

    // Creates the web view using android browser for url of a movie
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_web, container,
                false);

        WebView webView = (WebView) v.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(mUrl);

        return v;
    }

}
