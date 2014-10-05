TwitterProject with Fragments
==============
![alt tag](https://github.com/olevitsky/TwitterProject/blob/TwitterProjectWithFragments/Twitter2_timelines.gif)
![alt tag](https://github.com/olevitsky/TwitterProject/blob/TwitterProjectWithFragments/Twitter2_timelines_2_userProfile_v2.gif)

Simple twitter client that allow to connect to Twitter using user name and password and fetch tweets, mentions and user pfofile to the mobile device
Main features:
Implemented in main branch
- User can sign in to Twitter using OAuth login
- User can view the tweets from their home timeline
  - Displays username, name and body for each tweet
  - Display the relative timestamp
  - Supports infinite pagination when scrolling (fetches more tweets as needed)
  - Launches web browser when click on clickable tweet
- User can compose a new tweet
  - Compose button in the action bar
  - Entered tweet is posted to twitter
  - User can see the number of characters left when typing
  - After posting the tweet the app takes user back to the timeline with new tweet visible in the timeline
- User can open twitter app offline
 - Utilizes ActiveAndroid ORM to manage data locally
- User interface and theme the app to feel "twitter branded"

Additional features for branch: TwitterProjectWithFragments
 - User can swtich between Timeline and Mention views using tabs
 - User can view their home timeline tweets
 - User can view the recent mentions of their username.
 - User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
 - User can navigate to view their own profile
     User can see picture, tagline, # of followers, # of following, and tweets on their profile.
- User can click on the profile image in any tweet to see another user's profile.
    User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
    Profile view  includes that user's timeline
- Robust error handling, check if internet is available, handle error cases, network failures
- User interface and theme the app to feel "twitter branded"


    



