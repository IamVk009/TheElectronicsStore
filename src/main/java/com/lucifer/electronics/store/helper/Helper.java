package com.lucifer.electronics.store.helper;

import com.lucifer.electronics.store.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

public class Helper {

    /**
     * This method is typically used to convert a Page of entity objects fetched from a database query into a
     * PageableResponse of DTO objects
     *
     * @param page
     * @param type Type of target class (i.e. Type of pageable response we want to return.
     * @param <U>  Entity Class type
     * @param <V>  Dto Class type
     * @return Pageable response of type similar to type passed through argument.
     */
    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
//      Retrieving the list of entities from the input page.
        List<U> entity = page.getContent();
//      Mapping each entity to its corresponding DTO using the ModelMapper.
        List<V> productDtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).toList();
//      Creating a new instance of PageableResponse for the DTO type V.
        PageableResponse<V> response = new PageableResponse<V>();
        response.setContent(productDtoList);
        response.setTotalElements(page.getTotalElements());
        response.setPageSize(page.getSize());
        response.setPageNumber(page.getNumber());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }
}
