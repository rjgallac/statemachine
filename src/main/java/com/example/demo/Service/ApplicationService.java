package com.example.demo.Service;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import com.example.demo.model.Application;
import com.example.demo.model.ApplicationDto;
import com.example.demo.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void updateState(Long id, String event) {
        Optional<Application> application = applicationRepository.findById(id);
        if(application.isPresent()) {
            StateMachine<ApplicationReviewStates, ApplicationReviewEvents> machine = stateMachineService.getMachine(id, application.get().getApplicationReviewStates());
            machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.valueOf(event)).build())).subscribe();
            application.get().setApplicationReviewStates(machine.getState().getId());
            applicationRepository.save(application.get());
        }

    }

    public ApplicationDto getById(long id) {
        Optional<Application> application = applicationRepository.findById(id);
        StateMachine<ApplicationReviewStates, ApplicationReviewEvents> machine = stateMachineService.getMachine(id, application.get().getApplicationReviewStates());

        List<String> collect = machine.getTransitions().stream()
                .filter(transition -> transition.getSource().getId().equals(machine.getState().getId()))
                .map(transition -> transition.getTrigger().getEvent())
                .map(t ->t.name())
                .collect(Collectors.toList());
        application.get().setApplicationReviewStates(machine.getState().getId());
        Application application1 = application.get();
        ApplicationDto applicationDto = ApplicationDto
                .builder()
                .id(application1.getId())
                .date(application1.getDate())
                .events(collect)
                .build();
        return applicationDto;
    }

    public void tester() {
        StateMachine<ApplicationReviewStates, ApplicationReviewEvents> machine = stateMachineService.getMachineDefault();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.SUBMIT).build())).subscribe();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.REVIEWED).build())).subscribe();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.APPROVE).build())).subscribe();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.CLOSE).build())).subscribe();


    }
}
