package com.example.loginscreen;

import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
  fun getRepository() = (application as ReminderApp).getRepository()
}