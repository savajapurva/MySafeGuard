package com.example.loginscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactsFragement extends Fragment {

    public static final String USER_PREF = "USER_PREF" ;
    public static final String KEY_AGE = "KEY_AGE";
    SharedPreferences sp;
    ImageView contactpicker;
    Intent intent;
    Button saveContact;
    TextView name,number;
    CountryCodePicker ccp;
    EditText editTextCarrierNumber;
    private static final int PICK_CONTACT = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ImageView contactpicker  = (ImageView)v.findViewById(R.id.contactpicker);
        sp = this.getActivity().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        ccp = (CountryCodePicker)v.findViewById(R.id.ccp);
        TextView name = (TextView)v.findViewById(R.id.textView2);
        TextView number = (TextView)v.findViewById(R.id.textView);
        Button saveContact = (Button)v.findViewById(R.id.saveContact);
        EditText editTextCarrierNumber =  (EditText)v.findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        contactpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(intent, 7);
                try {
                    startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);
                } catch (Exception e) {
                    //EasyTracker.getTracker().sendException(e.getMessage(), false);
//                    this.toastMessage.setText(R.string.error_pick_contact);
//                    this.toastMessage.show();
                }


            }
        });

        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                intent = new Intent(getActivity(), Dashboard.class);
//                startActivity(intent);

                SharedPreferences.Editor editor = sp.edit();

                Set<String> myStrings = sp.getStringSet("myStrings", new HashSet<String>());
                myStrings.add(ccp.getFullNumber());
                editor.putStringSet("myStrings",myStrings);
                editor.apply();

                System.out.println(sp.getStringSet("myStrings",new HashSet<String>()));

//                Set<String> someStringSet = sp.getStringSet("myStrings",new HashSet<String>());
                //System.out.println(someStringSet);
//                Iterator it  = someStringSet.iterator();
//                while (it.hasNext()) {
//                    System.out.println(it.next());
//                }

                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();


            }
        });


        return v;


    }


}