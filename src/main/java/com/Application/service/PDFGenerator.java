package main.java.com.Application.service;

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
        return filePath;
    }
}

