package com.lucifer.electronics.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseMessage {
    private String imageFileName;
    private String message;
    private boolean success;
    private HttpStatus status;
}
