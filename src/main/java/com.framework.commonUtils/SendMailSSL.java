package com.framework.commonUtils;

import org.apache.log4j.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

import static com.framework.commonUtils.Constant.GmailPassword;
import static com.framework.commonUtils.Constant.GmailUser;

public class SendMailSSL {
    private final static Logger logger = Logger.getLogger(SendMailSSL.class);

    public static Session createConnection() throws MessagingException {
        // Create IMAPSSLStore object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // All sysout statements are used for testing, have to remove them
        // while implementation
        System.out.println("Connecting to gmail...");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GmailUser, GmailPassword);// change
                // accordingly
            }
        });
        return session;
    }

    /*
     * public static IMAPSSLStore createConnection() throws MessagingException { //
     * Create IMAPSSLStore object Properties props = System.getProperties();
     * props.setProperty("mail.store.protocol", "imaps");
     *
     * Session session = Session.getDefaultInstance(props, new
     * javax.mail.Authenticator() { protected PasswordAuthentication
     * getPasswordAuthentication() { return new
     * PasswordAuthentication(Constant.GmailUser, Constant.GmailPassword);// change
     * // accordingly } }); URLName urlName = new URLName("imap.gmail.com");
     * IMAPSSLStore store = new IMAPSSLStore(session, urlName);
     *
     * // All sysout statements are used for testing, have to remove them // while
     * implementation System.out.println("Connecting to gmail...");
     *
     * // Connect to GMail, enter user name and password here
     * store.connect("imap.gmail.com", Constant.GmailUser, Constant.GmailPassword);
     *
     * System.out.println("Connected to - " + store);
     *
     * sendMailAction(session, Constant.GmailUser, Constant.GmailPassword);
     *
     * System.out.println("send mail success"); return store; }
     */

    public static void sendMailAction(String PurveyorName, String RestaurantName, String fileType) {
		String to = System.getProperty("email");
        String user = Constant.sendMailFrom;// change
        // accordingly
        try {
            // get connection
            Session session = createConnection();
            // String filepath = RandomAction.setdownloadDir();
            File GFS_OG = RandomAction.getLatestFilefromDir("/var/jenkins_home/workspace/SyscoShop/", fileType);
            String filename = GFS_OG.getAbsolutePath();
            System.out.println(filename);

            MimeMessage message = new MimeMessage(session);
            // message.setFrom(new InternetAddress(user));
            // message.addRecipient(Message.RecipientType.TO, new
            // InternetAddress(to));
            // message.setSubject("Message Alert");

            MimeMessage messageBodyPart1 = new MimeMessage(session);
            messageBodyPart1.setFrom(new InternetAddress(user));// change
            // accordingly
            messageBodyPart1.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // smessageBodyPart1.addRecipient(Message.RecipientType.CC, new
            // InternetAddress("teamesave@gmail.com"));

            // Subject of mails
            message.setSubject("OnLineMacro :: " + PurveyorName + " :: " + RestaurantName);
            // Body of mails
            // message.setContent("Cheney OG export- date & time : " +
            // RandomAction.getDate(), "text");

            // message.setText();

            // 4) create new MimeBodyPart object and set DataHandler object to
            // this object
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(filename);

            // 5) create Multipart object and add MimeBodyPart objects to this
            // object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart2);

            // 6) set the multiplart object to the message object
            message.setContent(multipart);

            // send message
            /*
             * Transport transport = session.getTransport("smtp");
             * transport.connect(Constant.GmailURL,Constant.GmailUser ,
             * Constant.GmailPassword); transport.sendMessage(message,
             * messageBodyPart1.getAllRecipients());
             */
            Transport.send(message, messageBodyPart1.getAllRecipients());

            System.out.println("Message send success for " + RestaurantName);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendReports(String Subject, String... filenames) {
        try {
            // Properties Constant = new Properties();
            // Constant.load(new FileInputStream("Config.properties"));
            // String to = Constant.getProperty("reportTo");
            // String user = Constant.getProperty("sendMailFrom");// change
            // // accordingly
            String user = Constant.sendMailFrom;
            String[] to = Constant.reportTo;
            String cc = Constant.cc;
            // get connection
            Session session = createConnection();

            MimeMessage message = new MimeMessage(session);

            MimeMessage messageBodyPart1 = new MimeMessage(session);
            messageBodyPart1.setFrom(new InternetAddress(user));// change
            // accordingly

            InternetAddress[] recipientAddress = new InternetAddress[to.length];
            int counter = 0;
            for (String recipient : to) {
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }

            messageBodyPart1.addRecipients(Message.RecipientType.TO, recipientAddress);
            messageBodyPart1.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));

            // Subject of mails
            message.setSubject(Subject);
            // Body of mails
            String date = RandomAction.getDate();
            message.setContent("Attached is the report for the OG export on : " + date, "text");

            // message.setText();

            // 4) create new MimeBodyPart object and set DataHandler object to
            // this object
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            for (String filename : filenames) {
                DataSource source = new FileDataSource(filename);
                messageBodyPart2.setDataHandler(new DataHandler(source));
                messageBodyPart2.setFileName(filename);
                logger.info("Attached file - " + filename);

            }
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart2);

            message.setContent(multipart);

            Transport.send(message, messageBodyPart1.getAllRecipients());

            logger.info("Message send success");

        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info("Technical issue in sending reporting");
        }

    }

}
