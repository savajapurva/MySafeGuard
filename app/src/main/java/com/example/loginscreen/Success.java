package com.example.loginscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

public class Success extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Button btnSignOut = (Button)findViewById(R.id.btnLogOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountKit.logOut();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                EditText edtUserId, edtPhone, edtEmail;

                edtUserId=(EditText)findViewById(R.id.edtUserId);
                edtUserId.setText(String.format("User Id %s", account.getId()));

                edtEmail=(EditText)findViewById(R.id.edtUserEmail);
                edtEmail.setText(String.format("Email ID %s", account.getEmail()));

                edtPhone=(EditText)findViewById(R.id.edtUserId);
                edtPhone.setText(String.format("Phone %s", account.getPhoneNumber()==null? "":account.getPhoneNumber().toString()));
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }
}
