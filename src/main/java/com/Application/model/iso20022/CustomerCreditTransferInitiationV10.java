package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CustomerCreditTransferInitiationV10")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerCreditTransferInitiationV10 {

    @XmlElement(name = "GroupHeader")
    private GroupHeader groupHeader;

    @XmlElement(name = "PaymentInformation")
    private List<PaymentInformation> paymentInformation = new ArrayList<>();

    // Getters and setters
    public GroupHeader getGroupHeader() {
        return groupHeader;
    }

    public void setGroupHeader(GroupHeader groupHeader) {
        this.groupHeader = groupHeader;
    }

    public List<PaymentInformation> getPaymentInformation() {
        return paymentInformation;
    }

    public void addPaymentInformation(PaymentInformation paymentInformation) {
        this.paymentInformation.add(paymentInformation);
    }
}

