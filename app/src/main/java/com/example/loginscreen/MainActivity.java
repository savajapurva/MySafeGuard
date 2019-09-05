package com.example.loginscreen;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    public final static int REQUEST_CODE=999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {

            startActivity(new Intent(MainActivity.this, Walkthrough.class));
        }



        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        //printKeyHash();

        RelativeLayout relative1 = (RelativeLayout) findViewById(R.id.r1);
        relative1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startLoginPage(LoginType.EMAIL);
                Log.d("Message", "onClick: Otp received");
            }
        });

        RelativeLayout relative2 = (RelativeLayout) findViewById(R.id.r2);
        relative2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startLoginPage(LoginType.PHONE);
                Log.d("Message", "onClick: Mail sent");
            }
        });

        // check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        com.facebook.accountkit.AccessToken loginToken = com.facebook.accountkit.AccountKit.getCurrentAccessToken();
        if (accessToken != null || loginToken != null) {
            // if previously logged in, proceed to the account activity
            startActivity(new Intent(this, Dashboard.class));
        }
    }


    private void startLoginPage(LoginType loginType) {

        if (loginType == loginType.EMAIL) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.EMAIL, AccountKitActivity.ResponseType.TOKEN);
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        } else if (loginType == loginType.PHONE) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(result.getError()!=null){
                Toast.makeText(this,""+result.getError().getErrorType().getMessage(),Toast.LENGTH_SHORT).show();
                return;
            } else if(result.wasCancelled()){

                return;
            } else {
//                if(result.getAccessToken() != null) {
//                    Toast.makeText(this, "Success ! %s" + result.getAccessToken(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Success ! %s" + result.getAuthorizationCode().substring(0,10), Toast.LENGTH_SHORT).show();
//                }
                startActivity(new Intent(this, Dashboard.class));
            }
        }
    }

    public void onBackPressed(){

    }
    
}
