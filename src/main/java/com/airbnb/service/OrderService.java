package com.airbnb.service;

import com.airbnb.dto.OrderDTO;
import com.airbnb.entity.Order;
import com.airbnb.repository.OrderRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JavaMailSender emailSender;
//    @Autowired
//    private SmsService smsService;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public void saveOrderAndSendEmail(OrderDTO orderDTO) {
        Optional<Order> existingOrderOpt = orderRepository.findByEmail(orderDTO.getEmail());
        Order order;
        if (existingOrderOpt.isPresent()) {
            order = existingOrderOpt.get();
        } else {
            order = new Order();
        }

        order.setFirstName(orderDTO.getFirstName());
        order.setLastName(orderDTO.getLastName());
        order.setEmail(orderDTO.getEmail());
        order.setAddress(orderDTO.getAddress());
        order.setAddress2(orderDTO.getAddress2());
        order.setCountry(orderDTO.getCountry());
        order.setState(orderDTO.getState());
        order.setZip(orderDTO.getZip());
        order.setCcName(orderDTO.getCcName());
        order.setCcNumber(orderDTO.getCcNumber());
        order.setCcExpiration(orderDTO.getCcExpiration());
        order.setCcCvv(orderDTO.getCcCvv());

        orderRepository.save(order);

        // Execute sending email and PDF in a separate thread
        executor.execute(() -> sendWelcomeEmail(order));
    }

    private void sendWelcomeEmail(Order order) {
        String emailContent = generateEmailContent(order);
        executor.execute(() -> sendPdfAttachment(order, emailContent));
//        executor.execute(() -> sendOrderConfirmationSms(order));
    }

    private void sendPdfAttachment(Order order, String emailContent) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Generate PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph(emailContent));
            document.close();

            byte[] pdfBytes = baos.toByteArray();

            // Send email with PDF attachment
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(order.getEmail());
            helper.setSubject("Order Confirmation from Trendcart");
            helper.setText("Dear " + order.getFirstName() + ",\n\n" +
                    "Please find attached your order confirmation.\n\n" +
                    "Best regards,\n" +
                    "The Trendcart Team");

            helper.addAttachment("order_confirmation.pdf", new ByteArrayResource(pdfBytes), "application/pdf");

            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }
//    private void sendOrderConfirmationSms(Order order) {
//        String message = "Hello " + order.getFirstName() + ", your order is confirmed and will be delivered soon. - Trendcart";
//        SmsService smsService = null;
//        smsService.sendSms(message, order.getPhoneNumber()).subscribe();
//    }

    private String generateEmailContent(Order order) {
        return String.format(
                "Hello %s %s,\n\n" +
                        "Welcome to Trendcart!\n\n" +
                        "Your order is confirmed and will be delivered in 10 working days to the address provided below:\n\n" +
                        "%s\n" +
                        "%s\n" +
                        "%s, %s\n" +
                        "%s\n\n" +
                        "Thank you for choosing Trendcart!\n\n" +
                        "Best regards,\n" +
                        "The Trendcart Team",
                order.getFirstName(), order.getLastName(),
                order.getAddress(),
                order.getAddress2() != null ? order.getAddress2() : "",
                order.getState(), order.getZip(),
                order.getCountry()
        );
    }
}
