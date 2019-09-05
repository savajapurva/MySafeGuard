package com.example.loginscreen;

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.loginscreen.sendNotification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionsIntentService : IntentService("GeoTrIntentService") {

  companion object {
    private const val LOG_TAG = "GeoTrIntentService"
  }

  override fun onHandleIntent(intent: Intent?) {
    // 1
    val geofencingEvent = GeofencingEvent.fromIntent(intent)
    // 2
    if (geofencingEvent.hasError()) {
      val errorMessage = GeofenceErrorMessages.getErrorString(this,
              geofencingEvent.errorCode)
      Log.e(LOG_TAG, errorMessage)
      return
    }
    // 3
    handleEvent(geofencingEvent)
  }

  private fun handleEvent(event: GeofencingEvent) {
    // 1
    if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
      // 2
      val reminder = getFirstReminder(event.triggeringGeofences)
      val message = reminder?.message
      val latLng = reminder?.latLng
      if (message != null && latLng != null) {
        // 3
        sendNotification(this, message, latLng)
      }
    }
  }

  private fun getFirstReminder(triggeringGeofences: List<Geofence>): Reminder? {
    val firstGeofence = triggeringGeofences[0]
    return (application as ReminderApp).getRepository().get(firstGeofence.requestId)
  }
}