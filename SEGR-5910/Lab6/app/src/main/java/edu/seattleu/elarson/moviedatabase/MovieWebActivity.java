package edu.seattleu.elarson.moviedatabase;

import android.app.Fragment;
import android.content.Intent;


public class MovieWebActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(MovieWebFragment.EXTRA_URL);
        return MovieWebFragment.newInstance(url);
    }
}
