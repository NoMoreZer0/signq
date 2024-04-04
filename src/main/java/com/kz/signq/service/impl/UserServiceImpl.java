package com.kz.signq.service.impl;

import com.kz.signq.db.UserDb;
import com.kz.signq.dto.MessageDto;
import com.kz.signq.dto.user.UserDto;
import com.kz.signq.exception.NoPermissionException;
import com.kz.signq.exception.UserNotFoundException;
import com.kz.signq.model.User;
import com.kz.signq.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kz.signq.utils.ErrorCodeUtil.ERR_NO_PERMISSION;
import static com.kz.signq.utils.ErrorCodeUtil.ERR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDb db;

    @Override
    public MessageDto editUserData(UserDto userDto, User currentUser) throws UserNotFoundException, NoPermissionException {
        Optional<User> opt = db.findById(userDto.getId());
        if (opt.isEmpty()) {
            throw new UserNotFoundException(ERR_USER_NOT_FOUND.name(), "User not found!");
        }
        var user = opt.get();
        var dbUser = db.findUserByEmail(userDto.getEmail());
        if (dbUser.isEmpty() || !dbUser.get().getId().equals(currentUser.getId())) {
            throw new NoPermissionException(
                    ERR_NO_PERMISSION.name(),
                    "You have no permission to edit data!"
            );
        }
        var editedUser = User.builder()
                .iin(userDto.getIin())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phoneNumber(userDto.getPhoneNumber())
                .role(currentUser.getRole())
                .build();
        editedUser.setId(user.getId());
        db.save(editedUser);
        return MessageDto.builder().msg("Data edited successfully").build();
    }

    @Override
    public UserDto getUserData(User currentUser) {
        return UserDto.builder()
                .id(currentUser.getId())
                .name(currentUser.getName())
                .email(currentUser.getEmail())
                .iin(currentUser.getIin())
                .phoneNumber(currentUser.getPhoneNumber())
                .build();
    }
}
