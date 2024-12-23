package com.isu.prepaidcard.data.mappers.demo_response_mapper

import com.isu.prepaidcard.data.remote.dto.res.DemoResponseDto
import com.isu.prepaidcard.domain.models.res.DemoResponseDomain

interface DemoResponseMapper {
    fun mapToDomain(dto: DemoResponseDto): DemoResponseDomain
    fun mapToDTO(domain: DemoResponseDomain): DemoResponseDto
}