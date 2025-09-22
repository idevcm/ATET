# ATET Project

The complete documentation for this project is available at:  

👉 [ATET Documentation on Deepwiki](https://deepwiki.com/idevcm/ATET)

---

### Why do we centralize documentation on Deepwiki?
- Keep information always up to date in a single place.  
- Make reading and searching content easier.  
- Avoid duplication and version loss.  

---

## Additional Setup Required

### SeeSo GazeTrackerManager

To enable eye-tracking features, you must obtain an API Key from SeeSo.  
Register and generate your license at:  
👉 [https://www.seeso.io/](https://manage.seeso.io/#/sign)

Then, replace the value of `SEESO_LICENSE_KEY` in [`camp.visual.android.kotlin.sample.manager.GazeTrackerManager`](app/src/main/java/com/caicoders/pruebasatet/utils/Tracker/GazeTrackerManager.kt) with your personal key.

### Google Services

This project requires a valid `google-services.json` file for Firebase integration.  
Download it from the Firebase console and place it at:
