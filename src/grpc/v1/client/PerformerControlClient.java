
package grpc.v1.client;

import com.midfield_system.grpc.v1.PerformerControlGrpc;
import com.midfield_system.grpc.v1.PerformerControlGrpc.PerformerControlBlockingStub;
import com.midfield_system.grpc.v1.PerformerControlRequest;
import com.midfield_system.grpc.v1.PerformerControlResponse;

import io.grpc.ManagedChannel;

public class PerformerControlClient
{
    private final PerformerControlBlockingStub blockingStub;
    
    public PerformerControlClient(ManagedChannel managedChannel)
    {
        this.blockingStub = PerformerControlGrpc.newBlockingStub(managedChannel);
    }
    
    public void controlPerformer(String instanceId)
    {
        var request = PerformerControlRequest.newBuilder()
            .setInstanceId(instanceId)
            .build();
        
        PerformerControlResponse response;
        
        response = this.blockingStub.openPerformer(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException("openPerformer 失敗: " + response.getErrorMessage());
        }
        response = this.blockingStub.startPerformer(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "startPerformer 失敗: " + response.getErrorMessage()
            );
        }
        
        Reporter.readLine("> Enter キーの入力を待ちます．");
        
        response = this.blockingStub.stopPerformer(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException("stopPerformer 失敗: " + response.getErrorMessage());
        }
        response = this.blockingStub.closePerformer(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "closePerformer 失敗: " + response.getErrorMessage()
            );
        }
    }
}
