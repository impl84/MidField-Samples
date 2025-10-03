
package grpc.v1.client.performer;

import com.midfield_system.grpc.v1.MediaType;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.PerformerControlClient;
import grpc.v1.client.PerformerIoClient;
import grpc.v1.client.PerformerManagerClient;
import grpc.v1.client.SourceInfoClient;

public class DeviceToNetwork
    extends
        ExampleBase
{
    public DeviceToNetwork(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        var sourceInfo = new SourceInfoClient(getManagedChannel());
        var videoEntry = sourceInfo.getDefaultDeviceSourceEntry(MediaType.VIDEO);
        var audioEntry = sourceInfo.getDefaultDeviceSourceEntry(MediaType.AUDIO);
        
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("DeviceToNetwork");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureDeviceSource(instanceId, videoEntry, audioEntry);
        var formatList = performerIo.listOutgoingStreamFormats(instanceId);
        performerIo.configureNetworkOutput(instanceId, formatList);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.controlPerformer(instanceId);
    }
}
