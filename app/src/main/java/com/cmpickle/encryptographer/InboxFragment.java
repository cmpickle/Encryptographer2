package com.cmpickle.encryptographer;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.Manifest;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private InboxAdapter inboxAdapter;
    private ArrayList<Sms> smses = new ArrayList<>();

    final int MY_PERMISSIONS_REQUEST_READ_SMS =  12345;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12346;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshSmsInbox();
        inboxAdapter = new InboxAdapter(smses);
        recyclerView.setAdapter(inboxAdapter);
        return view;
    }

    public void refreshSmsInbox() {



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                }
        }
    }

    public class LookupConversations extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {int smsPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
            int contactPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
            if(smsPermission != PackageManager.PERMISSION_GRANTED || contactPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_SMS);
            } else {
                Log.d("test", "you do have permission");
                long startTime = Calendar.getInstance().getTimeInMillis();
                ContentResolver contentResolver = getActivity().getContentResolver();
                Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, "address IS NOT NULL) GROUP BY (address", null, null);
                int indexBody = smsInboxCursor.getColumnIndex("body");
                int indexAddress = smsInboxCursor.getColumnIndex("address");
                int timeMillis = smsInboxCursor.getColumnIndex("date");

                Calendar cal = Calendar.getInstance();
                cal.set(1969, 12, 31, 17, 0);
                long baseTime = cal.getTimeInMillis();

                long endTime = Calendar.getInstance().getTimeInMillis();
                Log.d("test", "query time: " + (endTime - startTime));

                if(indexBody < 0 || !smsInboxCursor.moveToFirst())
                    return;

//        contactAdapter.clear();
//        phoneNum.clear();


                do {
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd h:mm aa");
                    String dateText = format.format(new Date(smsInboxCursor.getLong(timeMillis) + baseTime));
                    startTime = Calendar.getInstance().getTimeInMillis();
                    String address = smsInboxCursor.getString(indexAddress);
                    Activity activity = getActivity();
                    smses.add(new Sms(Contact.openPhoto(Contact.getContactIDFromNumber(address, activity), activity), ContactLookup.getContactDisplayNameByNumber(address, activity), smsInboxCursor.getString(indexBody), dateText));
                    endTime = Calendar.getInstance().getTimeInMillis();
                } while(smsInboxCursor.moveToNext());
                Log.d("test", "populate data time: " + (endTime - startTime));

                smsInboxCursor.close();
            }
            return null;
        }
    }
}
