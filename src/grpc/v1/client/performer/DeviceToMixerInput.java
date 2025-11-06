
package grpc.v1.client.performer;

import com.midfield_system.grpc.v1.MediaType;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.PerformerControlClient;
import grpc.v1.client.PerformerIoClient;
import grpc.v1.client.PerformerManagerClient;
import grpc.v1.client.Reporter;
import grpc.v1.client.SourceInfoClient;

public class DeviceToMixerInput
    extends
        ExampleBase
{
    public DeviceToMixerInput(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        var sourceInfo = new SourceInfoClient(getManagedChannel());
        var videoEntry = sourceInfo.getDefaultDeviceSourceEntry(MediaType.VIDEO);
        var audioEntry = sourceInfo.getDefaultDeviceSourceEntry(MediaType.AUDIO);
        
        var mixerSource = sourceInfo.getFirstMixerSource();
        
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("DeviceToMixerInput");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureDeviceSource(instanceId, videoEntry, audioEntry);
        performerIo.configureMixerInput(instanceId, mixerSource);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.openAndStart(instanceId);

        Reporter.readLine("> Enter キーの入力を待ちます．");
        
        performerControl.stopAndClose(instanceId);
    }
}
