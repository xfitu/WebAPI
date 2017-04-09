
package local.healthportal.javaclass;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This custom Java class is used to send email to user's email address
 * using smtp.gmail.com mail server via ssl connection
 * @author Julio Vaz
 */
public class Email {
     
       private String username = "ximenesxfitu@gmail.com";
       private String password = "ximenes123";
       private String host= "smtp.gmail.com";
       //private int port=587; //port for plain text
       private int port=465;
       private boolean status=false;
         
    /**
     * Default constructor
     */
  public Email(){
    
   }
 
    /**
     * Constructor
     * @param SenderEmail
     * @param SenderPassword
     */
 public Email(String SenderEmail,String SenderPassword){
    this.username = SenderEmail;
    this.password = SenderPassword;
}

    /**
     * Constructor
     * @param SenderEmail
     * @param SenderPassword
     * @param Host
     * @param port
     */
  public Email(String SenderEmail,String SenderPassword,String Host,int port){
    this.username=SenderEmail;
    this.password=SenderPassword;
    this.host = Host;
    this.port=port;
   }
 
    /**
     * Method to send email to certain email address
     * @param ReceiverEmail
     * @param Subject
     * @param MessageContent
     */
    public void sendMail(String ReceiverEmail,String Subject,String MessageContent) { 
        Properties props = new Properties();
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.ssl.enable", "true");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", "465");//set port 465 for ssl
        props.put("mail.smtps.ssl.trust", "smtp.gmail.com");
       
 
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });
 
        try {
 
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.username));
            message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(ReceiverEmail));
            message.setSubject(Subject);
            message.setText(MessageContent);
 
            Transport transport = session.getTransport("smtps");
            transport.connect (this.host, this.port,this.username,this.password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();  
            System.out.println("Message sent successfully");
             this.status=true;
 
        } catch (MessagingException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            System.out.println("Error Message:"+e.getMessage());
            this.status=false;

        }
    }//ends method here.......
   
    /**
     * Method to check if email is sent successfully or not
     * @return true or false
     */
public boolean IsEmailSent(){
    if(this.status){
      return true;
    }
    else{
      return false; 
        }
    }//ends method here.....

}//ends class here......


