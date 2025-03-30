
package grpc.v1.client;

import com.midfield_system.grpc.v1.ConfigureDestinationRequest;
import com.midfield_system.grpc.v1.ConfigureSourceRequest;
import com.midfield_system.grpc.v1.DeviceSourceSettings;
import com.midfield_system.grpc.v1.NetworkSource;
import com.midfield_system.grpc.v1.NetworkSourceSettings;
import com.midfield_system.grpc.v1.PerformerIoGrpc;
import com.midfield_system.grpc.v1.PerformerIoGrpc.PerformerIoBlockingStub;
import com.midfield_system.grpc.v1.RendererSettings;

import io.grpc.ManagedChannel;

public class PerformerIoClient
{
    private final PerformerIoBlockingStub blockingStub;
    
    public PerformerIoClient(ManagedChannel managedChannel)
    {
        this.blockingStub = PerformerIoGrpc.newBlockingStub(managedChannel);
    }
    
    public void configureDeviceSource(
        String instanceId, DeviceSourceEntry video, DeviceSourceEntry audio
    )
    {
        var deviceSourceSettings = DeviceSourceSettings.newBuilder()
            .setVideoDeviceSourceId(video.getDeviceSourceId())
            .setVideoFormatIndex(video.getFormatIndex())
            .setAudioDeviceSourceId(audio.getDeviceSourceId())
            .setAudioFormatIndex(audio.getFormatIndex())
            .build();
        
        var request = ConfigureSourceRequest.newBuilder()
            .setInstanceId(instanceId)
            .setDeviceSourceSettings(deviceSourceSettings)
            .build();
        
        var response = this.blockingStub.configureSource(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "configureDeviceSource 失敗: " + response.getErrorMessage()
            );
        }
    }
    
    public void configureNetworkSource(String instanceId, NetworkSource networkSource)
    {
        var networkSourceSettings = NetworkSourceSettings.newBuilder()
            .setNetworkSourceId(networkSource.getNetworkSourceId())
            .build();
        
        var request = ConfigureSourceRequest.newBuilder()
            .setInstanceId(instanceId)
            .setNetworkSourceSettings(networkSourceSettings)
            .build();
        
        var response = this.blockingStub.configureSource(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "configureNetworkSource 失敗: " + response.getErrorMessage()
            );
        }
    }
    
    public void configureRenderer(String instanceId)
    {
        var rendererSettings = RendererSettings.newBuilder()
            .setRendererId("default")
            .build();
        
        var request = ConfigureDestinationRequest.newBuilder()
            .setInstanceId(instanceId)
            .setRendererSettings(rendererSettings)
            .build();
        
        var response = this.blockingStub.configureDestination(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "configureRenderer 失敗: " + response.getErrorMessage()
            );
        }
    }
}
