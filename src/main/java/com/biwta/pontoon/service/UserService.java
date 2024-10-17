package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Authority;
import com.biwta.pontoon.domain.Users;
import com.biwta.pontoon.dto.AdminUserDTO;
import com.biwta.pontoon.dto.ProfileDetailsModel;
import com.biwta.pontoon.dto.UserDTO;
import com.biwta.pontoon.repository.AuthorityRepository;
import com.biwta.pontoon.repository.UserRepository;
import com.biwta.pontoon.security.AuthoritiesConstants;
import com.biwta.pontoon.security.JwtTokenProvider;
import com.biwta.pontoon.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${wrong.password.limit}")
    private Integer wrongPasswordLimit;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(propagation = Propagation.NEVER)
    public String signIn(String email, String password) {
        Optional<Users> appUser = userRepository.findByEmailOrUsernameIgnoreCaseAndActivated(email, true);

        if (!appUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not found!");
        }

        if (appUser.get().getWrongAttemptCount() != null && appUser.get().getWrongAttemptCount() > wrongPasswordLimit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your account is blocked");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            resetWrongAttemptCount(email);
            return jwtTokenProvider.createToken(email, appUser.get());
        } catch (Exception e) {
            wrongPasswordAttentAction(email);
//            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email/password supplied");
        }
    }

    @Transactional
    public Optional<Users> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);

        return userRepository
                .findOneByActivationKey(key)
                .map(
                        user -> {
                            // activate given user for the registration key.
                            user.setActivated(true);
                            user.setActivationKey(null);
                            log.debug("Activated user: {}", user);
                            return user;
                        }
                );
    }

    @Transactional
    public Optional<Users> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository
                .findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
                .map(
                        user -> {
                            user.setPassword(passwordEncoder.encode(newPassword));
                            user.setWrongAttemptCount(0);
                            user.setResetKey(null);
                            user.setResetDate(null);
                            return user;
                        }
                );
    }

    @Transactional
    public Optional<Users> resetWrongAttemptCount(String email) {
        log.debug("Reset user password for reset key {}", email);

        return userRepository
                .findOneByEmailIgnoreCaseAndActivated(email, true)
                .map(
                        user -> {
                            user.setWrongAttemptCount(0);
                            userRepository.save(user);
                            return user;
                        }
                );
    }

    @Transactional
    public Optional<Users> requestPasswordReset(String mail) {
        return userRepository
                .findOneByEmailIgnoreCase(mail)
                .filter(Users::isActivated)
                .map(
                        user -> {
                            user.setResetKey(SecurityUtils.getGeneratedStringKey());
                            user.setResetDate(Instant.now());
                            return user;
                        }
                );
    }

    @Transactional
    public Users registerUser(AdminUserDTO userDTO, String password) {
        userRepository
                .findOneByEmailIgnoreCase(userDTO.getEmail())
                .ifPresent(
                        existingUser -> {
                            boolean removed = removeNonActivatedUser(existingUser);
                            if (!removed) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
                            }
                        }
                );

        Users newUser = new Users();
        String encryptedPassword = passwordEncoder.encode(password);

        // New user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());

        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }

        newUser.setLangKey(userDTO.getLangKey());
        // New user is not active
        newUser.setActivated(false);
        // New user gets registration key
        newUser.setActivationKey(SecurityUtils.getGeneratedStringKey());

        Set<Authority> authorities = new HashSet<>();

        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        // this.clearUserCaches(newUser);
        log.debug("Created information for user: {}", newUser);

        return newUser;
    }

    private boolean removeNonActivatedUser(Users existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }

        userRepository.delete(existingUser);
        userRepository.flush();
        // this.clearUserCaches(existingUser);

        return true;
    }

    @Transactional
    public Users createUser(AdminUserDTO userDTO) {
        Users user = new Users();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }

        if (userDTO.getLangKey() == null) {
            user.setLangKey("en");        // Default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }

        String encryptedPassword = passwordEncoder.encode(SecurityUtils.getGeneratedStringKey());
        user.setPassword(encryptedPassword);
        user.setResetKey(SecurityUtils.getGeneratedStringKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);

        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }

        userRepository.save(user);
        // this.clearUserCaches(user);
        log.debug("Created information for user: {}", user);

        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    @Transactional
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
                .of(userRepository.findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(
                        user -> {
                            // this.clearUserCaches(user);
                            user.setFirstName(userDTO.getFirstName());
                            user.setLastName(userDTO.getLastName());

                            if (userDTO.getEmail() != null) {
                                user.setEmail(userDTO.getEmail().toLowerCase());
                            }

                            user.setActivated(userDTO.isActivated());
                            user.setLangKey(userDTO.getLangKey());

                            Set<Authority> managedAuthorities = user.getAuthorities();
                            managedAuthorities.clear();
                            userDTO
                                    .getAuthorities()
                                    .stream()
                                    .map(authorityRepository::findById)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .forEach(managedAuthorities::add);
                            // this.clearUserCaches(user);
                            log.debug("Changed information for user: {}", user);

                            return user;
                        }
                )
                .map(AdminUserDTO::new);
    }

    @Transactional
    public void deleteUser(String login) {
        userRepository
                .findOneByEmailIgnoreCase(login)
                .ifPresent(
                        user -> {
                            userRepository.delete(user);
                            // this.clearUserCaches(user);
                            log.debug("Deleted user: {}", user);
                        }
                );
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    @Transactional
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByEmailIgnoreCase)
                .ifPresent(
                        user -> {
                            user.setFirstName(firstName);
                            user.setLastName(lastName);

                            if (email != null) {
                                user.setEmail(email.toLowerCase());
                            }

                            user.setLangKey(langKey);
                            user.setImageUrl(imageUrl);
                            // this.clearUserCaches(user);
                            log.debug("Changed information for user: {}", user);
                        }
                );
    }

    @Transactional
    public boolean changePassword(String currentClearTextPassword, String newPassword) {
        AtomicBoolean passwordChanged = new AtomicBoolean(false);

        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByEmailIgnoreCase)
                .ifPresent(
                        user -> {
                            String currentEncryptedPassword = user.getPassword();

                            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password!");
                            }

                            String encryptedPassword = passwordEncoder.encode(newPassword);
                            user.setPassword(encryptedPassword);
                            log.debug("Changed password for user: {}", user);
                            passwordChanged.set(true);
                        }
                );

        return passwordChanged.get();
    }

   /* @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByEmailIgnoreCase)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password!");
                    }
                    
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    log.debug("Changed password for user: {}", user);
                }
            );
    }*/

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<Users> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByEmail(login);
    }

    @Transactional(readOnly = true)
    public Optional<Users> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByEmail);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(
                        user -> {
                            userRepository.delete(user);
                        }
                );
    }

    @Transactional(propagation = Propagation.NEVER)
    public void wrongPasswordAttentAction(String email) {
        Optional<Users> optionalUsers = userRepository.findOneByEmailIgnoreCase(email);

        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            Integer count = users.getWrongAttemptCount() == null ? 0 : users.getWrongAttemptCount();
            users.setWrongAttemptCount(count + 1);
            userRepository.save(users);
        }
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public ProfileDetailsModel getAllEmployeeDetails(String username) {
        return userRepository.findByEmailOrUsernameIgnoreCaseAndActivated(username, true)
                .map(users -> {
                    ProfileDetailsModel profileDetailsModel = new ProfileDetailsModel();
                    profileDetailsModel.setId(users.getId());
                    // Check for null before accessing fields
                    profileDetailsModel.setEmployeeName(users.getFirstName() != null ? users.getFirstName() : "");
                    profileDetailsModel.setEmployeeImage(users.getImageUrl() != null ? users.getImageUrl() : "");

                    if (users.getEmployee() != null && users.getEmployee().getDesignation() != null) {
                        profileDetailsModel.setDesignation(users.getEmployee().getDesignation().getName() != null ?
                                users.getEmployee().getDesignation().getName() : "");
                    } else {
                        profileDetailsModel.setDesignation("");
                    }

                    profileDetailsModel.setAuthority(users.getAuthorities() != null ?
                            users.getAuthorities().iterator().next().getName() : "");

                    profileDetailsModel.setJoiningDate(users.getEmployee() != null && users.getEmployee().getJoiningDate() != null ?
                            String.valueOf(users.getEmployee().getJoiningDate()) : "");

                    profileDetailsModel.setPhoneNumber(users.getEmployee() != null && users.getEmployee().getPhoneNumber() != null ?
                            users.getEmployee().getPhoneNumber() : "");

                    profileDetailsModel.setEmail(users.getEmail() != null ? users.getEmail() : "");
                    profileDetailsModel.setNid(users.getEmployee() != null && users.getEmployee().getNid() != null ?
                            users.getEmployee().getNid() : "");

                    profileDetailsModel.setPmsId(users.getEmployee() != null && users.getEmployee().getPmsId() != null ?
                            users.getEmployee().getPmsId() : "");

                    return profileDetailsModel;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with username:" + username));
    }

}
