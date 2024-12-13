package com.Application.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class PDFGenerator {

    public static String generateUserPdf(String name, String email, String idValue, String address, String city,
                                         String postalCode, String country, String accountNumber,
                                         String accountType, String bank, String bic) {
        String filePath = "user_receipt.pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Reçu de création de compte"));
            document.add(new Paragraph("Nom: " + name));
            document.add(new Paragraph("Email: " + email));
            document.add(new Paragraph("CIN ou Passeport: " + idValue));
            document.add(new Paragraph("Adresse: " + address));
            document.add(new Paragraph("Ville: " + city));
            document.add(new Paragraph("Code postal: " + postalCode));
            document.add(new Paragraph("Pays: " + country));
            document.add(new Paragraph("Numéro de compte: " + accountNumber));
            document.add(new Paragraph("Type de compte: " + accountType));
            document.add(new Paragraph("Banque: " + bank));
            document.add(new Paragraph("Code BIC: " + bic));

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            document.close();
        }

        return filePath;
    }
}

