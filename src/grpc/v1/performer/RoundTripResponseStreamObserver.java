
package grpc.v1.performer;

import com.midfield_system.grpc.v1.ExperimentalResponse;

import io.grpc.stub.StreamObserver;

public class RoundTripResponseStreamObserver
    implements
        StreamObserver<ExperimentalResponse>
{
    @Override
    public void onCompleted()
    {
        System.out.println("[clt] stub: Round trip completed.");
    }
    
    @Override
    public void onError(Throwable t)
    {
        System.err.println("[clt] stub: Error: " + t.getMessage());
    }
    
    @Override
    public void onNext(ExperimentalResponse value)
    {
        System.out
            .println("[clt] stub: Received response: " + value.getResponseMessage());
    }
}
