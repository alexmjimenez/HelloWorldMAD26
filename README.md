# HelloWorldMAD26

**HelloWorld** is a project focuses on **environmental awareness**, encouraging citizens to map and catalog urban waste containers (trash cans) to promote a cleaner environment.

## Week 2
* **Advanced Navigation**: Implementation of a 3-level navigation flow (MainActivity ↔ SecondActivity ↔ ThirdActivity)
* **GPS Location**: The app requests runtime permissions and displays the device's latitude and longitude
* **System Logging**: Integrated Logcat messaging with different levels of verbosity (Debug and Info) to monitor the activity lifecycle
* **Custom Styling**: UI components have been customized

## Week 3
* **RecyclerView:** Replaced static buttons with a dynamic list in SecondActivity. We used a custom Adapter (MyAdapter.kt) and a data model (MyItem.kt) to display a list of points of interest (Campus Sur UPM, Puerta del Sol)
* **Navigation:** When clicking on any list item, the adapter captures the event and sends the latitude, longitude, and place name to the next screen
* **OpenStreetMap:** Integrated the osmdroid library within OpenStreetMapsActivity
* **Map Control:** The map receives the coordinates via an Intent, configures itself dynamically, sets an appropriate zoom level, and automatically centers on the user's selected point using GeoPoint

## Week 4
* **Data Persistence:** Implemented an AlertDialog that prompts for a User ID upon opening the app. This ID is saved to greet the user in future sessions without asking again.
* **File Storage:** The app records the user's location history in gps_coordinates.csv file. 
* **Recording Features:** The recorded data includes Latitude, Longitude, Altitude, and a Timestamp. A Switch was added to the main screen to easily enable or disable this recording feature.
* **Custom App Icon:** Replaced the default Android green robot with a custom launcher icon using Asset Studio.

## Week 5
* **Navigation Menu:** Replaced the basic navigating buttons with a professional Navigation Drawer to manage app navigation smoothly from a side menu
* **Settings Activity:** Created a SettingsActivity that allows the user to display and edit their User ID
* **Custom ListView:** Upgraded the data reading feature. The raw .csv text file is now parsed and displayed in a structured, multi-column ListView using a custom ArrayAdapter (showing Date, Latitude, Longitude, and Altitude).
* **List Item Interaction:** Enabled touch events for every list item in the coordinates log.

### Week 6
* **External RESTful API:** Initial OpenWeather API integration to fetch weather data based on the user's coordinates.
* **Local Database:** Full CRUD functionality for adding, editing, and deleting places using PlacesEntity.
* **Cloud Backend:** Setup of the Firebase project and integration of Firebase Auth for secure user login.

### Week 7
* **External Libraries:** Full integration of Glide (weather images) and Retrofit (weather API calls). 
* **Security:** API Keys successfully hidden and managed via SharedPreferences.
* **Environmental Awareness:** Built the "trash can" tracking functionality.
* **Gamification:** Implemented the XP Score System directly tied to the user's contributions.
* **Cloud Sync:** Final data synchronization to Firebase Realtime Database.
* **Clean Code:** Dynamic map icons allocation, deep refactoring, and stability fixes for empty CSV readings.

## Demo
The following video shows the app's functionality
* **WEEK 2:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBfmI1b7zOyT77djcYB0f35AaHORbE7RlY49zui4uB_K44?e=ExVohq
* **WEEK 3:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBL9AbN5pEFS4AFgErhoTRPAY7HVbP7Vkku8KaPdDwlmKg?e=HKlpFe
* **WEEK 4:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgCP1z2ZNW0BSqMtG-QyIma1AVqTo_K_I7dt0u66-tyl-_U?e=5tMYlS
* **WEEK 5:** https://upm365.sharepoint.com/:v:/s/MobileAPPDevelopmentGroup/IQC-EGgo1LSqTbYTrk_Twoz0AaF8IpDL83fgrmFmK0wTaOw?e=zrHCsj
* **WEEK 7:** https://upm365.sharepoint.com/:v:/s/MobileAPPDevelopmentGroup/IQCwnYLH-nNWQr6hcnNfafnBAeCEd6Xqk-7FcywgiVxH8B8?e=hDnOiG

## Final project resources
All the resources (video and task distribution) are on the OneDrive:
https://upm365-my.sharepoint.com/shared?id=%2Fsites%2FMobileAPPDevelopmentGroup%2FShared%20Documents&listurl=https%3A%2F%2Fupm365%2Esharepoint%2Ecom%2Fsites%2FMobileAPPDevelopmentGroup%2FShared%20Documents

## Extra
* API Key of OpenWeatherMap:
3e6cb458858bbbc6f173401b67ceca53

* Firebase Realtime Database:
https://console.firebase.google.com/u/0/project/helloworld-40476/database/helloworld-40476-default-rtdb/data

