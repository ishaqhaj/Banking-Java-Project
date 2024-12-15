package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CstmrCdtTrfInitn")
public class CstmrCdtTrfInitn {

    private GroupHeader groupHeader;
    private List<PaymentInformation> paymentInformation = new ArrayList<>();

    @XmlElement(name = "GrpHdr")
    public GroupHeader getGroupHeader() {
        return groupHeader;
    }

    public void setGroupHeader(GroupHeader groupHeader) {
        this.groupHeader = groupHeader;
    }

    @XmlElement(name = "PmtInf")
    public List<PaymentInformation> getPaymentInformation() {
        return paymentInformation;
    }

    public void addPaymentInformation(PaymentInformation paymentInformation) {
        this.paymentInformation.add(paymentInformation);
    }
}
