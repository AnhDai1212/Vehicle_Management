package tipone.buone.api.vehiclemanagement.models.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.enums.SortDirection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortDTO {
    private String field;
    private SortDirection sort;
}