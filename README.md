Operation Beerbarossa
=====================

## To use the app, follow this url:
http://1-dot-cpsc310demoapp.appspot.com

## To deploy it to google app engine

1.  Make sure the build paths has the basic GWT stuff
2.  Make sure it has the twitter and the gwt-maps.jar in the
    lib folder
3.  Be sure to change the application name in
    appengine-web.xml


## To set up Twitter:

1. Go to this url:   http://apps.twitter.com
2. Sign in with User as:  Beerbarossa and the password is
   cpsc310summer
3. Click on the user icon and select "Beerbarossa"
4. Go to settings and change the website and callback URL to
   match the URL you are deploying to
5. Select update and refresh

## If you want to deploy under your own Twitter account:
1. You go to the same URL
2. Sign in with your own Twitter account
3. Create app
4. Set consumer key and consumer secret, in the
   TwitterServiceImpl.java
5. Go to settings and change the website and callback URL to
   match the URL you are deploying to
6. Select update and refresh

## Troubleshooting:

- if you see all the .jars but the project is still
reporting errors (files are red), right click on the
project, select "Build Path" and then "Configure Build Path"
- Under "Order and Export" uncheck all BUT the /src and
/test
- press "OK"
- Then re-add the stuff you unchecked
