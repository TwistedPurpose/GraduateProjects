package edu.seattleu.elarson.moviedatabase;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieWebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieWebFragment extends Fragment {

    public static final String EXTRA_URL = "edu.seattleu.elarson.moviedatabase.url";
    private String mUrl;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url URL.
     * @return A new instance of fragment MovieWebFragment.
     */
    public static MovieWebFragment newInstance(String url) {
        MovieWebFragment fragment = new MovieWebFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieWebFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(EXTRA_URL);
        }
    }

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
