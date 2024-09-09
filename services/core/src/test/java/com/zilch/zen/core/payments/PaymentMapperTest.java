package com.zilch.zen.core.payments;

import com.zilch.zen.core.payments.entity.Payment;
import com.zilch.zen.core.payments.kafka.PaymentDTO;
import com.zilch.zen.core.purchases.entity.Purchase;
import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.utils.PaymentMethod;
import com.zilch.zen.core.utils.WhichPayment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class PaymentMapperTest {

    PaymentMapper paymentMapper = new PaymentMapper();

    @Test
    public void shouldMapPaymentDTOtoPayment() {
        // given
        PaymentDTO paymentDTO = new PaymentDTO(
                "paymentId",
                "userId",
                "purchaseId",
                "purchaseName",
                new BigDecimal("10.27"),
                PaymentMethod.FOUR_WEEKS,
                WhichPayment.TWO
        );
        User user = new User("userId", "John", "abc@example.com");
        Purchase purchase = new Purchase("purchaseId", "purchaseName", PaymentMethod.FOUR_WEEKS, user);

        // when
        Payment payment = paymentMapper.toEntity(paymentDTO, purchase);

        // then
        assertThat(payment.getId()).isEqualTo("paymentId");
        assertThat(payment.getPurchaseName()).isEqualTo("purchaseName");
        assertThat(payment.getAmount()).isEqualTo(new BigDecimal("10.27"));
        assertThat(payment.getPayment()).isEqualTo(WhichPayment.TWO);
        assertThat(payment.getPurchase()).isEqualTo(purchase);
    }
}