
package local.healthportal.javaclass;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * This custom Java class used to create LDAPS or LDAP connection with Domain Controller Server
 * and register new user object to Active Directory and search user object's attributes as 
 * well as add and delete user's attributes from Active Directory
 * @author Julio vaz
 */

    public class ActiveDirectory {
    
    private String DOMAIN_NAME = ""; //domain name
    private String DOMAIN_ROOT = "";//split domain fully qualified name ex: healthportal.local
    private String DOMAIN_URL = ""; //Domain Controller server IP Address with port number 389 for AD connection
    private String DOMAIN_SSL_URL = ""; //server URL for ssl connection
    private String DOMAIN_CONTROLLER_KEYSTORE_PATH ="";
    
    private String ADMIN_NAME = "";// Active Directory Administrator username 
    private String ADMIN_PASS = ""; //Active Directory Administrator password
    private String DOMAIN_ADMIN_LOGINNAME ="";
    private String OrganizationalUnit="";
   
    private  DirContext dirContext=null; //DirContext for AD connection
    private SearchControls searchcontrol=null;//searchControls for searching thru AD
    private NamingEnumeration enumeration=null;//NamingEnumeration for user's returned attributes
	
    /**
     * Default constructor with no parameter
     */
public ActiveDirectory(){
 
}

    /**
     * constructor to connect Active Directory with given username and password and without domain controller certificate path
     * @param username
     * @param password
     * @param domainname
     * @param domainIP
     */
public ActiveDirectory(String username,String password, String domainname,String domainIP){
        this.ADMIN_NAME=username;
        this.ADMIN_PASS=password;
        this.DOMAIN_URL = "ldap://"+domainIP+":389"; //Domain Controller URL for LDAP connection with AD with simple mode
        this.DOMAIN_SSL_URL = "ldaps://"+domainIP+":636"; //Domain Controller URL for LDAP connection with AD over SSL mode
        this.DOMAIN_ADMIN_LOGINNAME = this.ADMIN_NAME+"@"+domainname;//create user principal for login
        this.DOMAIN_NAME=domainname;
        this.DOMAIN_ROOT =getDomainRoot(domainname);
} //ends constructore here.......
    
    /**
     * constructor to connect Active Directory with given username and password with domain controller certificate path as well
     * @param username
     * @param password
     * @param domainname
     * @param domainIP
     * @param Domain_Controller_Certificate_Path
     */
public ActiveDirectory(String username,String password, String domainname,String domainIP,String Domain_Controller_Certificate_Path){
        this.ADMIN_NAME=username;
        this.ADMIN_PASS=password;
        this.DOMAIN_URL = "ldap://"+domainIP+":389"; //Domain Controller URL for LDAP connection with AD with simple mode
        this.DOMAIN_SSL_URL = "ldaps://"+domainIP+":636"; //Domain Controller URL for LDAP connection with AD over SSL mode
        this.DOMAIN_ADMIN_LOGINNAME = this.ADMIN_NAME+"@"+domainname;//create user principal for login
        this.DOMAIN_NAME=domainname;
        this.DOMAIN_ROOT =getDomainRoot(domainname);
        this.DOMAIN_CONTROLLER_KEYSTORE_PATH = Domain_Controller_Certificate_Path;
} //ends constructore here.......

    /**
     * constructor to connect Active Directory with given username and password with domain controller certificate path as well
     * @param username
     * @param password
     * @param domainname
     * @param domainIP
     * @param Domain_Controller_Certificate_Path
     * @param OU
     */
public ActiveDirectory(String username,String password, String domainname,String domainIP,String Domain_Controller_Certificate_Path,String OU){
        this.ADMIN_NAME=username;
        this.ADMIN_PASS=password;
        this.DOMAIN_URL = "ldap://"+domainIP+":389"; //Domain Controller URL for LDAP connection with AD with simple mode
        this.DOMAIN_SSL_URL = "ldaps://"+domainIP+":636"; //Domain Controller URL for LDAP connection with AD over SSL mode
        this.DOMAIN_ADMIN_LOGINNAME = this.ADMIN_NAME+"@"+domainname;//create user principal for login
        this.DOMAIN_NAME=domainname;
        this.DOMAIN_ROOT=getDomainRoot(domainname);
        this.DOMAIN_CONTROLLER_KEYSTORE_PATH = Domain_Controller_Certificate_Path;
        this.OrganizationalUnit = OU;
} //ends constructore here.......
   
     /**
     * Method to create Domain Root based given domain name ex:mydomain.com to DC=mydomain,DC=com
     * @param domainname ex:AD-portal.local
     * @return domain root ex:DC=AD-portal,DC=local 
     */
private String getDomainRoot(String domainname){
  String domain[] = domainname.split("\\.");
  String domainroot="";
  for(int index=0; index<domain.length; index++){
    if(index==domain.length-1) {  
        domainroot = domainroot+"DC="+domain[index];
    }   
    else{ domainroot = domainroot+"DC="+domain[index]+",";}
  }
        return domainroot;
}//ends method here...............
        
    /**
     * Method to make a simple connection or LDAP to active directory  
     * @return boolean true or false
     */
public boolean SimpleConnectToActiveDirectory() {	
	boolean connectionstatus=false;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); // simple mode connectin with AD
        env.put(Context.SECURITY_PRINCIPAL, this.DOMAIN_ADMIN_LOGINNAME);
        env.put(Context.SECURITY_CREDENTIALS, this.ADMIN_PASS);
        env.put(Context.PROVIDER_URL, this.DOMAIN_URL);
         env.put("com.sun.jndi.ldap.connect.pool", "false");
        env.put("com.sun.jndi.ldap.connect.timeout", "300000");
try {
        this.dirContext = new InitialDirContext(env);
        System.out.println("Connection to AD is successful");
        connectionstatus = true;  
}catch (NamingException e) {
        System.out.println("Connection to AD is failed");
        e.printStackTrace();
        return connectionstatus;
}
     return connectionstatus;
}// ends method here.....
    
    /**
     * Method to make SSL connection or LDAPS connection with Active Directory
     * @return boolean true or false
     */
public boolean SSLConnectToActiveDirectory(){
        boolean connectionstatus=false;
        System.setProperty("javax.net.ssl.trustStore",this.DOMAIN_CONTROLLER_KEYSTORE_PATH);
        //Hashtable env = new Hashtable();
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_PROTOCOL,"ssl"); // use SSL security protocol to make connection with AD
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, this.DOMAIN_ADMIN_LOGINNAME);
        env.put(Context.SECURITY_CREDENTIALS, this.ADMIN_PASS);
        env.put(Context.PROVIDER_URL, this.DOMAIN_SSL_URL);
        env.put("com.sun.jndi.ldap.connect.pool", "false");
        env.put("com.sun.jndi.ldap.connect.timeout", "300000");
try {
        this.dirContext = new InitialDirContext(env);
        System.out.println("Connection to AD is successful");
        connectionstatus = true;
}catch(NamingException e) {
   e.printStackTrace();
   System.out.println("cannot connect to active directory server");
   return connectionstatus;
}
  return connectionstatus;
}// ends method here......
    
    /**
     * Method to make SSL connection or LDAPS connection with Active Directory
     * @param Domain_Controller_Certificate_Path
     * @return boolean true or false
     */
public boolean SSLConnectToActiveDirectory(String Domain_Controller_Certificate_Path){
       boolean connectionstatus=false;
       this.DOMAIN_CONTROLLER_KEYSTORE_PATH = Domain_Controller_Certificate_Path;
       System.setProperty("javax.net.ssl.trustStore",this.DOMAIN_CONTROLLER_KEYSTORE_PATH);
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_PROTOCOL,"ssl"); // use SSL security protocol to make connection with AD
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, this.DOMAIN_ADMIN_LOGINNAME);
        env.put(Context.SECURITY_CREDENTIALS, this.ADMIN_PASS);
        env.put(Context.PROVIDER_URL, this.DOMAIN_SSL_URL);
        env.put("com.sun.jndi.ldap.connect.pool", "false");
        env.put("com.sun.jndi.ldap.connect.timeout", "300000");
try {
            this.dirContext = new InitialDirContext(env);
            System.out.println("Connection to AD is successful");
            connectionstatus = true;
}catch(NamingException e) {
    e.printStackTrace();
    System.out.println("Cannot connect to Active Directory Server");
    return connectionstatus;
}
    return connectionstatus;
}// ends method here......

    /**
     * This method is used for user login test 
     * @param username
     * @param password
     * @param domainname
     * @param serverIP
     * @return boolean true or false
     */
public boolean LoginToActiveDirectory(String username,String password,String domainname,String serverIP){	
	boolean connectionstatus=false;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); // simple mode connectin with AD
        env.put(Context.SECURITY_PRINCIPAL, username+"@"+domainname);//user's principal name
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.PROVIDER_URL, "ldap://"+serverIP+":389");
        env.put("com.sun.jndi.ldap.connect.pool", "false");
        env.put("com.sun.jndi.ldap.connect.timeout", "300000");      
try{
        this.dirContext = new InitialDirContext(env);
        System.out.println("Connection to AD is successful");
        connectionstatus = true;
}catch (NamingException e) {
        System.err.println("Connection to AD is failed ");
        e.printStackTrace();
        return connectionstatus;
}
    return connectionstatus;
}// ends method here.....        

    /**
     * Method to enable an user's account in Active Directory
     * @param name
     * @param OrganizationalUnitName
     * @param UserAccountControlFlag
     * @return true or false
     */
public boolean enableUser(String name,String OrganizationalUnitName,int UserAccountControlFlag){
     int userAccountControlValue = UserAccountControlFlag;// value: 512
     String dn =  getUserDN(name,OrganizationalUnitName);
try{
    ModificationItem[] mods = new ModificationItem[1];
    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",""+userAccountControlValue));
    this.dirContext.modifyAttributes(dn, mods);
    System.out.println("User's account is enabled");
    return true;
}catch(NamingException e){
      System.out.println("cannot enable user's account");
      e.printStackTrace();
      System.err.println();
      return false;
}
}//ends method here.....

    /**
     * Method to get user account control flag if user exists otherwise returns zero
     * @param name
     * @param OrganizationalUnitName
     * @return 0 or 512 or 544 or 514
     */
public int IsUserExist(String name,String OrganizationalUnitName){
    int flag = 0;
    String userdn = getUserDN(name,OrganizationalUnitName);// get user distinguished name ex: CN=name,OU=myorganization,DC=domainname,DC=com
try{    
    String temp = this.dirContext.getAttributes(userdn).get("userAccountControl").get().toString();
    flag = Integer.parseInt(temp);
}catch(NamingException e){
      System.out.println("User does not exist");
      e.printStackTrace();
      System.err.println();
      return flag;// return flag zero if user with given userdn does not exist in the AD
}
     return flag;// return flag value of the given userdn
}//ends method here.....    

    /**
     * method to check whether a name is already exist in the active directory
     * @param name
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean IsNameExist(String name,String OrganizationalUnitName){
    boolean flag=false ;
    String userdn = getUserDN(name,OrganizationalUnitName);// get user distinguished name ex: CN=name,OU=myorganization,DC=domainname,DC=com
try{    
     String temp = this.dirContext.getAttributes(userdn).get("cn").get().toString();
     if(!temp.equalsIgnoreCase("No attributes")){
       flag = true;
     }
}catch(NamingException e){
 return flag;
}
     return flag;
}//ends method here.....    
    
    /**
     * Method to disable an user's account in Active Directory
     * @param name
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean DisableUser(String name,String OrganizationalUnitName){
    String dn =  getUserDN(name,OrganizationalUnitName);
try{     
    int userAccountControlValue = 514; //to disable user's account set its useraccountcontrol value to 514
    ModificationItem[] mods = new ModificationItem[1];
    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",""+userAccountControlValue));
    this.dirContext.modifyAttributes(dn, mods);
    System.out.println("User's account is enabled");
    return true;
}catch(NamingException e){
      System.out.println("cannot enable user's account");
      e.printStackTrace();
      return false;
     }
}//ends method here.....    

    /**
     * Method to update user password in Active Directory
     * To Update user's password it is required LDAPS connection 
     * @param name
     * @param password
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean UpdatePassword(String name, String password,String OrganizationalUnitName){       
    boolean check=false;
    String BASE_NAME;
            if(OrganizationalUnitName.equalsIgnoreCase("null")){
            
                BASE_NAME = getUserDN(name,"users");
            }
            else{
             
                BASE_NAME = getUserDN(name,OrganizationalUnitName);
            }
            byte pwdArray[] = EncodePassword(password); //encode user's password
try{
	    ModificationItem[] mods = new ModificationItem[1];
	    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("UnicodePwd", pwdArray));
	    this.dirContext.modifyAttributes(BASE_NAME, mods);
            check=true;
            System.out.println("user password updated!");
}catch (NamingException |NullPointerException e){ 
	e.printStackTrace();
        System.out.println("update password error: " + e);
        return check;
}
        return check;
}//method ends here......
    
    /**
     * method to update certain user's attribute 
     * @param name
     * @param attributeName
     * @param attributeValue
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean UpdateUserAttribute(String name, String attributeName, String attributeValue,String OrganizationalUnitName){   
    boolean check=false; 
    String BASE_NAME;
        if(OrganizationalUnitName.equalsIgnoreCase("null")){
          BASE_NAME = getUserDN(name,"users");
        }
        else{
           BASE_NAME = getUserDN(name,OrganizationalUnitName);
        }
try{ 
	    System.out.println("updating " + name +"\n");
	    ModificationItem[] mods = new ModificationItem[1];
	    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attributeName, attributeValue));
	    this.dirContext.modifyAttributes(BASE_NAME, mods);
            check=true;
            System.out.println("Your "+attributeName+ "is updated to "+ attributeValue);
}catch (NamingException e){  
            e.printStackTrace();
	    System.out.println(" update error: " + e);
	    return check;
}
    return check;    
         
}//method ends here......

    /**
     * Method to add new user attribute to Active Directory  
     * @param name
     * @param NewAttributeName
     * @param NewAttributeValue
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean AddNewAttribute(String name, String NewAttributeName, String NewAttributeValue,String OrganizationalUnitName){   
  boolean check=false; 
  String BASE_NAME;
        if(OrganizationalUnitName.equalsIgnoreCase("null")){
          BASE_NAME = getUserDN(name,"users");
        }
        else{
          BASE_NAME = getUserDN(name,OrganizationalUnitName);
        }
try{
	    ModificationItem[] mods = new ModificationItem[1];
	    mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(NewAttributeName, NewAttributeValue));
	    this.dirContext.modifyAttributes(BASE_NAME, mods);
            check=true;
            System.out.println(NewAttributeName+ "is added "+ NewAttributeValue);
}catch (NamingException e){  
            e.printStackTrace();
	    System.out.println(" add new attribute error: " + e);
	    return check;
}
    return check;    
         
}//method ends here...... 

    /**
     * method to remove or delete a certain user's attribute in from Active Directory
     * @param name
     * @param AttributeName
     * @param AttributeValue
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean RemoveAttribute(String name, String AttributeName, String AttributeValue,String OrganizationalUnitName){   
    boolean check=false; 
    String BASE_NAME;
        if(OrganizationalUnitName.equalsIgnoreCase("null")){
            BASE_NAME = getUserDN(name,"users");
        }
        else{
            BASE_NAME = getUserDN(name,OrganizationalUnitName);
        }
try{ 
	    ModificationItem[] mods = new ModificationItem[1];
	    mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(AttributeName, AttributeValue));
	    this.dirContext.modifyAttributes(BASE_NAME, mods);
            check=true;
            System.out.println("Your "+AttributeName+ "with value: " + AttributeValue +" is already removed");
}catch (NamingException e){  
            e.printStackTrace();
	    System.out.println(" remove attribute error: " + e);
	    return check;
}
    return check;    
         
}//method ends here......  

    /**
     * method to update certain user's username value in Active Directory
     * @param name
     * @param NewUsername
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean UpdateUsername(String name, String NewUsername,String OrganizationalUnitName){   
    boolean check=false; 
    String BASE_NAME;
        if(OrganizationalUnitName.equalsIgnoreCase("null")){
            BASE_NAME = getUserDN(name,"users");
        }
        else{
            BASE_NAME = getUserDN(name,OrganizationalUnitName);
        }
        String sAMAccountName = NewUsername;
        String userPrincipalName = NewUsername+"@"+this.DOMAIN_NAME;
        String uid = NewUsername;
try{ 
	 
            System.out.println("updating " + NewUsername +"\n");
	    ModificationItem[] mods = new ModificationItem[3];
	    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("sAMAccountName",sAMAccountName ));
	    mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPrincipalName",userPrincipalName ));
            mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("uid",uid));

            this.dirContext.modifyAttributes(BASE_NAME, mods);
            check=true;
            System.out.println("Your username is updated to:"+NewUsername);
}catch (NamingException e){  
            e.printStackTrace();
	    System.out.println(" update error: " + e);
	    return check;
}
    return check;    
         
}//method ends here......

    /**
     * method to update user's name value in Active Directory 
     * @param name
     * @param firstname
     * @param lastname
     * @param OrganizationalUnitName
     * @return true or false
     */
public boolean UpdateName(String name, String firstname, String lastname,String OrganizationalUnitName){   
    boolean check=false; 
    String BASE_NAME;
    String userOldDn,userNewDn;
    String userNewName = new StringBuffer(firstname).append(" ").append(lastname).toString();
        if(OrganizationalUnitName.equalsIgnoreCase("null")){
            BASE_NAME = getUserDN(name,"users");
            userOldDn = getUserDN(name,"users");
            userNewDn = getUserDN(userNewName,"users");
        }
        else{
            BASE_NAME = getUserDN(name,OrganizationalUnitName);
            userOldDn = getUserDN(name,OrganizationalUnitName);
            userNewDn = getUserDN(userNewName,OrganizationalUnitName);
        }
try{ 
            String cnValue = userNewName;
            System.out.println("updating " + name +"\n");
            ModificationItem[] mods = new ModificationItem[3];
            //update user's lastname or sn
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("sn", lastname));
            //update user's first name or givenName
            mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("givenName", firstname));
            //update user's display name 
            mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("displayName", cnValue));
	    this.dirContext.modifyAttributes(BASE_NAME, mods);
            //rename user's common name or distinguishedname
            this.dirContext.rename(userOldDn, userNewDn);
            check=true;
            System.out.println("Your name is already updated to "+cnValue);
            
}catch (NamingException|NullPointerException e){  
            e.printStackTrace();
	    System.out.println("update error: " + e);
	    return check;
}
    return check;    
        
}//method ends here......
 
    /**
     * Method to get all user's attributes which have values from Active Directory by user's full name
     * @param Name
     * @param OrganizationalUnitName
     * @return HashMap of attributes and values as key pairs
     */
public HashMap<String,String> GetUserAttributes(String Name,String OrganizationalUnitName){
    HashMap<String,String> eachattribute = new HashMap();
    String dN;
    //if OU is not given then the search will be done on the main container
    if(OrganizationalUnitName.equalsIgnoreCase("null")){
      dN = getUserDN(Name,"users");
    }
    else{ //Otherwise the search will be done on the given OU container
        dN = getUserDN(Name,OrganizationalUnitName);
    }
try{
    Attributes answer = this.dirContext.getAttributes(dN);
    for (NamingEnumeration<?> ae = answer.getAll(); ae.hasMore();) {
    Attribute attr = (Attribute) ae.next();
    String attributeName = attr.getID();
    String value="";
    for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();) {
           if(attributeName.equalsIgnoreCase("memberOf")){ 
                 value = value+e.next().toString()+",";    
                 //System.out.println(attributeName + ":" +value);
           }else{ 
                value = value+e.next().toString();
                //System.out.println(attributeName + "=" +value);
           }
           
        
    }
        eachattribute.put(attributeName, value);//add each attribute name and value to map
    }
}catch (NamingException e){
	e.printStackTrace();
        System.out.println("user is not exist");
        return eachattribute;
}
    return eachattribute;
}//ends method here......

    /**
     * method to add an user to a certain group in Active Directory
     * @param Name
     * @param groupName
     * @param organizationalUnitName
     * @return true or false
     */
public boolean AddToGroup(String Name,String groupName,String organizationalUnitName){
        boolean check = false;
        String userDn ;
        String groupDn ;
    //if OU is not given then the search will be done on the main container
    if(organizationalUnitName.equalsIgnoreCase("null")){
       userDn = getUserDN(Name,"users");
       groupDn = getUserDN(groupName,"users");
    }
    else{ //Otherwise the search will be done on the given OU container
        userDn = getUserDN(Name,organizationalUnitName);
        groupDn = getUserDN(groupName,organizationalUnitName);
    }
        
try {
       ModificationItem mods[] = new ModificationItem[1];
       mods[0]= new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", userDn));
       this.dirContext.modifyAttributes(groupDn,mods);
       System.out.println(Name+" is already a member of "+groupName+" group");
       check=true;
}catch(NamingException e) {
       e.printStackTrace();
       System.err.println("Problem adding member: " + e);
       return check;
}
return check;
}// ends method here......

    /**
     * Method to remove a user from a group in Active Directory
     * @param Name
     * @param groupName
     * @param organizationalUnitName
     * @return true or false
     */
public boolean RemoveUserFromGroup(String Name,String groupName,String organizationalUnitName){
    boolean check = false;
    String userDn,groupDn;    
    //if OU is not given then the search will be done on the main container
    if(organizationalUnitName.equalsIgnoreCase("null")){
       userDn = getUserDN(Name,"users");
       groupDn = getUserDN(groupName,"users");
    }
    else{ //Otherwise the search will be done on the given OU container
        userDn = getUserDN(Name,organizationalUnitName);
        groupDn = getUserDN(groupName,organizationalUnitName);
    }

try {
       ModificationItem mods[] = new ModificationItem[1];
       mods[0]= new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("member", userDn));
       this.dirContext.modifyAttributes(groupDn,mods);
       System.out.println(Name+" is already removed from "+groupName+" group");
       check=true;
}catch(NamingException e) {
	  System.err.println("Problem removing user from group: " + e);
         return check;
 }
        return check;
}// ends method here......

    /**
     * Method to create a new user object to an organizational unit in Active Directory
     * @param firstName
     * @param lastName
     * @param Email
     * @param userName
     * @param password
     * @param organisationUnit
     * @return true or false
     */
public boolean addUser(String firstName,String lastName,String Email,String userName,String password,String organisationUnit){
        Attributes container = new BasicAttributes();
        // Create the objectclass to add
        Attribute objClasses = new BasicAttribute("objectClass");
        objClasses.add("top");
        objClasses.add("person");
        objClasses.add("organizationalPerson");
        objClasses.add("user");
        // Assign the username, first name, and last name
        String cnValue = new StringBuffer(firstName).append(" ").append(lastName).toString();
        Attribute cn = new BasicAttribute("cn", cnValue);
        Attribute sAMAccountName = new BasicAttribute("sAMAccountName", userName);
        Attribute principalName = new BasicAttribute("userPrincipalName", userName + "@" + this.DOMAIN_NAME);
        Attribute givenName = new BasicAttribute("givenName", firstName);
        Attribute sn = new BasicAttribute("sn", lastName);
        Attribute uid = new BasicAttribute("uid", userName);
        Attribute displayname = new BasicAttribute("displayname",firstName+" "+lastName);
        Attribute email = new BasicAttribute("mail",Email); 
        byte[]UnicodePassword = EncodePassword(password);//encode the user password
        Attribute pass = new BasicAttribute("unicodePwd",UnicodePassword);
        // Add these to the container
        container.put(objClasses);
        container.put(sAMAccountName);
        container.put(principalName);
        container.put(cn);
        container.put(sn);
        container.put(givenName);
        container.put(uid);
        container.put(displayname);
        container.put(email);
        container.put(pass);
try {
            this.dirContext.createSubcontext(getUserDN(cnValue, organisationUnit), container);
            System.out.println("successfully add user to active directory");
            return true;
} catch (NamingException e) {
            System.out.println("There is an error, cannot add new user");
            e.printStackTrace();
            return false;
}
}//ends method here......

    /**
     * method to delete an user object from active directory
     * @param name
     * @param OU
     * @return true or false
     */
public boolean DeleteUser(String name, String OU){
    String userDn;
    boolean check=false;
    //check if user located at users container 
    if(OU.equalsIgnoreCase("users")){
      userDn = getUserDN(name,"users");
    }else{
      userDn = getUserDN(name,OU);  
    }
try{
    this.dirContext.destroySubcontext(userDn);
    System.out.println("user is successfully deleted");
    check = true;
}catch (NamingException e) {
   e.printStackTrace();
   System.out.println("Error,cannot delete the user");
   return check;
}
        return check;
}//ends method here......................


    /**
     * method to split user's attribute name from attribute value
     * @param value
     * @return attribute:value as a string
     */
private String Spliter(String value){
   String temp[] = value.split(":");
   String attributename = temp[0].substring(temp[0].indexOf("=")+1);
   String attributevalue = temp[1].substring(1, temp[1].indexOf("}"));
   String result=attributename+":"+attributevalue;
   return result;
}//ends method here..........

    /**
     * Method to retrieve a single user's attribute from Active Directory
     * @param username
     * @param attributename
     * @param OU
     * @return attribute with its value as string
     */
public String GetOneAttribute(String username,String attributename,String OU){
   String value="attribute:No attributes"; 
   String searchBase; 
    if(OU.equalsIgnoreCase("null")){
      searchBase = "CN=users,"+this.DOMAIN_ROOT;   
    }
    else{ //Otherwise the search will be done on the given OU container
      searchBase = "OU="+OU+","+this.DOMAIN_ROOT;  
    }          
    this.searchcontrol = new SearchControls();
    String returnedAtts[];
    returnedAtts = new String[1];
    returnedAtts[0]=attributename;
    this.searchcontrol.setReturningAttributes(returnedAtts);
    this.searchcontrol.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String searchFilter = "(&(objectClass=user)(sAMAccountName=" +username+ "))";
try{            
    this.enumeration = this.dirContext.search(searchBase, searchFilter, this.searchcontrol);
    while (this.enumeration.hasMoreElements()){
       SearchResult sr = (SearchResult)this.enumeration.next();
       if(sr.getAttributes().toString().equalsIgnoreCase("No attributes")){
         value="attribute:No attributes";
       }else{//otherwise attribute has value
         value = Spliter(sr.getAttributes().toString());
        }                
    }//ends while here..................
        
}catch (NamingException | NullPointerException e) {
    System.out.println("User attribute value is empty");
    return value;// return e.g attributename:attributevalue
}
 
return value;

}//ends method here..........................

    /**
     * Method to retrieve user's attributes from Active Directory by user's account name or username
     * @param username
     * @param OU
     * @return HashMap attributes names and values
     */
 public HashMap<String,String> FetchAttributes(String username,String OU){
   HashMap<String,String> result = new HashMap(); 
   String value; 
   String searchBase; 
    if(OU.equalsIgnoreCase("null")){
      searchBase = "CN=users,"+this.DOMAIN_ROOT;   
    }
    else{ //Otherwise the search will be done on the given OU container
      searchBase = "OU="+OU+","+this.DOMAIN_ROOT;  
    }          
    this.searchcontrol = new SearchControls();
    //array of attributes name to be fetched from active directory
    String[]attributesID = {"objectCategory","mail","memberOf","instanceType","st","objectClass","company","name","description","sn",
        "telephoneNumber","userAccountControl","primaryGroupID","postalcode","physicalDeliveryOfficeName",
        "co","cn","title","mobile","sAMAccountType","givenName","displayName","userPrincipalName","department","streetAddress", 
        "countryCode","l","distinguishedName","c","sAMAccountName","postOfficeBox"};

    this.searchcontrol.setReturningAttributes(attributesID);
    this.searchcontrol.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String searchFilter = "(&(objectClass=user)(sAMAccountName=" +username+ "))";

   try{            
    this.enumeration = this.dirContext.search(searchBase, searchFilter, this.searchcontrol);
    SearchResult sr = (SearchResult)this.enumeration.next();
    Attributes answer = sr.getAttributes();
    for (NamingEnumeration<?> ae = answer.getAll(); ae.hasMore();) {
    Attribute attr = (Attribute) ae.next();
    String attributeName = attr.getID();
    if(attributeName.equalsIgnoreCase("memberOf")){ 
        value="";
        //user is member of more than one groups in active directory
        for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();) {
                 value = value+e.next().toString()+",";    
        }
    }else{ 
        value = attr.get().toString();
    }
     //System.out.println(attributeName+":"+value);
     result.put(attributeName, value);
  }
 
}catch (NamingException | NullPointerException e) {
    e.printStackTrace();
    return result;
}
 
return result;

}//ends method here..........................

    /**
     * method to check whether a certain username already exist in active directory
     * @param username
     * @param OU
     * @return true or false
     */
public boolean IsUsernameExist(String username,String OU){
   boolean value=false; 
   String searchBase; 
    if(OU.equalsIgnoreCase("null")){
      searchBase = "CN=users,"+this.DOMAIN_ROOT;   
    }
    else{ //Otherwise the search will be done on the given OU container
      searchBase = "OU="+OU+","+this.DOMAIN_ROOT;  
    }          
    this.searchcontrol = new SearchControls();
    String returnedAtts[];
    returnedAtts = new String[1];
    returnedAtts[0]="cn";
    this.searchcontrol.setReturningAttributes(returnedAtts);
    this.searchcontrol.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String searchFilter = "(&(objectClass=user)(sAMAccountName=" +username+ "))";
try{            
    this.enumeration = this.dirContext.search(searchBase, searchFilter, this.searchcontrol);
    //Loop through the search results
    while (this.enumeration.hasMoreElements()) 
    {
       SearchResult sr = (SearchResult)this.enumeration.next();
        String temp = sr.getAttributes().toString();
       if(!temp.equalsIgnoreCase("No attributes")){
         value=true;
       }              
    }//ends while here..................
        
}catch (NamingException e) {
    return value;
}
 
return value;

}//ends method here..........................

    /**
     * Method to encoded password into an uniCode Password
     * @param password
     * @return byte uniCode encoded  password
     */
private byte[] EncodePassword(String password){
    String temp = "\"" + password + "\"";
    byte[]UnicodePassword=null;
try{
        UnicodePassword = temp.getBytes("UTF-16LE");
}catch(UnsupportedEncodingException e) {
   System.out.println("Problem encoding password: " + e);
   e.printStackTrace();
}
 return UnicodePassword;//return the encoded password
}// ends method here........ 
     
    /**
     * Method to create a distinguished name with domain root 
     * @param ausername
     * @param aOU
     * @return Distinguished name: CN=test user,OU=portal,DC=AD-portal,DC=local
     */
private String getUserDN(String aUsername, String aOU){
    String result;
    //check if user located at users container 
    if(aOU.equalsIgnoreCase("users")){
         result = "cn=" + aUsername + ",cn=" + aOU + "," + this.DOMAIN_ROOT;
    }else{
         result = "cn=" + aUsername + ",ou=" + aOU + "," + this.DOMAIN_ROOT;
    }
  return result;
}//ends method here......................

    /**
     * Method to close dirContext and enumeration connection to Domain Controller Server
     */
public void CloseConnection(){
try {
        if(this.dirContext != null){
            this.dirContext.close();
            System.out.println("Connection to AD is already closed!");
        }
} catch (NamingException e) {
  System.out.println("Cannot close AD connection!,since it is null");
}
try{ 
        if(this.enumeration!=null){
            this.enumeration.close();
            System.out.println("enumeration is already closed!");
        }
}catch (NamingException e) {
System.out.println("Cannot close enumeration!,since it is null");
}
} //ends method here.......
    
}/* ...............................................ends class here.......................................*/


