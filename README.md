# PhotoSharingApp
A social media photo app for sharing photos and looking up the location of where the photo was taken.
The app requires you to log in. After you've logged in, you can, with the floating action button, either take a photo with the device's camera, or select a photo from the gallery. The photo will then show up among a list of all the photos taken by everyone who used this app. The photos will be diplayed along with the username of the uploader. On another page there will be a map where there will be a marker for every location where a photo was taken. Each marker will have the username of the uploader and the photo itself.

# Installation
This app uses the Gradle build system.

First download the code by cloning this repository or downloading an archived snapshot. (See the options at the top of the page.)

In Android Studio, use the "Import non-Android Studio project" or "Import Project" option. Next select the directory in which you donwloaded this repository. If prompted for a gradle configuration accept the default settings.

Make sure the Android version in your device/emulator is at least 5.0 (API level 21).

# API Keys
You'll need Api Keys for google maps and cloudinary

In the gradle.properties file you need to add the following lines:\n
GOOGLE_MAPS_API_KEY=YOUR_MAPS_API_KEY\n
CLOUD_NAME=YOUR_CLOUD_NAME\n
CLOUD_API_KEY=YOUR_CLOUD_API_KEY\n
CLOUD_API_SECRET=YOUR_SECRET_KEY

Get the keys from here: https://developers.google.com/maps/documentation/android-api/start

and here: https://cloudinary.com/users/register/free (you'll get the keys after signing up)
