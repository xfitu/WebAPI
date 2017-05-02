# WebAPI
It is Java Web API that Create and Search User's object and its attributes to and from Microsoft Active Directory.There are total fifteen Servlet classes in this project each Servlet class handles different request and returns JSON object as response. The API is created using Java Servlet and deploy on Apache Tomcat Web Server. This Web API uses JNDI API library (http://www.oracle.com/technetwork/java/jndi/index.html) to make LDAPS connection with Microsoft Active Directory server in order to create user object,add, update,delete and search user's attributes in Microsoft Active Directory. 

 
 There are three packages in this Web API project:
1. Package for configureation Java classes: local.healthportal.ConfigClass
There are three different configuration Java classes in this package:
	1. AD_Admin_Config.java, it contains credential of Active Directory Administrator
	2. DomainController_Certificate_Config.java, it contains the path to the Active Directory server public key certificate for SSL       connection 
	3. Email_Config.jva, it contains email credential that will be used to send email to user's email address
		
2. Package for custom Java classes: local.healhtportal.javaclass
There are total five custom Java classes in this package and each class has different function.
	1. ActiveDirectory.java, it contains all the methods that are used to connect Active Directory server and add new users to Active Directory
	2. Email.java, it is the class used to send email to user's email address by using smtp.gmail.com mail server
	3. PasswordChecker.java, it is the class used to verify password requirements for Active Directory
	4. ResponseObject.java, it is the class used to hold JSON response object 
	5. UserAttributes.java, it is the class used to hold JSON object of user's attributes

3. Package for Java Servlet classes: local.healthportal.ServletClass  
 There are total fifteen Servlet classes in this package and each Servlet class handles different request
 and returns JSON object as response. 
		
1.AddUser- Description: register user to active directory
	- access URL: https://localhost:7443/WebAPI/AddUser
	- Required parameter variables: fname,lname,username,email,role, password
	- Send request method: GET or POST
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"your account is already created"} 
		
		- fail: {"Status":"fail","ErrorMessage":"invalid access,must provide all the required parameters in the request"}
        -How to send AJAX request to this WEP API using JQUERY library
	dataString = "fname=" + fname+"&lname="+lname+"&username="+username+"&email="+email+"&password="+password+"&role="+role;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/AddUser",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	
	   	
2.SendEmail
	- Description: send email to given email address 
	- access URL: https://localhost:7443/WebAPI/SendEmail
	- Required parameter variables: fname,lname,receiveremail,subject,body,verificationlink
	- Send request method: GET or POST
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"email has sent"} 
		
		- fail: {"Status":"fail","ErrorMessage":"invalid email address"}
		
         -How to send AJAX request to this WEP API using JQUERY library
	dataString = "fname=" + 	fname+"&lname="+lname+"&receiveremail="+email+"&subject="+subject+"&body="+body+"&verificationlink="+link;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/SendEmail",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 		

3.ActivateAccount
	- Description: activate or enable user's account in active directory 
	- access URL: https://localhost:7443/WebAPI/ActivateAccount
	- Required parameter variables: fname,lname
	- Send request method: GET or POST
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":your account has been activated"} 
		
		- fail: {"Status":"fail","ErrorMessage":"oops,cannot activate your account"}
		
	
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "fname=" + fname+"&lname="+lname;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/ActivateAccount",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 
 
4.AddAttribute
	- Description: add a new user's attribute to active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/AddAttribute
	- Required parameter variables: name,newattributename,newattributevalue
								
	- Send request method: GET or POST
		EX: name=test user & newattributename=description & newattributevalue= this is an user
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":your new displayName is already added"} 
		
		- fail: {"Status":"fail","ErrorMessage":"oops,your new displayName cannot be added"}
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name=" + name+"&newattributename="+attributename+"&newattributevalue="+attributevalue;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/AddAttribute",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	
		
5.GetAnUserAttributes
	- Description: retrieve all the user's attributes which have values set in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/GetAnUserAttributes
	- Required parameter variables: name or username
								
	- Send request method: GET or POST
						EX: name=test user or username=accountname
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","objectCategory":"CN=Person,CN=Schema,CN=Configuration,DC=AD-portal,DC=local","email":"test@gmail.com",
					"memberOf":"patient,doctor","group":"patient,doctor","instanceType":"4","state":"Bangkok"
					,"objectClass":"toppersonorganizationalPersonuser","name":"test user","lastname":"user"
					,"telephoneNumber":"345874587","userAccountControl":"512","primaryGroupID":"513"
					,"postalCode":"74635","physicalDeliveryOfficeName":"ICT","country":"Thailand"
					,"cn":"test user","mobile":"783748736","sAMAccountType":"805306368","firstname":"test"
					,"displayName":"test user","userPrincipalName":"test@AD-portal.local","streetAddress":"salaya soi 11"
					,"countryCode":"764","city":"Nakhom pathom","fist_twoletters_of_countryname":"TH"
					,"distinguishedName":"CN=test user,OU=portal,DC=AD-portal,DC=local","username":"test","PoBox":"3847"} 
		
		- fail: {"Status":"fail","ErrorMessage":"oops,name does not exist"}
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name; or dataString = "username"+username;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/GetAnUserAttributes",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	
		
6.GetAttribute
	- Description: retrieve an user's attribute which has value in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/GetAttribute
	- Required parameter variables: username,attributename
								
	- Send request method: GET or POST
						EX: username=test & attributename=mail
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","email":"test@gmail.com"}
		- fail: {"Status":"fail","ErrorMessage":"user attribute has no value"}	
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&attributename="+attributename;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/GetAttribute",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	
		

7.UpdateAttribute
	- Description: update value of an user's attribute which has value in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/UpdateAttribute
	- Required parameter variables: name,attributename,attributevalue
								
	- Send request method: GET or POST
						EX: name=test user & attributename=mail &attributevalue=test@email2.com
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"your mail is already updated to test@email2.com"}
		- fail: {"Status":"fail","ErrorMessage":"your mail cannot be updated to test@email2.com"}		
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&attributename="+attributename+"&attributevalue="+attributevalue;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/UpdateAttribute",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	
8.UpdatePassword
	- Description: update user's password in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/UpdatePassword
	- Required parameter variables: name,username,password
								
	- Send request method: GET or POST
						EX: name=test user & username=test & password=dkjfkdfjHG123
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"your account password is already updated"}
		- fail: {"Status":"fail","ErrorMessage":"oops,your account password cannot be updated "}		
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&username="+username+"&password="+password;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/UpdatePassword",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	

9.UpdateName
	- Description: update user's name in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/UpdateName
	- Required parameter variables: name,newfname,newlname
								
	- Send request method: GET or POST
						EX: name=test user & newfname=test2 & newlname=user2
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"your name is already updated }
		- fail: {"Status":"fail","ErrorMessage":"oops,your name cannot be updated "}		
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&newfname="+newfname+"&newlname="+newlname;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/UpdateName",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	
		
10.UpdateUsername
	- Description: update user's username in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/UpdateUsername
	- Required parameter variables: name,newusername
								
	- Send request method: GET or POST
						EX: name=test user & newusername=testuser2
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"your username is already updated"}
		- fail: {"Status":"fail","ErrorMessage":"oops,your username cannot be updated "}		
        -How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&newusername="+newusername;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/UpdateUsername",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 
11.RemoveUserFromGroup
	- Description: remove user from a certain group in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/RemoveUserFromGroup
	- Required parameter variables: name,groupname
								
	- Send request method: GET or POST
						EX: name=test user & groupname=doctor
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"user is already removed from doctor group"}
		- fail: {"Status":"fail","ErrorMessage":"oops,user cannot be removed from doctor group"}		
        -How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&groupname="+groupname;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/RemoveUserFromGroup",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 
12.AddUserToGroup
	- Description: Add  a user to a certain group in active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/AddUserToGroup
	- Required parameter variables:name,groupname
								
	- Send request method: GET or POST
						EX: name=testuser & groupname=doctor
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"user is already member of the group"}
		- fail: {"Status":"fail","ErrorMessage":"cannot set user as member of the group"}		
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&groupname="+groupname;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/AddUserToGroup",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 	
13.DeleteAttribute
	- Description: Delete an user's attribute which has value in the active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/DeleteAttribute
	- Required parameter variables:name,attributename,attributevalue
								
	- Send request method: GET or POST
						EX: name=test user & attributename=mail & attributevalue=testemail@gmail.com
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"your mail with value:testemail@gmail.com is already removed"}
		- fail: {"Status":"fail","ErrorMessage":"oops,cannot remove mail with value:testemail@gmail.com"}		
        -How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&attributename="+attributename+"&attributevalue="+attributevalue;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/DeleteAttribute",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 
14.DeleteUser
	- Description: Delete an user or account from active directory 
			See LDAP Name for each user's attibute in Active Directory here: https://www.manageengine.com/products/ad-manager/help/csv-import-management/active-directory-ldap-attributes.html		
	- access URL: https://localhost:7443/WebAPI/DeleteUser
	- Required parameter variables:name
								
	- Send request method: GET or POST
						EX: name=test user 
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"user is already deleted"}
		- fail: {"Status":"fail","ErrorMessage":"oops,user cannot be deleted"}		
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/DeleteUser",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 
 15.ForgetPassword
	- Description:Check user's name and username in active directory when user forgets password
	- access URL: https://localhost:7443/WebAPI/ForgetPassword
	- Required parameter variables: name,username,email
	- Send request method: GET or POST
	
	- Reponse is returned as JSON object:
	
		- success: {"Status":"success","SuccessMessage":"valid user"} 
		
		- fail: {"Status":"fail","ErrorMessage":"invalid user"}
				
	-How to send AJAX request to this WEP API using JQUERY library
	dataString = "name="+name+"&username="+username+"&email="+email;
 		$.ajax({
       			 type: "POST",
        		 url: "https://localhost:7443/WebAPI/ForgetPassword",
       			 data: dataString,
       			 dataType: "json",
        		 success: function(result) {  
          			 //do something with the result json object
			},
        		error: function(jqXHR, textStatus){
           			//do something when error occures
        		},
        		beforeSend: function(settings){
           			settings.data += "&dummyData=whatever";
        		},
        		complete: function(){
          			//do something after AJAX request has completed    
        		}
		}); /*ends ajax here....*/ 
		
		
HOW TO CONFIGURE AND RUN THIS WEB API
======================================
1. Download and install JDK 1.8

2. Download and setup Apache Tomcat 9.0.0.M17

   2.1. Bind Apache Tomcat to SSL
   
   2.2. Create Tomcat users 
   
3. Download and install NetBeans IDE 8.2

4. Clone this WEB API project from the NETBeans IDE

5. Export Microsoft Active Directory server public key certificate 
   (Reference: https://qnatech.wordpress.com/2013/05/19/how-to-configure-ssl-on-active-directory/)
   
6. Create JKS type certificate for the Microsoft Active Directory public key certificate using Keytool command in JDK 
   (Reference: https://qnatech.wordpress.com/2013/05/19/how-to-configure-ssl-on-active-directory/)
   
7. Open Package for configureation Java classes: local.healthportal.ConfigClass

8. Change the contents of these three configuration Java classes
  
 	 8.1. AD_Admin_Config.java, change the details of Microsoft Active Directory server (IP addresss,domain name,admin credentials 		      and OU)
   
   	 8.2. DomainController_Certificate_Config.java, Change the path to the Microsoft Active Directory server JKS type certificate 		      for SSL connection with Active Directory server (Create in step 6)
   
   	 8.3. Email_Config.java, change the account and password to valid gmail account which will be used to send verification email 
   
9. Clean and Build the WEB API from NetBeans IDE

10. Deploy the WEB API to Tomacat app manager

11. WEB API is ready to be used 
