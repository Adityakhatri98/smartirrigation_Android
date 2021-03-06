package com.example.smartcrop;

import android.util.Log;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

public class SendEmail {

    //keval key
    // public static final String key = "your key";

    public static final String key = "your key";
    public static void send(String otp, String useremail) {
        //Alternate way of instantiating
        //SendGrid sendGrid = new SendGrid(SENDGRID_USERNAME,SENDGRID_PASSWORD);

        //Instantiate the object using your API key String
        SendGrid sendgrid = new SendGrid(key);

        SendGrid.Email email = new SendGrid.Email();
        email.addTo(useremail);
        email.setFrom("Smartirrigationproject7@gmail.com");
        email.setSubject("Verification from SmartCrop");
        email.setText("Your verification code for smartCrop is : "+otp);

        try {
            SendGrid.Response response = sendgrid.send(email);
        }
        catch (SendGridException e) {
            Log.e("sendError", "Error sending email");
        }
    }
}


