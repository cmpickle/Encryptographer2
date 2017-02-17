package com.cmpickle.encryptographer;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, "address IS NOT NULL) GROUP BY (address", null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int timeMillis = smsInboxCursor.getColumnIndex("date");

        Calendar cal = Calendar.getInstance();
        cal.set(1969, 12, 31, 17, 0);
        long baseTime = cal.getTimeInMillis();

        if(indexBody < 0 || !smsInboxCursor.moveToFirst())
            return;

//        contactAdapter.clear();
//        phoneNum.clear();

        do {
            String dateString = smsInboxCursor.getString(timeMillis);
            long dateTime = Long.valueOf(dateString);
            long finalTime = dateTime + baseTime;
            Date date = new Date(finalTime);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd h:mm aa");
            String dateText = format.format(date);
            String str = ContactLookup.getContactDisplayNameByNumber(smsInboxCursor.getString(indexAddress), getActivity()) + "\n"
                    + smsInboxCursor.getString(indexBody) + "\n" + dateText + "\n";
//            phoneNum.add(smsInboxCursor.getString(indexAddress));
            smses.add(new Sms(Contact.openPhoto(Contact.getContactIDFromNumber(smsInboxCursor.getString(indexAddress), getActivity()), getActivity()), ContactLookup.getContactDisplayNameByNumber(smsInboxCursor.getString(indexAddress), getActivity()), smsInboxCursor.getString(indexBody), dateText));
        } while(smsInboxCursor.moveToNext());

        smsInboxCursor.close();
    }

}
