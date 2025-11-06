
package grpc.v1.client;

import com.midfield_system.grpc.v1.MixerControlGrpc;
import com.midfield_system.grpc.v1.MixerControlGrpc.MixerControlStub;
import com.midfield_system.grpc.v1.MouseEvent.Action;
import com.midfield_system.grpc.v1.UiEventRequest;
import com.midfield_system.grpc.v1.UiEventStreamResult;

import io.grpc.ManagedChannel;
import io.grpc.stub.ClientCallStreamObserver;

public class MixerControlClient
{
    private final MixerControlStub asyncStub;
    
    private final ClientCallStreamObserver<UiEventRequest> requestObserver;
    
    public MixerControlClient(ManagedChannel managedChannel)
    {
        this.asyncStub       = MixerControlGrpc.newStub(managedChannel);
        this.requestObserver = (ClientCallStreamObserver<UiEventRequest>)this.asyncStub
            .streamUiEvents(
                new UiEventResponseObserver()
            );
    }
    
    public void streamUiEvent(String instanceId, Action action, int x, int y)
    {
        var mouseEvent = com.midfield_system.grpc.v1.MouseEvent.newBuilder()
            .setAction(action)
            .setX(x)
            .setY(y)
            .build();
        
        var request = UiEventRequest.newBuilder()
            .setPerformerId(instanceId)
            .setMouseEvent(mouseEvent)
            .build();
        
        this.requestObserver.onNext(request);
    }
    
    public void close()
    {
        this.requestObserver.onCompleted();
    }
}

class UiEventResponseObserver
    implements
        io.grpc.stub.StreamObserver<UiEventStreamResult>
{
    @Override
    public void onCompleted()
    {
        Reporter.message("UiEventResponseObserver#onCompleted");
    }
    
    @Override
    public void onError(Throwable th)
    {
        Reporter.error("UiEventResponseObserver#onError>", th);
    }
    
    @Override
    public void onNext(UiEventStreamResult response)
    {
        Reporter.message("UiEventResponseObserver#onNext> " + response);
    }
}
