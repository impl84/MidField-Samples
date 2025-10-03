
package grpc.v1.client.performer;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.PerformerControlClient;
import grpc.v1.client.PerformerIoClient;
import grpc.v1.client.PerformerManagerClient;
import grpc.v1.client.SourceInfoClient;

public class NetworkToNetwork
    extends
        ExampleBase
{
    private static final String TARGET_NODE = "default";
    
    public NetworkToNetwork(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        var sourceInfo    = new SourceInfoClient(getManagedChannel());
        var networkSource = sourceInfo.getFirstNetworkSource(TARGET_NODE);
        
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("NetworkToNetwork");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureNetworkSource(instanceId, networkSource);
        var formatList = performerIo.listOutgoingStreamFormats(instanceId);
        performerIo.configureNetworkOutput(instanceId, formatList);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.controlPerformer(instanceId);
    }
}
