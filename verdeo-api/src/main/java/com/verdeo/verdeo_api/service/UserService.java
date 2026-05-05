package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.exception.BadRequestException;
import com.verdeo.verdeo_api.exception.ResourceNotFoundException;
import com.verdeo.verdeo_api.model.Role;
import com.verdeo.verdeo_api.model.User;
import com.verdeo.verdeo_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public User register(User user) {

        boolean exists = userRepository.existsByEmailIgnoreCase(user.getEmail());

        if (exists) {
            throw new BadRequestException("El correo ya está registrado");
        }

        user.setEmail(user.getEmail().toLowerCase().trim());
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole(Role.USER);
        user.setActive(true);

        return userRepository.save(user);
    }

    public User update(UUID id, User updated) {
        User existing = getById(id);

        String newEmail = updated.getEmail().toLowerCase().trim();

        if (!existing.getEmail().equalsIgnoreCase(newEmail) &&
                userRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new BadRequestException("El correo ya está en uso");
        }

        existing.setName(updated.getName());
        existing.setEmail(newEmail);

        return userRepository.save(existing);
    }

    public void changePassword(UUID id, String currentPassword, String newPassword) {
        User user = getById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new BadRequestException("Contraseña actual incorrecta");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deactivate(UUID id) {
        User user = getById(id);
        user.setActive(false);
        userRepository.save(user);
    }
}