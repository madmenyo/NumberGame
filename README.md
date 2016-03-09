# NumberGame
Google NumberGame example implemented using LibGDX

I removed the entire android/res/values/ folder from the project to mask my sensitive ID's. You basically need the very same Google Playe Service setup as in Googles default android example. When you have setup your leaderboards and achievements you can grab all the Id's and paste them in android/res/values/ids.xml. I did not alter strings.xml as in the example except for adding a view of my own achievements in it. [Google has a handy tool to add all the achievements and leaderboards to your play service app.](http://playgameservices.github.io/android-basic-samples/config-magic/index.html?sample=typeanumber) you just need to supply your ID.

Other then that it's a completely functional mimic of the NumberGame created with the LibGDX framework. Use this as reference to get familiar with integrating Google's play services into LibGDX.

What I basically do in core is setting up a simple Interface to communicate between the different projects. For the desktop project I just sent a string to the console telling the different functionality is not working for Desktop applications. Ofcourse you can add your own desktop implementations here or using the REST API to integrate the desktop module with Google Play Services.In the android project I implement the same Interface and integrate the Google API. I have not seen a proper LibGDX implementation since Base Game Activity was declared deprecated.

I also implemented interstitial ads from Google admob. I added a button on the main menu that is supposed to display interstitial ads. Just create a account and ad on admob and add in the ad ID in ids.xml with the name "Interstitial_ad" or supply it on top of the onCreate() method of the AndroidLauncher. It are just a view lines of code that do the magic.
