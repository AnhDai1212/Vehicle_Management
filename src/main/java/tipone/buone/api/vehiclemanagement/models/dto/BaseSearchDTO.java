package tipone.buone.api.vehiclemanagement.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PaginationDTO;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.SortDTO;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSearchDTO<T> {
    @Valid
    private PaginationDTO pagination;
    private List<SortDTO> sortedBy;
    @Size(max = 100, message = MessageConstant.KEY_WORD_TOO_LONG)
    private String searchedBy;
    private T filter;
}