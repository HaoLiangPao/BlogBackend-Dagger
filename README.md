#### Backend for Six Degrees of Kevin Bacon
  Keywords: `Java backend`, `NoSQL (Neo4j)`, `REST API`, `Maven`
  
### 1. Descriptions<br/>
  This is an Java implementation of the backend for a service that allows users to post, view and delete blog posts. Dependency injection was done through Dagger. REST-api was used for server communication.
* The backend is running on port 8080. 
* MongoDB database is called as `blogs`; 
* Collections called `posts`;

------

### 2. Project/IDE Setup

* Command Line:
  * Install [maven](https://maven.apache.org/index.html)
  * To compile your code simply run mvn compile in the root directory (the folder
that has pom.xml)
  * To run your code run mvn exec:java 
* Eclipse:
  * File → Import → Maven → Existing Maven Projects
  * Navigate to the project location
  * Right click the project → Maven Build…
  * Input compile exec:java in the Goals input box
  * Click apply then run/close
  * You can now run the project by pressing the green play button
* Intellij:  
  * Import project
  * Navigate to the project location
  * Import project from external model → Maven
  * Next → Next → Next → Finish
  * Add Configuration → + Button → Maven
  * Name the configuration and input `exec:java` in the Command Line box
  * Apply → Ok
  * You can now run the project by pressing the green play button
  
------

### 3. Testing
* Response Body:
  * JSON format
  
* Response Status:
  * 200 OK for successful delete
  * 400 BAD REQUEST if the request body is improperly formatted or missing required information
  * 500 INTERNAL SERVER ERROR if save or add was unsuccessful (if user has done everything as per
requirements but request is unable to respond correctly)
  * 404 NOT FOUND if post does not exist
  * 405 METHOD NOT ALLOWED if called with something other than GET, PUT, DELETE

* Existing endpoints:
  * (GET) http://localhost:8080/api/v1/post
    * Get blog post. Returns list of corresponding posts. If _id is specified, return the exact post that has that
_id. If title is specified, return all posts that contain the specified words in the given title, in order.
    * Request Body:<br/>
{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "First"<br/>
}<br/>
or<br/>
{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"_id": "3m4jk393jneke93h3k"<br/>
}<br/>
or<br/>
{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "First"<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"_id": "3m4jk393jneke93h3k"<br/>
}<br/>
  * (PUT) http://localhost:8080/api/v1/post
    * Request Body:<br/>
{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "My First Post",<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"author": "Jake Peralta",<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"content": "This is my first post. I’m very excited to create this blog. YAY!",<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tags": [<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"first post",<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"new blog",<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"excitement"<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]<br/>
}<br/> 
  * (DELETE) http://localhost:8080/api/v1/post
    * Request Body:<br/>
{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"_id": "3m4jk393jneke93h3k"<br/>
}<br/>
* PostMan Testing Package:
  You can download it from the PostMan folder.


  

