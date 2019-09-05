package com.example.loginscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendEmailTask extends AsyncTask<String, Void, Boolean> {
    Context context;

    public SendEmailTask(Context c) {
        this.context = c;
    }

    protected Boolean doInBackground(String... params) {
        try {
            return new Mail(params[1], params[2], this.context).send();
        } catch (Exception e) {
            Log.w("sendEmailTask.java", "Exceptiopn: " + e.toString());
            return Boolean.FALSE;
        }
    }

    @SuppressLint("WrongConstant")
    protected void onPostExecute(Boolean result) {
        if (!result) {
            Toast.makeText(this.context, this.context.getString(R.string.error_email_not_sent), 1).show();
        }
    }
}
