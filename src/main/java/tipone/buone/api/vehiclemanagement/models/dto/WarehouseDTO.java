package tipone.buone.api.vehiclemanagement.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {
    private String id;

    @NotNull(message = MessageConstant.INVALID_WAREHOUSE_NAME)
    @Pattern(regexp = Constant.WAREHOUSE_NAME_REGEX, message = MessageConstant.INVALID_WAREHOUSE_NAME)
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}