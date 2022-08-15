package machine.api;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private String id;

    private Integer amount;

    private Integer amountAvailable;

    @NotNull(message = "Cost cannot be null")
    private Integer cost;

    @NotNull(message = "Product name cannot be null")
    private String productName;

    private String sellerId;

}
