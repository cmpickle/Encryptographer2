package com.cmpickle.encryptographer;

import android.graphics.Bitmap;

/**
 * @author Cameron Pickle
 *         Copyright (C) Cameron Pickle (cmpickle) on 2/17/2017.
 */

public class Sms {
    private Bitmap photo;
    private String name;
    private String body;
    private String date;

    public Sms(Bitmap photo, String name, String body, String date) {
        super();
        this.photo=photo;
        this.name=name;
        this.body=body;
        this.date=date;
    }

    public Bitmap getImage() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }
}
