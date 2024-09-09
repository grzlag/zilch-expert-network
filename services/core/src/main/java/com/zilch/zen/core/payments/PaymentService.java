package com.zilch.zen.core.payments;

import com.zilch.zen.core.messagesender.EmailMessageSender;
import com.zilch.zen.core.payments.entity.Payment;
import com.zilch.zen.core.payments.entity.PaymentRepository;
import com.zilch.zen.core.payments.kafka.PaymentDTO;
import com.zilch.zen.core.purchases.entity.Purchase;
import com.zilch.zen.core.purchases.entity.PurchaseRepository;
import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.users.entity.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PurchaseRepository purchaseRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;
    private final EmailMessageSender emailMessageSender;

    public PaymentService(
            PaymentRepository paymentRepository,
            PurchaseRepository purchaseRepository,
            PaymentMapper paymentMapper,
            UserRepository userRepository,
            EmailMessageSender emailMessageSender
    ) {
        this.paymentRepository = paymentRepository;
        this.purchaseRepository = purchaseRepository;
        this.paymentMapper = paymentMapper;
        this.userRepository = userRepository;
        this.emailMessageSender = emailMessageSender;
    }

    public void processPayment(PaymentDTO paymentDTO) {
        // Tech debt
        // - we should have separate event for purchase, for now it is being calculated from payments events
        // - also too much logic in one place, refactor
        Purchase purchase = purchaseRepository.findById(paymentDTO.purchaseId()).orElseGet(() -> {
            User user = userRepository.findById(paymentDTO.userId()).orElseThrow(
                    () -> new IllegalStateException(String.format("User %s not found", paymentDTO.userId()))
            );
            Purchase newPurchase = new Purchase();
            newPurchase.setId(paymentDTO.purchaseId());
            newPurchase.setPurchaseName(paymentDTO.purchaseName());
            newPurchase.setPaymentMethod(paymentDTO.paymentMethod());
            newPurchase.setUser(user);
            return purchaseRepository.save(newPurchase);
        });

        Payment payment = paymentMapper.toEntity(paymentDTO, purchase);
        paymentRepository.save(payment);

        emailMessageSender.sendMessage(paymentDTO);
    }
}
