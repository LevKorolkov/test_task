package com.levkorolkov.test_application.repository;

import com.levkorolkov.test_application.entity.Application;
import com.levkorolkov.test_application.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByUserAndStatus_SentOrderByDateTimeDesc(User user, Pageable pageable);
    List<Application> findAllByUserNameAndStatus_SentOrderByDateTimeDesc(String username, Pageable pageable);
    List<Application> findAllByUserNameAndStatus_SentOrderByDateTimeAsc(String username, Pageable pageable);
    List<Application> findAllByUserAndStatus_SentOrderByDateTimeAsc(User user, Pageable pageable);
    List<Application> findAllApplicationsByStatus_SentOrderByDateTimeDesc(Pageable pageable);
    List<Application> findAllApplicationsByStatus_SentOrderByDateTimeAsc(Pageable pageable);
    Optional<Application> findApplicationsByIdAndUserAndStatus_Sent(Long id, User user);


}
