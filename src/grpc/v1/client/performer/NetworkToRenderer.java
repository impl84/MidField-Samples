
package grpc.v1.client.performer;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.PerformerControlClient;
import grpc.v1.client.PerformerIoClient;
import grpc.v1.client.PerformerManagerClient;
import grpc.v1.client.SourceInfoClient;
import io.grpc.StatusRuntimeException;

public class NetworkToRenderer
    extends
        ExampleBase
{
    private final String targetNode;
    
    public NetworkToRenderer(String host, int port, String targetNode)
    {
        super(host, port);
        this.targetNode = targetNode;
    }
    
    @Override
    public void execute()
    {
        try {
            setupAndControlPerformer();
        }
        catch (StatusRuntimeException ex) {
            System.err.println("gRPCエラー: " + ex.getStatus() + " - " + ex.getMessage());
        }
        catch (Exception ex) {
            System.err.println("実行時エラー: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void setupAndControlPerformer()
    {
        var sourceInfo    = new SourceInfoClient(getManagedChannel());
        var networkSource = sourceInfo.getDefaultNetworkSource(this.targetNode);
        
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("NetworkToRenderer");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureNetworkSource(instanceId, networkSource);
        performerIo.configureRenderer(instanceId);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.controlPerformer(instanceId);
    }
}
