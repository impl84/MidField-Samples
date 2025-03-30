
package grpc.v1.client;

import com.midfield_system.grpc.v1.PerformerManagerGrpc;
import com.midfield_system.grpc.v1.PerformerManagerGrpc.PerformerManagerBlockingStub;
import com.midfield_system.grpc.v1.RegisterPerformerRequest;

import io.grpc.ManagedChannel;

public class PerformerManagerClient
{
    private final PerformerManagerBlockingStub blockingStub;
    
    public PerformerManagerClient(ManagedChannel managedChannel)
    {
        this.blockingStub = PerformerManagerGrpc
            .newBlockingStub(managedChannel);
    }
    
    public String registerPerformer(String description)
    {
        var request = RegisterPerformerRequest.newBuilder()
            .setPerformerDescription(description)
            .build();
        
        var response = this.blockingStub.registerPerformer(request);
        if (response.getInstanceId() == null || response.getInstanceId().isEmpty()) {
            throw new RuntimeException("registerPerformer 失敗: instanceIdが空です");
        }
        return response.getInstanceId();
    }
}
