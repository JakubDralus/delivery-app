package org.company.modules.order.application;

import lombok.AllArgsConstructor;
import org.company.modules.address.application.AddressAssembler;
import org.company.modules.address.domain.Address;
import org.company.modules.address.domain.AddressRepository;
import org.company.modules.delivery_man.appliction.DeliveryManAssembler;
import org.company.modules.delivery_man.domain.DeliveryMan;
import org.company.modules.delivery_man.domain.DeliveryManRepository;
import org.company.modules.order.application.web.OrderDto;
import org.company.modules.order.application.web.OrderReadDto;
import org.company.modules.order.domain.Order;
import org.company.modules.partner.application.PartnerAssembler;
import org.company.modules.partner.domain.Partner;
import org.company.modules.partner.domain.PartnerRepository;
import org.company.modules.user.application.UserAssembler;
import org.company.modules.user.domain.User;
import org.company.modules.user.domain.UserRepository;
import org.company.shared.aplication.IAssembler;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@AllArgsConstructor
public class OrderAssembler implements IAssembler<Order, OrderDto> {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressAssembler addressAssembler;
    private final UserAssembler userAssembler;
    private final DeliveryManAssembler deliveryManAssembler;
    private final PartnerAssembler partnerAssembler;
    private final DeliveryManRepository deliveryManRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setAddressStart(addressAssembler.toDto(order.getAddressStart()));
        orderDto.setCustomer(userAssembler.toDto(order.getCustomer()));
        orderDto.setAddressEnd(addressAssembler.toDto(order.getAddressEnd()));
        if(order.getDeliveryMan() != null) {
            orderDto.setDeliveryMan(deliveryManAssembler.toDto(order.getDeliveryMan()));
        }
        orderDto.setPartner(partnerAssembler.toDto(order.getPartner()));
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setTip(order.getTip());
        orderDto.setCreationDate(order.getCreationDate());
        orderDto.setCompletionDate(order.getCompletionDate());
        orderDto.setStatus(order.getStatus());
        orderDto.setDistanceInKm(order.getDistanceInKm());
        return orderDto;
    }

    @Override
    public void toEntity(OrderDto orderDto, Order order) {
        updateAddresses(orderDto,order);
        updateCustomer(orderDto, order);
        updatePartner(orderDto, order);
        if (orderDto.getDeliveryMan() == null){
            order.setDeliveryMan(null);
        }
        else {
            updateDeliveryMan(orderDto, order);
        }
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setTip(orderDto.getTip());
        order.setCreationDate(orderDto.getCreationDate());
        order.setCompletionDate(orderDto.getCompletionDate());
        order.setStatus(orderDto.getStatus());
        order.setDistanceInKm(orderDto.getDistanceInKm());
        order.setRating(orderDto.getRating());
        order.setRating(orderDto.getRating());
    }

    public OrderReadDto toReadDto(Order order) {
        OrderReadDto orderReadDto = new OrderReadDto();
        orderReadDto.setId(order.getId());
        orderReadDto.setAddressStart(order.getAddressStart().getStreet());
        orderReadDto.setAddressEnd(order.getAddressEnd().getStreet());
        orderReadDto.setCustomerFirstName(order.getCustomer().getFirstName());
        orderReadDto.setCustomerLastName(order.getCustomer().getLastName());
        orderReadDto.setCustomerTelephoneNumber(order.getCustomer().getTelephoneNumber());
        orderReadDto.setPartner(order.getPartner().getName());
        orderReadDto.setPartnerPhotoPath(order.getPartner().getPhotoPath());
        if(order.getDeliveryMan() == null) {
            orderReadDto.setDeliveryManId(null);
            orderReadDto.setDeliveryManFirstName(null);
            orderReadDto.setDeliveryManLastName(null);
        }
        else {
            orderReadDto.setDeliveryManId(order.getDeliveryMan().getUser().getId());
            orderReadDto.setDeliveryManFirstName(order.getDeliveryMan().getUser().getFirstName());
            orderReadDto.setDeliveryManLastName(order.getDeliveryMan().getUser().getLastName());
        }
        orderReadDto.setTotalPrice(order.getTotalPrice());
        orderReadDto.setTip(order.getTip());
        Date creationDate = order.getCreationDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        orderReadDto.setCreationDate(simpleDateFormat.format(creationDate));
        Date completionDate = order.getCompletionDate();
        if(completionDate != null) orderReadDto.setCompletionDate(simpleDateFormat.format(completionDate));
        orderReadDto.setStatus(order.getStatus());
        orderReadDto.setDistanceInKm(order.getDistanceInKm());
        orderReadDto.setRating(order.getRating());
        return orderReadDto;
    }

    private void updateAddresses(OrderDto orderDto, Order order) {
        Address addressStart = addressRepository.findById(orderDto.getAddressStart().getId()).orElseThrow(null);
        Address addressEnd = addressRepository.findById(orderDto.getAddressEnd().getId()).orElseThrow(null);
        order.setAddressStart(addressStart);
        order.setAddressEnd(addressEnd);
    }
    private void updateCustomer(OrderDto orderDto, Order order) {
        User customer = userRepository.findById(orderDto.getCustomer().getId()).orElseThrow(null);
        order.setCustomer(customer);
    }
    private void updateDeliveryMan(OrderDto orderDto, Order order) {
        DeliveryMan deliveryMan =  deliveryManRepository.findById(orderDto.getDeliveryMan().getId()).orElseThrow(null);
        order.setDeliveryMan(deliveryMan);
    }
    private void updatePartner(OrderDto orderDto, Order order) {
        Partner partner = partnerRepository.findById(orderDto.getPartner().getId()).orElseThrow(null);
        order.setPartner(partner);
    }
}
