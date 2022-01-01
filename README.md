<div align="center">
<img src="https://github.com/AbdullahM0hamed/SalahTimes/raw/c17b8b6cd7764548a49fab6a80b6d25cb92295c6/app/src/main/res/drawable-xxhdpi/ic_launcher.png">
<br>
<strong><i>An android app which helps a Muslim keep track of the times of Salah</i></strong>
</div>

## Salah Times
This is a simple android app intended to help Muslims keep track of the times of salah, whilst also being open-source so Muslims can feel comfortable letting this app use their location data. Everything is built entirely via Github [actions](https://github.com/AbdullahM0hamed/SalahTimes/actions).

## Planned Features - CheckList
This app is not yet complete enough (in my opinion) for release. Here is a list of features that I want done, and whether or not they are complete.

- [x] Calculate Salah Times
  - [x] Make calculation method configurable
  - [x] Calculate times for other days, so users can check ahead of time if they wish
- [x] Notify the user when it is time for Salah. 
- [ ] - Have a functioning Qiblah compass
  - [x] - Provide a fallback for users who do not have the necessary sensors to allow for a working compass, using the sun as a reference. Unfortunately, I am one of these people and need help testing any code I will have to write for a functioning compass.
- [ ] A widget for Salah Times
- [ ] A widget that shows the Hijri date
- [ ] A comprehensive theme engine (I'm an advocate of choice when it comes to app aesthetics)

## Screenshots

The following screenshots showcase the app is it right now, and may change in the near-future.

<img align="center" src="https://github.com/AbdullahM0hamed/SalahTimes/blob/main/screenshots/screenshot_splash.jpg">
<img align="center" src="https://github.com/AbdullahM0hamed/SalahTimes/blob/main/screenshots/screenshot_prayer_times.jpg">
<!-- <img align="center" src="https://github.com/AbdullahM0hamed/SalahTimes/blob/main/screenshots/screenshot_missing_sensors.jpg"> -->
<img align="center" src="https://github.com/AbdullahM0hamed/SalahTimes/blob/main/screenshots/screenshot_solar_compass.jpg">
<img align="center" src="https://github.com/AbdullahM0hamed/SalahTimes/blob/main/screenshots/screenshot_settings.jpg">

## Open-source libraries

The following are the open-source libraries used in this prproject (bar anything that comes from android, because of course I'm using android):

- [Adhan Java](https://github.com/batoulapps/adhan-java)
- [ummalqura-calendar](https://github.com/msarhan/ummalqura-calendar)
- [reduxkotlin](https://github.com/reduxkotlin/redux-kotlin)
- [Conductor](https://github.com/bluelinelabs/Conductor)
- [Dexter](https://github.com/Karumi/Dexter)
- [vanilla-place-picker](https://github.com/Mindinventory/vanilla-place-picker)

## Thanks

To:
  - [Thunderbolt-Blue](https://github.com/Thunderbolt-Blue) for the original design on which this app is based
  - My sister for the logo (though I plan to add several others for the sake of choice)
