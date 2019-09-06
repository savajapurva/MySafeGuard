# MySafeGuard
An android app which can trigger SMS alert in case of any emergency situations.

### Technology & Tools

- Java
- Android Studio

### Login & Dashboard

- To access the features of our app user need to login through gmail or phone number
- Once user is registered he will be redirected to dashboard which have Pill Reminder, SafeZone, Fall Detection and SOS module

<img src="gifs/login.gif" width="300" height="500"> 

### SOS Feature

- It has two voice commands ”HELP ME” & “STOP UPDATES”
- SMS has been automatically sent to their added emergency contacts once the keyword “HELP ME” detected by the application
- Interestingly, voice can be detected even if the phone is locked by the user. Moreover, for the interval of 15 minutes SMS will be keep on sending automatically to their emergency contact until the user speaks “STOP UPDATE” keyword

<img src="gifs/voice_sos.gif" width="300" height="500"> 

### Safezone Feature

- Here, user can create safezone and type message for that safezone
- If user leave/re-enter to his safezone he will be notified by the toast notification. In addition, user can create danger zone just in case if he forget that the particular area is dangerous, the app will notify the user that you are entering in the danger zone

<img src="gifs/safezone.gif" width="300" height="500">

### Fall Detection Feature

- At first, after clicking on power on button the accelerometer starts detecting movements and if the sensor detects such value that exceeds threshold limit value than popup screen will be displayed to user with the timer of 20sec
- User can select mode of contact as well. For Instance, by enabling both call and text options the app will make a call as well as send an SMS to the emergency contact
- If nothing is selected in 20sec than SMS alert will send to the emergency contact with live location
- Level of sensitivity can also be configure as low, medium, high in the setting

<img src="gifs/falldetection.gif" width="300" height="500">

### Pill Reminder Feature

- In this module, users have to fill up the form for medication details
- Form includes details like medication name with its specific shape and color
- Reminders and schedule are added according to the user requirements
- It will store all your medications with different dosages, days, time and send reminder when its time to take

<img src="gifs/pill.gif" width="300" height="500">


