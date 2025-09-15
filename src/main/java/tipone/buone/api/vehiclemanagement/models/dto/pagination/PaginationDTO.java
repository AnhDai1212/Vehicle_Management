package tipone.buone.api.vehiclemanagement.models.dto.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {
    @Min(value = 0, message = MessageConstant.PAGE_MIN)
    private int page;

    @Max(value = 100, message = MessageConstant.PAGE_SIZE_MAX)
    private int pageSize;
}