package com.example.demo.Service;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import com.example.demo.model.Application;
import com.example.demo.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final StateMachineService stateMachineService;

    public void save(Application application) {
        applicationRepository.save(application);

    }

    public List<Application> getAll() {
        return applicationRepository.findAll();
    }

    public void updateState(Long id, String state) {
        Optional<Application> application = applicationRepository.findById(id);
        if(application.isPresent()) {
            StateMachine<ApplicationReviewStates, ApplicationReviewEvents> machine = stateMachineService.getMachine(id, application.get().getApplicationReviewStates());
            machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.valueOf(state)).build())).subscribe();
            application.get().setApplicationReviewStates(machine.getState().getId());
            applicationRepository.save(application.get());
        }

    }
}
