package com.Application.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;

import java.io.File;
import java.util.Properties;

public class EmailService {

    public void sendEmail(String toEmail, String uniqueId, String pdfPath) {
        String from = "khalidlakbir5@gmail.com";
        String password = "ccmgfokjfhzfjwjh";       // Remplacez par votre mot de passe d'application Gmail

        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Création de la session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Votre identifiant unique avec reçu");

            // Partie texte de l'email
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Bonjour,\n\nVotre identifiant unique est : " + uniqueId +
                    "\n\nVeuillez trouver ci-joint le reçu de création de compte.\n\nCordialement.");

            // Partie pièce jointe
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pdfPath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName("Recu_inscription.pdf");

            // Combinez les parties dans un multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            // Ajout du contenu multipart au message
            message.setContent(multipart);

            // Envoi du message
            Transport.send(message);
            System.out.println("Email envoyé avec succès à : " + toEmail);

            // Suppression du fichier PDF après envoi
            File pdfFile = new File(pdfPath);
            if (pdfFile.delete()) {
                System.out.println("Le fichier PDF a été supprimé avec succès.");
            } else {
                System.out.println("Échec de la suppression du fichier PDF.");
            }

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Échec de l'envoi de l'email.");
        }
    }
}
