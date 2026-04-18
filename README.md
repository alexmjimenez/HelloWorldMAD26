# Workspace

Github:
* Repository: https://github.com/alexmjimenez/HelloWorldMAD26.git
* Releases: https://github.com/alexmjimenez/HelloWorldMAD26/releases

Workspace: https://upm365.sharepoint.com/:u:/s/MobileAPPDevelopmentGroup/IQCpZIwIHHZUQYlnT6Hqz18QAfoaPzNKJiFzHrLOmGAjLPk?e=FPQX9R

## Description
HelloWorld is a "clean city App" focused on gamification & environmental awareness. It allows citizens to map and catalog urban waste containers (trash cans) to promote a cleaner environment.
It enhances the experience within its scope by rewarding users with XP (Experience Points) for finding and mapping new recycling spots. Additionally, it provides real-time weather information so users know the conditions before going out to recycle. Compared to standard maps, this app is specifically tailored to community-driven waste management and eco-friendly gamification.

## Screenshots nad navigation
| Home | Map | Places |
| :---: | :---: | :---: |
|<img width="450" height="924" alt="1" src="https://github.com/user-attachments/assets/77a0472c-db01-4eac-aafa-c7b0097f240e" /> Displaying the Gamification Score (XP) and Location Switch.|<img width="450" height="929" alt="image" src="https://github.com/user-attachments/assets/178e67c4-c999-44d7-868f-dada099ac743" /> Icons for trash cans and a real-time weather card.|<img width="446" height="890" alt="3" src="https://github.com/user-attachments/assets/345e08e2-6c19-4808-afde-7c298a3a0078" /> Add, edit and delete places/trash cans|
| Add new place | Add trash can | Records |
|<img width="447" height="895" alt="4" src="https://github.com/user-attachments/assets/1363b154-f042-4f73-9b5c-b107c24a2dbb" />|<img width="447" height="932" alt="5" src="https://github.com/user-attachments/assets/1825603d-3991-4ad5-9403-651af209f49a" />|<img width="450" height="925" alt="6" src="https://github.com/user-attachments/assets/373bf910-ea54-4ff7-ab1a-2349224197c0" /> When you enable the location, it will be recording your the GPS coordinates, latitude, longitude...|

## Demo video

## Features
### Functional features
* Search and view default recycling points on an interactive map.
* Add new trash cans (Organic, Plastics, Paper) with auto-filled categories to keep the city clean.
* **Gamification system:** Earn 10 XP for every new location mapped.
* View real-time weather conditions for the selected map location.
* Record, track, and manage physical GPS routes.
### Technical features
* **Maps:** OpenStreetMaps (Osmdroid).
* **RESTful APIs:** OpenWeatherMap API integrated using Retrofit and Gson.
* **Images:** Glide library used for loading dynamic weather icons.
* **Sensors:** GPS coordinates (Latitude, Longitude, Altitude) utilizing LocationManager.
* **Persistence in csv/text file:** Tracking data saved in gps_coordinates.csv.
* **Persistence in shared preferences:** User ID and API Key management.
* **Persistence in Room database:** Full CRUD functionality via PlacesEntity.
* **Firebase Realtime database:** Real-time data storage and community sync for places.
* **Firebase authentication:** Secure user login via Email and Google Auth UI.

## How to Use
1. Open the app and log in using your Google Account or Email.
2. Enter a User ID when prompted to track your profile.
3. Check your Score XP on the main screen. 
4. Navigate to the Map to check your surroundings, dynamic icons, and real-time weather.
5. To map a new container, go to Places, tap Add Trash Can, and save.
6. Return to the main screen to see your XP Score increase by 10 points!

## Additional section: Development History
### Week 2
* **Advanced Navigation**: Implementation of a 3-level navigation flow (MainActivity ↔ SecondActivity ↔ ThirdActivity)
* **GPS Location**: The app requests runtime permissions and displays the device's latitude and longitude
* **System Logging**: Integrated Logcat messaging with different levels of verbosity (Debug and Info) to monitor the activity lifecycle
* **Custom Styling**: UI components have been customized
### Week 3
* **RecyclerView:** Replaced static buttons with a dynamic list in SecondActivity. We used a custom Adapter (MyAdapter.kt) and a data model (MyItem.kt) to display a list of points of interest (Campus Sur UPM, Puerta del Sol)
* **Navigation:** When clicking on any list item, the adapter captures the event and sends the latitude, longitude, and place name to the next screen
* **OpenStreetMap:** Integrated the osmdroid library within OpenStreetMapsActivity
* **Map Control:** The map receives the coordinates via an Intent, configures itself dynamically, sets an appropriate zoom level, and automatically centers on the user's selected point using GeoPoint
### Week 4
* **Data Persistence:** Implemented an AlertDialog that prompts for a User ID upon opening the app. This ID is saved to greet the user in future sessions without asking again.
* **File Storage:** The app records the user's location history in gps_coordinates.csv file. 
* **Recording Features:** The recorded data includes Latitude, Longitude, Altitude, and a Timestamp. A Switch was added to the main screen to easily enable or disable this recording feature.
* **Custom App Icon:** Replaced the default Android green robot with a custom launcher icon using Asset Studio.
### Week 5
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

## Participants
List of MAD developers:
* Huili Chen (huili.chen@alumnos.upm.es)
* Lozano Martín Alex (@alumnos.upm.es)

Workload distribution between members: (50% Huili / 50% Alex).
* *Alex focused on base app structure, Room Database architecture, GPS sensors integration, dynamic map icons, and Firebase Auth.*
* *Huili led the external API connections (OpenWeather, Retrofit, Glide), Gamification XP system, SharedPreferences, and Firebase Realtime Database cloud sync.*

## Demo
The following video shows the app's functionality
* **WEEK 2:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBfmI1b7zOyT77djcYB0f35AaHORbE7RlY49zui4uB_K44?e=ExVohq
* **WEEK 3:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBL9AbN5pEFS4AFgErhoTRPAY7HVbP7Vkku8KaPdDwlmKg?e=HKlpFe
* **WEEK 4:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgCP1z2ZNW0BSqMtG-QyIma1AVqTo_K_I7dt0u66-tyl-_U?e=5tMYlS
* **WEEK 5:** https://upm365.sharepoint.com/:v:/s/MobileAPPDevelopmentGroup/IQC-EGgo1LSqTbYTrk_Twoz0AaF8IpDL83fgrmFmK0wTaOw?e=zrHCsj
* **WEEK 7:** https://upm365.sharepoint.com/:v:/s/MobileAPPDevelopmentGroup/IQCwnYLH-nNWQr6hcnNfafnBAeCEd6Xqk-7FcywgiVxH8B8?e=hDnOiG


