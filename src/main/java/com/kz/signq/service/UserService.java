package com.kz.signq.service;

import com.kz.signq.dto.MessageDto;
import com.kz.signq.dto.user.UserDto;
import com.kz.signq.exception.NoPermissionException;
import com.kz.signq.exception.UserNotFoundException;
import com.kz.signq.model.User;

public interface UserService {

    MessageDto editUserData(UserDto userDto, User currentUser) throws UserNotFoundException, NoPermissionException;

    UserDto getUserData(User currentUser);
}
