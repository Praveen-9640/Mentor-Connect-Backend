package com.klu.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private UserDTO mentee;
    private UserDTO mentor;
}
