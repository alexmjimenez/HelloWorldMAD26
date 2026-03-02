# HelloWorldMAD26

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
* **Data Persistence:** Implemented an AlertDialog that prompts for a User ID upon opening the app. This ID is saved using SharedPreferences to greet the user in future sessions without asking again.
* **File Storage:** The app silently records the user's location history in a local gps_coordinates.csv file. 
* **Recording Features:** The recorded data includes Latitude, Longitude, Altitude, and a Timestamp. A Switch was added to the main screen to easily enable or disable this recording feature.
* **File Reading:** Implemented a feature in ThirdActivity to read the internal .csv file and display the saved coordinates log in a scrollable view.
* **Custom App Icon:** Replaced the default Android green robot with a custom launcher icon using Asset Studio.

## Demo
The following video shows the app's functionality
* **WEEK 2:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBfmI1b7zOyT77djcYB0f35AaHORbE7RlY49zui4uB_K44?e=ExVohq
* **WEEK 3:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBL9AbN5pEFS4AFgErhoTRPAY7HVbP7Vkku8KaPdDwlmKg?e=HKlpFe
* **WEEK 4:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgCP1z2ZNW0BSqMtG-QyIma1AVqTo_K_I7dt0u66-tyl-_U?e=5tMYlS
