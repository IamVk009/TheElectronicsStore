package com.lucifer.electronics.store.dtos;

import com.lucifer.electronics.store.validate.CoverImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "Category must have some title..")
    @Size(min = 4, message = "Title must be of minimum 4 characters")
    private String title;

    @NotBlank(message = "Category must have some description")
    private String description;

    @CoverImageNameValid
    private String coverImage;
}
