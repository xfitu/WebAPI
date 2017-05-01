
package local.healthportal.ConfigClass;

/**
 * This is configuration class for Domain Controller Administrator account and password
 * which is used to create new user in Active Directory on Domain Controller server
 * as well as server IP address,domain name,ou, and ports
 * NOTE: must be an admin account to be able to create user in Active Directory
 * @author Julio Vaz
 */
public class AD_Admin_Config {
public final static String ADMIN_USERNAME ="vaz"; //Active Directory admin user name
public final static String ADMIN_PASSWORD ="Kotonurak123456"; //Active Directory admin password 
public final static String DOMAIN_NAME ="AD-portal.local"; //Domain controller name
public final static String SERVER_IP ="10.10.10.10"; //Domain controller server IP address
public final static String OU = "portal"; //Organizational Unit in active directory which user will be added to
public final static String AD_NORMAL_PORT="389";//port that used to connect to Active Directory in normal mode or non SSL mode
public final static String AD_SSL_PORT="636"; //port that used to connect to Active Directory in SSL mode  

}
