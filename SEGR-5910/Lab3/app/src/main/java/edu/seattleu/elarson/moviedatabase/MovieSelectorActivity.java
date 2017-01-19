package edu.seattleu.elarson.moviedatabase;


/**
 * By Soren Ludwig
 * Main controller for the app
 */
public class MovieSelectorActivity extends SingleFragmentActivity {

    @Override
    protected MovieSelectorFragment createFragment() {
        return MovieSelectorFragment.newInstance();
    }
}
