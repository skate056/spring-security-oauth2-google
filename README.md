# Spring Security OAuth2 Google Connector

---
## What do I do?

I am simple Spring MVC application which secured by Spring Security. Instead of using simple form based security, I am
secured by Spring Security OAuth2 and the OAuth provider is Google.


## Why am I required?
Developing a Spring MVC web application with Security enabled and integrated with Google is hard to implement. So this
is a sample implementation of Spring MVC + Spring Security OAuth2 + Google Provider.

## Get it up and runnning
The project is built with Gradle so you can run the build using the following gradle command
`gradle clean build`

To start up the application using the following command (Since it is a Spring boot application)
`gradle bootRun`

The application.properties (in src/main/resources) contains the details of the Google application which it uses to authenticate details.
Change the values of the following attributes to the values for your application
google.client.id
google.client.secret

###To register a Google App perform the following steps
* Go to https://console.developers.google.com and login with your Google account (this will be the developer account and the email of 
this account will be published when someone tries to authenticate with the Google application)
* If you don't have a project create a new one and then click into the project.
* In the menu on the left side select "APIs & auth" --> "Credentials" --> "Create a new client ID"
* In the popup select the following
  * Application Type = Web Application
  * Authorized Javascript Origins = <YOUR DOMAIN>, 
  * Authorized Redirect URI = <THE CALL BACK HANDLER>, the URI for our application is /googleLogin so for local testing you should enter 
  http://localhost:9000/googleLogin and http://localhost:9000/googleLogin/ on different lines.
  * Copy the client ID and client Secret and update the application.properties
 * Make sure you update the mandatory values on the "APIs & auth" --> "Consent screen" page as the application will not work without it.

 When you have a the Google App configured and the Spring boot application and you navigate to http://localhost:9000. It will redirect
 you to a Google login page. Upong login it will ask you to authorize your application for access to your account to get email and profile
 data. On successful login it will render the basic HTML page which means the authentication was sucessful.

 If you are interested in fetching the Google profile data in type in the following URL http://localhost:9000/find?term=hello in the 
 authenticated browser session and the profile data will be logged in the console of the Spring application.

 ## Technical nitty gritties
 It is important to use the correct filters from Spring Security OAuth2 to get this right and it is also very important to get their 
 order right. 

 The filters oauth2ClientContextFilter and oAuth2AuthenticationProcessingFilter are very important the their position in the chain is 
 equally important. oauth2ClientContextFilter must exist before oAuth2AuthenticationProcessingFilter. Also both these must be before the 
 FILTER_SECURITY_INTERCEPTOR so that the authentication with google is complete before we check for the security permissions for access to
 the application.

 Also the authentication entry point of the secured application is clientAuthenticationEntryPoint which essentially redirects the user to 
 the configured url /googleLogin when the user is not authenticated. When the url /googleLogin is hit the filters listed above lead to a 
 redirect to the Google URL. Upon authentication, Google redirects back to us and the filter populates the Authentication object.

 After all the authentication is complete, the request is redirected back to http://localhost:9000. This request will hit the 
 FILTER_SECURITY_INTERCEPTOR but the session is already authenticated and the Security Context is populated so access to the application 
 is granted by the interceptor and the session is authentication.

 If you want to see the profile data, in an authenticated session (since the resource uses the OAuth2RestOperations) a GET to 
 http://localhost:9000/find?term=hello will display the profile data on the web application console. See the 
 src/main/java/com/rst/oauth2/google/api/UserResource.java for implementation details.