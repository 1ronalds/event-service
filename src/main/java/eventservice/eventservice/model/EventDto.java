package eventservice.eventservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Model of the event data")
public class EventDto {

    @ApiModelProperty(value = "Unique id of the event")
    private Long id;

    @ApiModelProperty(value = "Title of the event")
    @NotNull
    @Size(max = 50)
    private String title;

    @ApiModelProperty(value = "Description of the event")
    @NotNull
    @Size(min = 10, max = 500)
    private String description;

    @ApiModelProperty(value = "Country, where the event will take place")
    @NotNull
    @Size(max = 25)
    private String country;

    @ApiModelProperty(value = "City, where the event will take place")
    @NotNull
    @Size(max = 25)
    private String city;

    @ApiModelProperty(value = "Maximum amount of people who can attend the event")
    @NotNull
    private int maxAttendance;

    @ApiModelProperty(value = "Date and time of the event in format d-m-yyyy hh:mm")
    @NotNull
    @JsonFormat(pattern="d-M-yyyy HH:mm")
    private LocalDateTime dateTime;

    @ApiModelProperty(value = "The amount of people who are attending the event")
    private int attendeeCount;

    @ApiModelProperty(value = "Organiser of the event")
    private UserMinimalDto organiser;

    @ApiModelProperty(value = "Type of event")
    private EventTypeDto type;
}
