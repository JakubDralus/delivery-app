package org.company.modules.partner.application;

import org.company.modules.partner.application.web.PartnerDto;
import org.company.modules.partner.application.web.PartnerReadDto;
import org.company.modules.partner.domain.Partner;
import org.company.modules.partner.domain.PartnerRepository;
import org.company.modules.user.application.UserService;
import org.company.shared.aplication.GenericService;
import org.company.shared.photos.PhotoService;
import org.company.shared.photos.PhotoType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PartnerService extends GenericService<Partner, PartnerDto, Long, PartnerRepository, PartnerAssembler> {
    private final PartnerRepository partnerRepository;
    private final PartnerAssembler partnerAssembler;
    protected final UserService userService;
    protected final PhotoService photoService;
    
    public PartnerService(PartnerRepository repository, PartnerAssembler assembler, UserService userService, PhotoService photoService) {
        super(repository, assembler);
        this.partnerRepository = repository;
        this.partnerAssembler = assembler;
        this.userService = userService;
        this.photoService = photoService;
    }

    @Transactional
    public PartnerDto saveItem(MultipartFile photo, PartnerDto partnerDto)
    {
        partnerDto.setPhotoPath(photoService.savePhoto(photo, PhotoType.partner));
        return super.saveItem(partnerDto);
    }
    public List<PartnerReadDto> getPartnersReadDto() {
        List<Partner> partners = partnerRepository.findAll();
        return partners.stream()
                .map(partnerAssembler::toReadDto)
                .collect(Collectors.toList());
    }

    public PartnerDto getPartnerByName(String name) {
        Partner partner = partnerRepository.findByName(name).orElse(null);
        return partnerAssembler.toDto(partner);
    }
    
    @Transactional
    public PartnerDto removeItem(Long id) {
        PartnerDto partnerDto = super.removeItem(id);
        photoService.removePhoto(PhotoType.partner, partnerDto.getPhotoPath());
        userService.removeItem(partnerDto.getOwner().getId());
        return partnerDto;
    }
}
