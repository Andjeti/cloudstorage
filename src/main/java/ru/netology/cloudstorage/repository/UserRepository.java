package ru.netology.cloudstorage.repository;

import ru.netology.cloudstorage.model.CloudUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CloudUser, Long> {

    Optional<CloudUser> findByUsername(String username);
}
