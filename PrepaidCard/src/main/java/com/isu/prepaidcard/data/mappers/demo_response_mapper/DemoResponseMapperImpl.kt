package com.isu.prepaidcard.data.mappers.demo_response_mapper

import com.isu.prepaidcard.data.remote.dto.res.DemoResponseDto
import com.isu.prepaidcard.domain.models.res.DemoResponseDomain

class DemoResponseMapperImpl : DemoResponseMapper {
    override fun mapToDomain(dto: DemoResponseDto): DemoResponseDomain {
        return DemoResponseDomain(dto.id, dto.name, dto.status)
    }

    override fun mapToDTO(domain: DemoResponseDomain): DemoResponseDto {
        return DemoResponseDto(domain.id, domain.name, domain.status)
    }
}