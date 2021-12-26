package pl.java.homebudget.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditDto {
    private BigDecimal percentage;
    private BigDecimal plannedAmount;
    private BigDecimal realAmount;
}
