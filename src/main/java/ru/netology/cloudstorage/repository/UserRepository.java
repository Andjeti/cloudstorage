package ru.netology.cloudstorage.repository;

import ru.netology.cloudstorage.model.CloudUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CloudUser, Long> {

    CloudUser findByUsername(String username);
}
