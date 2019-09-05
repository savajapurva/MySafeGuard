package com.example.loginscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedContactsFragement extends Fragment {

    View v;
    public static final String USER_PREF = "USER_PREF" ;
    private RecyclerView rv;
    private List<Contact> lstContact;
    SharedPreferences sp;
    public SavedContactsFragement(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_status, container, false);
        rv = v.findViewById(R.id.my_recycler_view);
        RecyclerViewAdapter reAdapter = new RecyclerViewAdapter(getContext(),lstContact);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(reAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setUserVisibleHint(true);
        super.onCreate(savedInstanceState);
        sp = this.getActivity().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        lstContact = new ArrayList<>();

        Set<String> someStringSet = sp.getStringSet("myStrings",new HashSet<String>());

        String[] str =new String[someStringSet.size()];
        str = someStringSet.toArray(str);
        //        while (it.hasNext()) {
//            lstContact.add(new Contact("Apurva",someStringSet,R.drawable.avatar));
//            System.out.println(it.next());
//        }

        for(int i=0;i<str.length;i++){ //start from 0 will show number from shared preference

            lstContact.add(new Contact("Apurva",str[i],R.drawable.avatar));

        }

        //System.out.println(someStringSet.size());


}

}