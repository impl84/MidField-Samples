
package grpc.v1.client;

import com.midfield_system.grpc.v1.AudioFormat;
import com.midfield_system.grpc.v1.ConfigureDestinationRequest;
import com.midfield_system.grpc.v1.ConfigureSourceRequest;
import com.midfield_system.grpc.v1.ConnectionMode;
import com.midfield_system.grpc.v1.DeviceSourceSettings;
import com.midfield_system.grpc.v1.ListOutgoingStreamFormatsRequest;
import com.midfield_system.grpc.v1.ListOutgoingStreamFormatsResponse;
import com.midfield_system.grpc.v1.MixerInputSettings;
import com.midfield_system.grpc.v1.MixerSource;
import com.midfield_system.grpc.v1.MixerSourceSettings;
import com.midfield_system.grpc.v1.NetworkOutputSettings;
import com.midfield_system.grpc.v1.NetworkSource;
import com.midfield_system.grpc.v1.NetworkSourceSettings;
import com.midfield_system.grpc.v1.PerformerIoGrpc;
import com.midfield_system.grpc.v1.PerformerIoGrpc.PerformerIoBlockingStub;
import com.midfield_system.grpc.v1.ProtocolType;
import com.midfield_system.grpc.v1.RendererSettings;
import com.midfield_system.grpc.v1.VideoFormat;

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
            .setVideoDeviceSourceIndex(video.getDeviceSourceIndex())
            .setVideoFormatIndex(video.getFormatIndex())
            .setAudioDeviceSourceIndex(audio.getDeviceSourceIndex())
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
    
    public void configureMixerInput(String instanceId, MixerSource mixerSource)
    {
        var mixerInputSettings = MixerInputSettings.newBuilder()
            .setMixerSourceId(mixerSource.getMixerSourceId())
            .build();
        
        var request = ConfigureDestinationRequest.newBuilder()
            .setInstanceId(instanceId)
            .setMixerInputSettings(mixerInputSettings)
            .build();
        
        var response = this.blockingStub.configureDestination(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "configureMixerInput 失敗: " + response.getErrorMessage()
            );
        }
    }
    
    public void configureMixerSource(String instanceId, String mixerName)
    {
        var videoFormat = VideoFormat.newBuilder()
            .setWidth(1280)
            .setHeight(720)
            .setBitsPerPixel(32)
            .setFrameRate(30)
            .build();
        
        var audioFormat = AudioFormat.newBuilder()
            .setChannels(2)
            .setBitsPerSample(16)
            .setSampleRateHz(48000)
            .build();
        
        var mixerSourceSettings = MixerSourceSettings.newBuilder()
            .setMixerName(mixerName)
            .setVideoFormat(videoFormat)
            .setAudioFormat(audioFormat)
            .build();
        
        var request = ConfigureSourceRequest.newBuilder()
            .setInstanceId(instanceId)
            .setMixerSourceSettings(mixerSourceSettings)
            .build();
        
        var response = this.blockingStub.configureSource(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "configureMixerSource 失敗: " + response.getErrorMessage()
            );
        }
    }
    
    public void configureNetworkOutput(
        String instanceId, ListOutgoingStreamFormatsResponse formatList
    )
    {
        var networkOutputSettings = NetworkOutputSettings.newBuilder()
            .setVideoFormatIndex(formatList.getDefaultVideoFormatIndex())
            .setAudioFormatIndex(formatList.getDefaultAudioFormatIndex())
            .setProtocolType(ProtocolType.TCP)
            .setConnectionMode(ConnectionMode.PASSIVE)
            .setPreviewable(true)
            .build();
        
        var request = ConfigureDestinationRequest.newBuilder()
            .setInstanceId(instanceId)
            .setNetworkOutputSettings(networkOutputSettings)
            .build();
        
        var response = this.blockingStub.configureDestination(request);
        if (response.getSuccess() == false) {
            throw new RuntimeException(
                "configureNetworkOutput 失敗: " + response.getErrorMessage()
            );
        }
    }
    
    public void configureNetworkSource(String instanceId, NetworkSource networkSource)
    {
        var networkSourceSettings = NetworkSourceSettings.newBuilder()
            .setNetworkSourceIndex(networkSource.getNetworkSourceIndex())
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
    
    public ListOutgoingStreamFormatsResponse listOutgoingStreamFormats(String instanceId)
    {
        var request = ListOutgoingStreamFormatsRequest.newBuilder()
            .setInstanceId(instanceId)
            .build();
        
        var response = this.blockingStub.listOutgoingStreamFormats(request);
        return response;
    }
}
