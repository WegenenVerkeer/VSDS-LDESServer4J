// package
// be.vlaanderen.informatievlaanderen.ldes.server.fragmentisers.mediator;
//
// import
// be.vlaanderen.informatievlaanderen.ldes.server.domain.ldesfragment.services.FragmentationExecutor;
// import
// be.vlaanderen.informatievlaanderen.ldes.server.domain.ldesfragment.services.FragmentationMediator;
// import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
// import org.awaitility.Awaitility;
// import org.awaitility.Durations;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// TODO fix
// import static org.awaitility.Awaitility.await;
// import static org.mockito.Mockito.*;
//
// class InMemoryFragmentationMediatorTest {
//
// private FragmentationMediator fragmentationMediator;
//
// private final FragmentationExecutor fragmentationExecutor =
// mock(FragmentationExecutor.class);
//
// @BeforeEach
// void setUp() {
// fragmentationMediator = new
// InMemoryFragmentationMediator(fragmentationExecutor, new
// SimpleMeterRegistry());
// }
//
// @Test
// @DisplayName("Adding a member to the queue")
// void
// when_MemberIsAddedForFragmentation_AThreadIsStartedWhichCallsTheFragmentationService()
// {
// Awaitility.reset();
// fragmentationMediator.addMemberToFragment("someMember");
//
// await()
// .pollDelay(Durations.ONE_MILLISECOND)
// .atMost(Durations.ONE_HUNDRED_MILLISECONDS);
//
// verify(fragmentationExecutor, times(1)).executeFragmentation("someMember");
// }
//
// }