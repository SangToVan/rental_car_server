package com.sangto.rental_car_server.domain.dto.meta;

import com.sangto.rental_car_server.constant.MetaConstant;
import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Builder
public record SortingDTO(
        @RequestParam(name = "sortField", required = false) String sortField,
        @RequestParam(name = "sortDir", required = false) String sortDir) {
    public String sortField() {
        return Objects.requireNonNullElse(this.sortField, MetaConstant.Sorting.DEFAULT_FIELD);
    }

    public String sortDir() {
        return Objects.requireNonNullElse(this.sortDir, MetaConstant.Sorting.DEFAULT_DIRECTION);
    }
}
