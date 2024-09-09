package com.zilch.zen.core.messagesender;

import com.zilch.zen.core.payments.kafka.PaymentDTO;
import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.utils.PaymentMethod;
import com.zilch.zen.core.utils.WhichPayment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MessageDTOMapperTest {

    private final MessageDTOMapper messageDTOMapper = new MessageDTOMapper();

    @Test
    void shouldMapPaymentDTOAndUserToMessageDTO() {
        // given
        PaymentDTO paymentDTO = new PaymentDTO(
                "payment-123",
                "user-456",
                "purchase-789",
                "Headphones",
                BigDecimal.valueOf(250.75),
                PaymentMethod.FOUR_WEEKS,
                WhichPayment.TWO
        );
        User user = new User("userId", "John", "john@example.com");
        // when
        MessageDTO result = messageDTOMapper.mapToMessageDTO(paymentDTO, user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNotBlank();
        assertThat(result.email()).isEqualTo("john@example.com");
        assertThat(result.purchaseName()).isEqualTo("Headphones");
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(250.75));
        assertThat(result.paymentMethod()).isEqualTo(PaymentMethod.FOUR_WEEKS);
        assertThat(result.payment()).isEqualTo(WhichPayment.TWO);
    }
}