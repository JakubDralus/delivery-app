package org.company.modules.partner.application;

import org.company.modules.partner.application.web.PartnerDto;
import org.company.modules.partner.application.web.PartnerReadDto;
import org.company.modules.partner.domain.Partner;
import org.company.modules.partner.domain.PartnerRepository;
import org.company.modules.partner.domain.PartnerSpecifications;
import org.company.shared.aplication.GenericService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PartnerService extends GenericService<Partner, PartnerDto, Long, PartnerRepository, PartnerAssembler> {
    private final PartnerRepository partnerRepository;
    private final PartnerAssembler partnerAssembler;
    public PartnerService(PartnerRepository repository, PartnerAssembler assembler) {
        super(repository, assembler);
        this.partnerRepository = repository;
        this.partnerAssembler = assembler;
    }
    
    public List<PartnerReadDto> getPartnersInCity(String city) {
        Specification<Partner> citySpec = PartnerSpecifications.hasCity(city);
        return partnerRepository.findAll(citySpec).stream().map(partnerAssembler::toReadDto).collect(Collectors.toList());
    }
    public List<PartnerReadDto> getPartnersInCityAndName(String city, String name) {
        Specification<Partner> citySpec = PartnerSpecifications.hasCityAndName(city, name);
        return partnerRepository.findAll(citySpec).stream().map(partnerAssembler::toReadDto).collect(Collectors.toList());
    }


}


