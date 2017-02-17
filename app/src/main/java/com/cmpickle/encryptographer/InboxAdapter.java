package com.cmpickle.encryptographer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Cameron Pickle
 *         Copyright (C) Cameron Pickle (cmpickle) on 2/17/2017.
 */

public class InboxAdapter extends android.support.v7.widget.RecyclerView.Adapter<InboxAdapter.ContactHolder> {

    private ArrayList<Sms> smses = new ArrayList<>();

    public InboxAdapter(ArrayList<Sms> smses) {
        this.smses = smses;
    }

    public static class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.receive_contact_image)
        ImageView receiveContactImage;
        @BindView(R.id.receive_name)
        TextView receiveContactName;
        @BindView(R.id.receive_body)
        TextView receiveContactBody;
        private Sms sms;

        public ContactHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void bindContact(Sms sms) {
            this.sms = sms;
            if(sms.getImage() != null)
                this.receiveContactImage.setImageBitmap(sms.getImage());
            this.receiveContactName.setText(sms.getName());
            this.receiveContactBody.setText(sms.getBody());
        }

        @Override
        public void onClick(View view) {
            Log.d("RecyclerView", "Click");
        }
    }

    @Override
    public InboxAdapter.ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_item, parent, false);
        return new ContactHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(InboxAdapter.ContactHolder holder, int position) {
        Sms itemSms = smses.get(position);
        holder.bindContact(itemSms);
    }

    @Override
    public int getItemCount() {
        return smses.size();
    }
}
