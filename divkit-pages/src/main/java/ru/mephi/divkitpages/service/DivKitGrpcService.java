package ru.mephi.divkitpages.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.mephi.gprc.divkitpages.AllDivKitResponse;
import ru.mephi.gprc.divkitpages.DivKitCreateRequest;
import ru.mephi.gprc.divkitpages.DivKitRequest;
import ru.mephi.gprc.divkitpages.DivKitResponse;
import ru.mephi.gprc.divkitpages.DivKitUpdateRequest;

@Service
@RequiredArgsConstructor
public class DivKitGrpcService extends DivKitServiceGrpc.DivKitServiceImplBase {

    private final DivKitService divKitService;
    private final DivKitGrpcMapper mapper;

    @Override
    public void getDivKit(DivKitRequest request,
                          StreamObserver<DivKitResponse> responseObserver
    ) {
        String screenId = request.getId();
        divKitService.getPage(screenId)
                .map(mapper::modelToDto)
                .subscribe(
                        responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted
                );
    }

    @Override
    public void getAllDivKits(Empty request,
                                 StreamObserver<AllDivKitResponse> responseObserver
    ) {
        divKitService.getAllPages()
                .collectList()
                .map(mapper::modelsToDto)
                .subscribe(
                        responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted
                );
    }

    @Override
    public void createDivKit(DivKitCreateRequest request,
                             StreamObserver<Empty> responseObserver
    ) {
        Mono.just(request)
                .map(mapper::dtoToModel)
                .flatMap(divKitService::createPage)
                .subscribe(
                        (response) -> responseObserver.onNext(Empty.getDefaultInstance()),
                        responseObserver::onError,
                        responseObserver::onCompleted
                );
    }

    @Override
    public void updateDivKit(DivKitUpdateRequest request,
                             StreamObserver<Empty> responseObserver
    ) {
        Mono.just(request)
                .map(mapper::dtoToModel)
                .flatMap(divKitService::updatePage)
                .subscribe(
                        (response) -> responseObserver.onNext(Empty.getDefaultInstance()),
                        responseObserver::onError,
                        responseObserver::onCompleted
                );
    }

    @Override
    public void removeDivKit(DivKitRequest request,
                                StreamObserver<Empty> responseObserver
    ) {
        String screenId = request.getId();
        divKitService.deletePage(screenId)
                .then(Mono.just(ResponseEntity.ok()))
                .subscribe(
                        (response) -> responseObserver.onNext(Empty.getDefaultInstance()),
                        responseObserver::onError,
                        responseObserver::onCompleted
                );
    }
}