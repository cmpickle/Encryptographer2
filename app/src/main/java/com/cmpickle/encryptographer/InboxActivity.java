package com.cmpickle.encryptographer;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class InboxActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, new InboxFragment(), "inboxFragment")
                .commit();
    }
}
