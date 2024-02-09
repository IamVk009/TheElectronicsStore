package com.lucifer.electronics.store.dtos;

import com.lucifer.electronics.store.validate.CoverImageNameValid;
import com.lucifer.electronics.store.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Size(min = 5, max = 20, message = "Title must be of min 5 characters..")
    private String title;

    @NotBlank
    private String description;

    @CoverImageNameValid
    private String coverImage;
}
