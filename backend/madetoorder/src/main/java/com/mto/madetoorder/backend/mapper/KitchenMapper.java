package com.mto.madetoorder.backend.mapper;

import com.mto.madetoorder.backend.dto.KitchenDTO;
import com.mto.madetoorder.backend.model.Kitchen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface KitchenMapper {

    KitchenMapper INSTANCE = Mappers.getMapper(KitchenMapper.class);

    @Mapping(source = "kitchenName", target = "name")
    @Mapping(source = "address.area", target = "area")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "rating.average", target = "rating")
    KitchenDTO toDTO(Kitchen kitchen);
}
