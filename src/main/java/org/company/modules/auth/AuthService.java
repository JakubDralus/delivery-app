package org.company.modules.auth;

import lombok.RequiredArgsConstructor;
import org.company.modules.address.application.AddressAssembler;
import org.company.modules.address.domain.Address;
import org.company.modules.address.domain.AddressRepository;
import org.company.modules.auth.web.*;
import org.company.modules.delivery_man.domain.DeliveryMan;
import org.company.modules.delivery_man.domain.DeliveryManRepository;
import org.company.modules.partner.domain.Partner;
import org.company.modules.partner.domain.PartnerRepository;
import org.company.modules.role.domain.Role;
import org.company.modules.role.domain.RoleRepository;
import org.company.modules.user.domain.User;
import org.company.modules.user.domain.UserRepository;
import org.company.shared.photos.PhotoService;
import org.company.shared.photos.PhotoType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;


@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final DeliveryManRepository deliveryManRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    
    private final AddressAssembler addressAssembler;
    
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PhotoService photoService;
    private final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    
    // in other words - sign in
    public AuthResponseDto authenticate(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        
        return AuthResponseDto.builder()
                .token(jwtToken)
                .role(user.getRole().getName())
                .expirationDate(isoFormat.format(jwtService.extractExpiration(jwtToken)))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .id(user.getId())
                .build();
    }
    
    public RegisterResponseDto registerUser(RegisterUserDto userDto) {
        if (!isEmailAvailable(userDto.getEmail())) {
            return RegisterResponseDto.builder().message("email not available").build();
        }
        
        Role role = roleRepository.findById(1L).orElse(null);
        
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .telephoneNumber(userDto.getTelephoneNumber())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        
        Address address = new Address();
        addressAssembler.toEntity(userDto.getAddress(), address);
        address.setUser(user);
        addressRepository.save(address);

        user.getAddresses().add(address);
        
        return RegisterResponseDto.builder().message("success").build();
    }
    
    public RegisterResponseDto registerAdmin(RegisterUserDto userDto) {
        if (!isEmailAvailable(userDto.getEmail())) {
            return RegisterResponseDto.builder().message("email not available").build();
        }
        Role role = roleRepository.findById(4L).orElse(null);
        
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .telephoneNumber(userDto.getTelephoneNumber())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(role)
                .build();
        
        userRepository.save(user);
        return RegisterResponseDto.builder().message("success").build();
    }
    
    public RegisterResponseDto registerPartner(RegisterPartnerDto partnerDto, MultipartFile photo) {
        if (!isEmailAvailable(partnerDto.getEmail())) {
            return RegisterResponseDto.builder().message("email not available").build();
        }
        if (!isPhotoFormatCorrect(photo)) {
            return RegisterResponseDto.builder().message("photo do not have a suitable extension").build();
        }
        Role role = roleRepository.findById(2L).orElse(null);

        // create user
        User user = User.builder()
                .firstName(partnerDto.getFirstName())
                .lastName(partnerDto.getLastName())
                .telephoneNumber(partnerDto.getTelephoneNumber())
                .email(partnerDto.getEmail())
                .password(passwordEncoder.encode(partnerDto.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        
        Address address = new Address();
        addressAssembler.toEntity(partnerDto.getAddress(), address);
        address.setUser(user);
        addressRepository.save(address);

        user.getAddresses().add(address);

        // create partner
        Partner partner = Partner.builder()
                .name(partnerDto.getName())
                .accountNumber(partnerDto.getAccountNumber())
                .contactNumber(partnerDto.getContactNumber())
                .description(partnerDto.getDescription())
                .openHour(partnerDto.getOpenHour())
                .closeHour(partnerDto.getCloseHour())
                .websiteLink(partnerDto.getWebsiteLink())
                .expectedWaitingTime(partnerDto.getExpectedWaitingTime())
                .owner(user)
                .photoPath( photoService.savePhoto(photo, PhotoType.partner))
                .type(partnerDto.getType())
                .build();
        
        partnerRepository.save(partner);

        return RegisterResponseDto.builder().message("success").build();
    }
    
    public RegisterResponseDto registerCourier(RegisterDeliveryManDto deliveryManDto) {
        if (!isEmailAvailable(deliveryManDto.getEmail())) {
            return RegisterResponseDto.builder().message("email not available").build();
        }
        Role role = roleRepository.findById(3L).orElse(null);
        
        // create user
        User user = User.builder()
                .firstName(deliveryManDto.getFirstName())
                .lastName(deliveryManDto.getLastName())
                .telephoneNumber(deliveryManDto.getTelephoneNumber())
                .email(deliveryManDto.getEmail())
                .password(passwordEncoder.encode(deliveryManDto.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        
        // create delivery man
        DeliveryMan deliveryMan = DeliveryMan.builder()
                .workingArea(deliveryManDto.getWorkingArea())
                .accountNumber(deliveryManDto.getAccountNumber())
                .user(user)
                .build();
        
        deliveryManRepository.save(deliveryMan);
        
        return RegisterResponseDto.builder().message("success").build();
    }
    
    /**
     * checks if account with this email is already created
     * @param providedEmail email from registration form
     * @return true - if email available / no account with this email in database
     */
    private boolean isEmailAvailable(String providedEmail) {
        User user = userRepository.findByEmail(providedEmail).orElse(null);
        return user == null;
    }
    
    private boolean isPhotoFormatCorrect(MultipartFile photo) {
        if(photo != null) {
            String name = photo.getOriginalFilename();
            String extension = name.substring(name.lastIndexOf("."));
            return extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg");
        }
        return false;
    }
}
