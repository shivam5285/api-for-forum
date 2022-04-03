# Mini Forum API
This spring boot application exposes certain API endpoints to create, delete and fetch posts or comments by users after login.

## Problem Statement
The forum should contain API to perform these actions.
A user should be able to
* Create a post (Anonymously / Non-Anonymously)
* Comment on a post (Anonymously / Non-Anonymously)
* See all his posts and comments
* Should be able to delete his post and comments

The catch here is the user can only know which of his/her posts are anonymous
Another user cannot know who has posted what anonymously

## Approach
The idea is to use JWT for user authentication and support required features based on user authorization

### Database Requirements
I have created two tables for the purpose of this assignment.
1. **User Credential table (userId, hashedPassword)**
   
    This table is used to authenticate a user based on credentials

2. **Content Table (contentId, contentType, creator, isAnonymous, creationTimestamp, title, parentPostId, mediaType, media)**

    Since a post and comment have same attributes, I have used single table called content to store both which can be differentiated by contentType. MediaType of a post/comment can be text, image or video. In case of image/video, their URL is stored in media, otherwise text data is stored in media column. Every post has a title whereas every comment has parentPostId.
  

### Authentication
I have used JWT to for user authentication. Expiry time and secret used to generate JWT can be configured from application.properties file.
A user gets JWT by hitting authenticate ````/user/authenticate```` endpoint with correct username and password.
- Username and hashedPassword is checked in Credential table. If it matches, a JWT is generated and provided to user
  
_**Note**: It is not a good practice to store hashed password, rather password should be encrypted at client using public key and then saved in database. For, matching, it should be decrypted using private key, which is fetched from keystore (like LunaHSM). But, I am using hashedPassword for this assignment to focus mainly on required features._

The user uses same JWT to interact with other API endpoints. In case the token is about to expire, it is refreshed via ````user/refresh```` endpoint

### New Content Creation
User can create new content using ````/user/fetch/content```` endpoint. The given content is inserted in database and a content ID is generated.

### Content Deletion
A user can delete only the content which belongs to him/her using `````/user/delete/content````` endpoint. Returns a boolean if content is deleted, otherwise throws particular exception

### Fetch Content
There are two modes of fetching content-
#### 1. Fetching self created content
   A user can fetch (anonymous and non-anonymous) contents based on type (post/comment) created by himself/herself in the past. A database search is performed for given userId and contentType and all the results are returned
#### 2. Fetching all content
   A user can fetch all contents created by all users based on type. A database search is performed for given contentType to get all posts/comments. Then, those are filtered i.e. username is removed for post/comment which is anonymous and is posted by a different user. Then, filtered list is returned to the user.
   
## Steps to Run
JDK11 and Maven3 is required to run this application.
- Clone the source code and let maven resolve dependencies automatically
- Create a database with above-mentioned two tables and run the SQL server. Then, configure server IP in application.properties
- _Optional:_ JWT expiry time and secret can be set in application.properties file
- Start the application by running main method of ````src/main/java/com/nowandme/forum/ForumApplication.java````