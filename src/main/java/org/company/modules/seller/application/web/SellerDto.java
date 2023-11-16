package org.company.modules.seller.application.web;

import lombok.Getter;
import lombok.Setter;
import org.company.modules.address.application.web.AddressDto;
import org.company.modules.user.application.web.UserDto;


@Getter
@Setter
public class SellerDto {
    private long id;
    private String name;
    private String account_number;
    private String contact_number;
    
    private AddressDto address;
    private UserDto owner;
}
