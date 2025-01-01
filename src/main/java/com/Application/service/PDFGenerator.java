package com.Application.service;

import com.Application.model.Account;
import com.Application.model.Address;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFGenerator {
	private static final Logger LOGGER = Logger.getLogger(PDFGenerator.class.getName());
	// Constructeur privé pour empêcher l'instanciation
    private PDFGenerator() {
        throw new UnsupportedOperationException("Cette classe ne peut pas être instanciée.");
    }

    public static String generateUserPdf(String name, String email, String idValue, Address address, Account account) {
        String filePath = "user_receipt.pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Reçu de création de compte"));
            document.add(new Paragraph("Nom: " + name));
            document.add(new Paragraph("Email: " + email));
            document.add(new Paragraph("CIN ou Passeport: " + idValue));
            document.add(new Paragraph("Adresse: " + address.getAddress()));
            document.add(new Paragraph("Ville: " + address.getCity()));
            document.add(new Paragraph("Code postal: " + address.getPostalCode()));
            document.add(new Paragraph("Pays: " + address.getCountry()));
            document.add(new Paragraph("Numéro de compte: " + account.getAccountNumber()));
            document.add(new Paragraph("Type de compte: " + account.getAccountType()));
            document.add(new Paragraph("Banque: " + account.getBank().getName()));
            document.add(new Paragraph("Code BIC: " + account.getBank().getBic()));

        } catch (DocumentException | IOException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la génération du reçu.", e);
            return null;
        } finally {
            document.close();
        }

        return filePath;
    }
}

