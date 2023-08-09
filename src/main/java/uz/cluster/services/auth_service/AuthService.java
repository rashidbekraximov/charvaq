package uz.cluster.services.auth_service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.entity.auth.User;
import uz.cluster.security.JwtResponse;
import uz.cluster.repository.user_info.RoleRepository;
import uz.cluster.repository.user_info.UserRepository;
import uz.cluster.entity.references.model.Role;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.auth.LoginDTO;
import uz.cluster.payload.auth.UserDTO;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.security.JwtProvider;
import uz.cluster.util.LanguageManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;

    @Transactional
    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_ADD)
    public ApiResponse add(UserDTO userDTO) { // This method is to add
        if (userRepository.existsByLoginAndIdNot(userDTO.getLogin(), 0)) //This will check login is unique or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("phone_exists"));

        if (userRepository.existsByEmailAndIdNot(userDTO.getEmail(), 0)) //This will check that email is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("email_exists"));

        if (userRepository.existsByDocumentSerialNumberAndIdNot(userDTO.getDocumentSerialNumber(), 0)) //This check that passport number is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("passport_number_exists"));

        if (userDTO.getPassword().isEmpty()) //This check that user has passport or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("no_password"));

        Optional<Role> optionalRole = roleRepository.findById(userDTO.getRoleId() != null ? userDTO.getRoleId() : 0);

        if (
                optionalRole.isEmpty()
                        && (userDTO.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_FORM_MEMBER || userDTO.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_MEMBER)
        ) //This check that user has this info or not, if not this statement terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("no_position"));

        if (Period.between(userDTO.getBirthday(), LocalDate.now()).getYears() < 18)  //This check that user's age is not less than AGE_RESTRICTION, if not this statement terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("age_restriction"));

        User user = new User(
                userDTO.getFirstName().substring(0, 1).toUpperCase(Locale.ROOT) + userDTO.getFirstName().toLowerCase().substring(1),
                userDTO.getLastName().substring(0, 1).toUpperCase(Locale.ROOT) + userDTO.getLastName().toLowerCase().substring(1),
                userDTO.getMiddleName().substring(0, 1).toUpperCase(Locale.ROOT) + userDTO.getMiddleName().toLowerCase().substring(1),
                userDTO.getDocumentSerialNumber().toUpperCase(),
                userDTO.getBirthday(),
                userDTO.getLogin(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getGender(),
                userDTO.getRoleId() != null ? optionalRole.get() : null,
                userDTO.getNotes(),
                userDTO.getSystemRoleName(),
                userDTO.isAccountNonLocked(),
                userDTO.getClusterId()
        );

        if (
                user.getSystemRoleName() != SystemRoleName.SYSTEM_ROLE_FORM_MEMBER
                        && user.getSystemRoleName() != SystemRoleName.SYSTEM_ROLE_MEMBER
        )


        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); //This sets user's password that is encrypted
        userRepository.save(user); //this saves user to database
        return new ApiResponse(true, user, LanguageManager.getLangMessage("saved"));
    }


    @Transactional
    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_EDIT)
    public ApiResponse edit(UserDTO userDTO,int id) {
        ApiResponse apiResponse = new ApiResponse();
        if (userRepository.existsByLoginAndIdNot(userDTO.getLogin(), userDTO.getId())) //This will check login is unique or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("phone_exists"));

        if (userRepository.existsByEmailAndIdNot(userDTO.getEmail(), userDTO.getId())) //This will check that email is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("email_exists"));

        if (userRepository.existsByDocumentSerialNumberAndIdNot(userDTO.getDocumentSerialNumber(), userDTO.getId())) //This check that passport number is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("passport_number_exists"));

        Optional<User> optionalUser = userRepository.findById(id);

        Optional<Role> optionalRole =  (userDTO.getRoleId() ==null )?null: roleRepository.findById(userDTO.getRoleId());

        if (optionalUser.isEmpty()) //This check that there is such user, if not this statement terminates from adding
            return new ApiResponse(false, id, LanguageManager.getLangMessage("no_user"));

        if ( (userDTO.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_FORM_MEMBER || userDTO.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_MEMBER) &&( optionalRole == null ||optionalRole.isEmpty())) //This check that user has this info or not, if not this statement terminates from adding
            return new ApiResponse(false, id, LanguageManager.getLangMessage("no_position"));

        if (Period.between(userDTO.getBirthday(), LocalDate.now()).getYears() < 18) //This check that user's age is not less than 18, if not this statement terminates from adding
            return new ApiResponse(false, optionalUser.get(), LanguageManager.getLangMessage("age_restriction"));

        User editingUser = optionalUser.get();
        editingUser.setFirstName(userDTO.getFirstName());
        editingUser.setLastName(userDTO.getLastName());
        editingUser.setMiddleName(userDTO.getMiddleName());
        editingUser.setLogin(userDTO.getLogin());
        editingUser.setDocumentSerialNumber(userDTO.getDocumentSerialNumber());
        editingUser.setSystemRoleName(userDTO.getSystemRoleName());

        if ( editingUser.getSystemRoleName() != SystemRoleName.SYSTEM_ROLE_FORM_MEMBER && editingUser.getSystemRoleName() != SystemRoleName.SYSTEM_ROLE_MEMBER )

        if (!userDTO.getPassword().isEmpty()) //If user's password is changed, it again encrypts it and assigns to user
            editingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User editedUser = userRepository.save(editingUser); //This will save User to DB
        apiResponse.setSuccess(true); //apiResponse status -> True or False
        apiResponse.setObject(editedUser); //this is User obj to give to Front-end
        apiResponse.setMessage(LanguageManager.getLangMessage("edited")); //ApiResponses message
        return apiResponse;
    }

    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_VIEW)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_VIEW)
    public User getById(int id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(value -> value.setPassword(""));
        return user.orElse(new User());
    }

    public ApiResponse delete(int id) {
        if (!userRepository.existsById(id))
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        userRepository.deleteById(id);
        return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    public JwtResponse login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getLogin(),
                    loginDTO.getPassword()
            )); //System checks user is exists or not
            User user = (User) authentication.getPrincipal(); //Casts Principal to User

            String token = jwtProvider.generateToken(user.getLogin());
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);

            return new JwtResponse(
                    token,
                    true,
                    user.getFio(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getMiddleName(),
                    "",
                    user.getEmail(),
                    user.getGender(),
                    user.getLogin(),
                    user.getSystemRoleName().name(),
                    true,
                    user.getClusterId(),
                    ""
            );
        } catch (Exception exception) {
            return new JwtResponse(
                    null,
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    false,
                    0,
                    null
            );
        }
    }
}
