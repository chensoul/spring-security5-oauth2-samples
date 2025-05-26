package com.chensoul.security;

import com.chensoul.security.entity.Workout;
import com.chensoul.security.repository.WorkoutRepository;
import com.chensoul.security.service.WorkoutService;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class})
class MethodTests {

    @Autowired
    private WorkoutService workoutService;

    @MockBean
    private WorkoutRepository workoutRepository;

    @Test
    void testProductServiceWithNoUser() {
        Workout w = new Workout();
        assertThrows(AuthenticationException.class,
                () -> workoutService.saveWorkout(w));
    }

    @Test
    @WithMockUser(username = "bill")
    void testSaveWorkoutWorksOnlyForAuthenticatedUser() {
        Workout w = new Workout();
        w.setUser("bill");
        workoutService.saveWorkout(w);
    }

}
