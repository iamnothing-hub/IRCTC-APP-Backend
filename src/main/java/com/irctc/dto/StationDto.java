package com.irctc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationDto {
    private Long stationId;

    @NotNull(message = "{STATION_NAME_CAN_NOT_BE_NULL}")
    private String stationName;


    @NotBlank(message = "{STATION_CODE_NOT_BLANK}")
    @Pattern(regexp = "[A-Z]{2,}", message = "{STATION_CODE_UPPER_CASE}")
    private String stationCode;

    @NotBlank(message = "{STATION_CITY_NOT_BLANK}")
    private String city;

    @NotBlank(message = "{STATION_STATE_NOT_BLANK}")
    private String state;
}
