package uz.cluster.services.auth_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.entity.auth.User;
import uz.cluster.entity.references.model.FileEntity;
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
import uz.cluster.services.references_service.FileService;
import uz.cluster.util.LanguageManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final FileService fileService;

    @Transactional
    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_ADD)
    public ApiResponse add(UserDTO userDTO, MultipartFile file) { // This method is to add
        if (userRepository.existsByLoginAndIdNot(userDTO.getLogin(), 0)) //This will check login is unique or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("phone_exists"));

        if (userRepository.existsByEmailAndIdNot(userDTO.getEmail(), 0)) //This will check that email is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("email_exists"));

        if (userRepository.existsByDocumentSerialNumberAndIdNot(userDTO.getDocumentSerialNumber(), 0)) //This check that passport number is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("passport_number_exists"));

        if (userDTO.getPassword().isEmpty()) //This check that user has passport or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("no_password"));

        FileEntity fileEntity = null;
        if (!file.isEmpty()){
            try{
                fileEntity = fileService.saveFile(file);
                log.info("User rasmini saqlandi !");
            }catch (Exception e){
                log.error("User rasmini saqlashda xatolik !");
            }
        }

        User user = new User(
                userDTO.getFirstName().substring(0, 1).toUpperCase(Locale.ROOT) + userDTO.getFirstName().toLowerCase().substring(1),
                userDTO.getLastName().substring(0, 1).toUpperCase(Locale.ROOT) + userDTO.getLastName().toLowerCase().substring(1),
                userDTO.getMiddleName().substring(0, 1).toUpperCase(Locale.ROOT) + userDTO.getMiddleName().toLowerCase().substring(1),
                userDTO.getDocumentSerialNumber().toUpperCase(),
                userDTO.getLogin(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getGender(),
                userDTO.getSystemRoleName(),
                fileEntity,
                userDTO.isEnabled()
        );

        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); //This sets user's password that is encrypted
        userRepository.save(user); //this saves user to database
        return new ApiResponse(true, user, LanguageManager.getLangMessage("saved"));
    }


    @Transactional
    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_EDIT)
    public ApiResponse edit(UserDTO userDTO,int id,MultipartFile file) {
        ApiResponse apiResponse = new ApiResponse();
        if (userRepository.existsByLoginAndIdNot(userDTO.getLogin(), userDTO.getId())) //This will check login is unique or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("phone_exists"));

        if (userRepository.existsByEmailAndIdNot(userDTO.getEmail(), userDTO.getId())) //This will check that email is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("email_exists"));

        if (userRepository.existsByDocumentSerialNumberAndIdNot(userDTO.getDocumentSerialNumber(), userDTO.getId())) //This check that passport number is already exists or not, if not it terminates from adding
            return new ApiResponse(false, LanguageManager.getLangMessage("passport_number_exists"));

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) //This check that there is such user, if not this statement terminates from adding
            return new ApiResponse(false, id, LanguageManager.getLangMessage("no_user"));

        FileEntity fileEntity = null;
        if (!file.isEmpty()){
            try{
                if (optionalUser.get().getFile() != null){
                    fileService.deleteFile(optionalUser.get().getFile().getId());
                }
                fileEntity = fileService.saveFile(file);
                log.info("User rasmini saqlandi !");
            }catch (Exception e){
                log.error("User rasmini saqlashda xatolik !");
            }
        }

        User editingUser = optionalUser.get();
        editingUser.setFirstName(userDTO.getFirstName());
        editingUser.setLastName(userDTO.getLastName());
        editingUser.setMiddleName(userDTO.getMiddleName());
        editingUser.setLogin(userDTO.getLogin());
        editingUser.setDocumentSerialNumber(userDTO.getDocumentSerialNumber());
        editingUser.setSystemRoleName(userDTO.getSystemRoleName());
        editingUser.setGender(userDTO.getGender());
        editingUser.setEnabled(userDTO.isEnabled());
        editingUser.setFile(fileEntity);
        editingUser.setAccountNonLocked(true);
        editingUser.setAccountNonExpired(true);

        if (!userDTO.getPassword().isEmpty())
            editingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User editedUser = userRepository.save(editingUser);
        apiResponse.setSuccess(true);
        apiResponse.setObject(editedUser);
        apiResponse.setMessage(LanguageManager.getLangMessage("edited"));
        return apiResponse;
    }

    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_VIEW)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @CheckPermission(form = FormEnum.ADMIN_PANEL, permission = Action.CAN_VIEW)
    public UserDTO getById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.orElseThrow().getDto();
        }
        return null;
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
            ));
            User user = (User) authentication.getPrincipal();

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
                    user.getEmail(),
                    user.getGender(),
                    user.getLogin(),
                    user.getSystemRoleName().name(),
                    true
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
                    false
            );
        }
    }
}
