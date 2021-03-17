# Download APK Test

## Project Details:

- Resource: https://androidwave.com/download-and-install-apk-programmatically/
- This code is converted from Kotlin into Java.
- I did not use the Extensions as it is not natively available to Java (it's also a paid plugin?). It's really a cool feature though!
- The sample APK URL in the sample is no longer valid, causing the code to not work. I used the APK from my other test project to test.

----

## Thoughts:

This practice application is my practice in downloading an APK from the internet with a provided URL, and triggering the installation of the APK from within the app.

This would be useful for deploying application updates to clients' devices without having to remote-in. For additional context, I am working with client specific private applications so there is no Google Store integration for most of the applications.

If done correctly, this will be beneficial to add into applications where we cannot remote in via MDM and cannot physically be there to update it. We would need a way for the client/staff to trigger the download when needed. Some ideas comes to mind such as a prompt/menu that is hidden & password locked maybe (some applications are for unmanaged kiosk applications, so it's a bit difficult to hide a settings section somewhere).

### Applications:

- You have the application's version name/code in BuildConfig object.
- You can do an API call to a server and the call would return to you the latest version of the app and the version details (and maybe the URL).
- Process whether you are up-to-date and if not updated, trigger an update available status.
- Upon confirmation of starting the update, just do the download and install as per the sample app.