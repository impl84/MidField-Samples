
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
    private final String targetNode;
    
    public NetworkToNetwork(String host, int port, String targetNode)
    {
        super(host, port);
        this.targetNode = targetNode;
    }
    
    @Override
    public void execute()
    {
        var sourceInfo    = new SourceInfoClient(getManagedChannel());
        var networkSource = sourceInfo.getFirstNetworkSource(this.targetNode);
        
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("NetworkToNetwork");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureNetworkSource(instanceId, networkSource);
        var formatList = performerIo.listOutgoingStreamFormat(instanceId);
        performerIo.configureNetworkOutput(instanceId, formatList);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.controlPerformer(instanceId);
    }
}
