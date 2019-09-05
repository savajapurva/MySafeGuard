package com.example.loginscreen;

import android.app.Application
import com.example.loginscreen.ReminderRepository

class ReminderApp : FadeGlobal() {

  private lateinit var repository: ReminderRepository

  override fun onCreate() {
    super.onCreate()
    repository = ReminderRepository(this)
  }

  fun getRepository() = repository
}