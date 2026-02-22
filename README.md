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

## Demo
The following video shows the app's functionality
* **WEEK 2:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBfmI1b7zOyT77djcYB0f35AaHORbE7RlY49zui4uB_K44?e=ExVohq
* **WEEK 3:** https://upm365.sharepoint.com/:f:/s/MobileAPPDevelopmentGroup/IgBL9AbN5pEFS4AFgErhoTRPAY7HVbP7Vkku8KaPdDwlmKg?e=HKlpFe
