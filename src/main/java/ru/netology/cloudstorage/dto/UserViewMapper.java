package ru.netology.cloudstorage.dto;

import org.mapstruct.Mapper;
import ru.netology.cloudstorage.model.CloudUser;
import ru.netology.cloudstorage.repository.UserRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserViewMapper {

    private UserRepository userRepository;

    public abstract UserView toUserView(CloudUser cloudUser);

    public abstract List<UserView> toUserView(List<CloudUser> cloudUsers);

    public UserView toUserViewById(Long id) {
        if (id == null) {
            return null;
        }
        return toUserView(userRepository.getById(id));
    }
}
