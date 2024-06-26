package org.company.modules.delivery_man.appliction;

import lombok.AllArgsConstructor;
import org.company.modules.delivery_man.appliction.web.DeliveryManDto;
import org.company.modules.delivery_man.domain.DeliveryMan;
import org.company.modules.user.application.UserAssembler;
import org.company.modules.user.domain.User;
import org.company.modules.user.domain.UserRepository;
import org.company.shared.aplication.IAssembler;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class DeliveryManAssembler implements IAssembler<DeliveryMan, DeliveryManDto> {
    private final UserAssembler userAssembler;
    private final UserRepository userRepository;
    
    @Override
    public DeliveryManDto toDto(DeliveryMan deliveryMan) {
        DeliveryManDto deliveryManDto = new DeliveryManDto();
        deliveryManDto.setId(deliveryMan.getId());
        deliveryManDto.setWorkingArea(deliveryMan.getWorkingArea());
        deliveryManDto.setAccountNumber(deliveryMan.getAccountNumber());
        deliveryManDto.setUser(userAssembler.toDto(deliveryMan.getUser()));
        return deliveryManDto;
    }

    @Override
    public void toEntity(DeliveryManDto deliveryManDto, DeliveryMan deliveryMan) {
        deliveryMan.setWorkingArea(deliveryManDto.getWorkingArea());
        deliveryMan.setAccountNumber(deliveryManDto.getAccountNumber());
        updateUser(deliveryManDto,deliveryMan);
    }

    private void updateUser(DeliveryManDto deliveryManDto,DeliveryMan deliveryMan) {
        User user = userRepository.findById(deliveryManDto.getUser().getId()).orElseThrow(null);
        deliveryMan.setUser(user);
    }
}
