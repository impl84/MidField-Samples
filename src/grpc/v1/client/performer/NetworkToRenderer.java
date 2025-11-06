
package grpc.v1.client.performer;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.PerformerControlClient;
import grpc.v1.client.PerformerIoClient;
import grpc.v1.client.PerformerManagerClient;
import grpc.v1.client.Reporter;
import grpc.v1.client.SourceInfoClient;

public class NetworkToRenderer
    extends
        ExampleBase
{
    private static final String TARGET_NODE = "default";
    
    public NetworkToRenderer(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        var sourceInfo    = new SourceInfoClient(getManagedChannel());
        var networkSource = sourceInfo.getFirstNetworkSource(TARGET_NODE);
        
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("NetworkToRenderer");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureNetworkSource(instanceId, networkSource);
        performerIo.configureRenderer(instanceId);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.openAndStart(instanceId);

        Reporter.readLine("> Enter キーの入力を待ちます．");
        
        performerControl.stopAndClose(instanceId);
    }
}
