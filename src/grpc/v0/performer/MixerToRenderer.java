
package grpc.v0.performer;

import java.io.IOException;

import com.midfield_system.grpc.v0.ConfigureInputDeviceRequest;
import com.midfield_system.grpc.v0.ConfigureMixerInputRequest;
import com.midfield_system.grpc.v0.ConfigureRendererRequest;
import com.midfield_system.grpc.v0.ConfigureStreamingMixerRequest;
import com.midfield_system.grpc.v0.CreateStreamPerformerRequest;
import com.midfield_system.grpc.v0.DeleteSegmentIoRequest;
import com.midfield_system.grpc.v0.DeviceInfoProviderGrpc;
import com.midfield_system.grpc.v0.DeviceInfoProviderGrpc.DeviceInfoProviderBlockingStub;
import com.midfield_system.grpc.v0.ListDeviceInfoRequest;
import com.midfield_system.grpc.v0.MediaType;
import com.midfield_system.grpc.v0.OperationRequest;
import com.midfield_system.grpc.v0.SegmentIoGrpc;
import com.midfield_system.grpc.v0.SegmentIoGrpc.SegmentIoBlockingStub;
import com.midfield_system.grpc.v0.StreamPerformerGrpc;
import com.midfield_system.grpc.v0.StreamPerformerGrpc.StreamPerformerBlockingStub;

public class MixerToRenderer
    extends
        MfsGrpcExample
{
    private static final String MIXER_NAME = "Experimental Mixer";
    
    private final DeviceInfoProviderBlockingStub devInfProv;
    private final StreamPerformerBlockingStub    performer;
    private final SegmentIoBlockingStub          segment;
    
    MixerToRenderer(String serverAddress, int portNumber)
    {
        super(serverAddress, portNumber);
        
        this.devInfProv = DeviceInfoProviderGrpc.newBlockingStub(getManagedChannel());
        this.segment    = SegmentIoGrpc.newBlockingStub(getManagedChannel());
        this.performer  = StreamPerformerGrpc.newBlockingStub(getManagedChannel());
    }
    
    @Override
    public void execute()
    {
        // ミキサー
        String mixSegIoId = this.segment.createSegmentIo(null).getSegmentIoId();
        this.segment.configureStreamingMixer(
            ConfigureStreamingMixerRequest.newBuilder()
                .setSegmentIoId(mixSegIoId)
                .setMixerName(MIXER_NAME)
                .build()
        );
        this.segment.configureRenderer(
            ConfigureRendererRequest.newBuilder()
                .setSegmentIoId(mixSegIoId)
                .build()
        );
        var mixPerfId = this.performer.createStreamPerformer(
            CreateStreamPerformerRequest.newBuilder()
                .setSegmentIoId(mixSegIoId)
                .build()
        ).getPerformerId();
        
        var mixOpReq = OperationRequest.newBuilder()
            .setPerformerId(mixPerfId)
            .build();
        this.performer.startStreamPerformer(mixOpReq);
        
        // ミキサー入力
        var vidRes    = this.devInfProv.listDeviceInfo(
            ListDeviceInfoRequest.newBuilder()
                .setMediaType(MediaType.VIDEO)
                .build()
        );
        var vidDevIdx = vidRes.getDefaultDeviceInfoIndex();
        var vidDevInf = vidRes.getDeviceInfoList().get(vidDevIdx);
        
        var audRes    = this.devInfProv.listDeviceInfo(
            ListDeviceInfoRequest.newBuilder()
                .setMediaType(MediaType.AUDIO)
                .build()
        );
        var audDevIdx = audRes.getDefaultDeviceInfoIndex();
        var audDevInf = audRes.getDeviceInfoList().get(audDevIdx);
        
        String mixInSegIoId = this.segment.createSegmentIo(null).getSegmentIoId();
        this.segment.configureInputDevice(
            ConfigureInputDeviceRequest.newBuilder()
                .setSegmentIoId(mixInSegIoId)
                .setVideoDeviceIndex(vidDevIdx)
                .setVideoFormatIndex(vidDevInf.getDefaultFormatIndex())
                .setAudioDeviceIndex(audDevIdx)
                .setAudioFormatIndex(audDevInf.getDefaultFormatIndex())
                .build()
        );
        this.segment.configureMixerInput(
            ConfigureMixerInputRequest.newBuilder()
                .setSegmentIoId(mixInSegIoId)
                .setMixerId(mixPerfId)
                .build()
        );
        var mixInPerfId = this.performer.createStreamPerformer(
            CreateStreamPerformerRequest.newBuilder()
                .setSegmentIoId(mixInSegIoId)
                .build()
        ).getPerformerId();
        
        var mixInOpReq = OperationRequest.newBuilder()
            .setPerformerId(mixInPerfId)
            .build();
        this.performer.startStreamPerformer(mixInOpReq);
        
        try {
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        this.performer.stopStreamPerformer(mixInOpReq);
        this.performer.deleteStreamPerformer(mixInOpReq);
        this.segment.deleteSegmentIo(
            DeleteSegmentIoRequest.newBuilder()
                .setSegmentIoId(mixInSegIoId)
                .build()
        );
        this.performer.stopStreamPerformer(mixOpReq);
        this.performer.deleteStreamPerformer(mixOpReq);
        this.segment.deleteSegmentIo(
            DeleteSegmentIoRequest.newBuilder()
                .setSegmentIoId(mixSegIoId)
                .build()
        );
    }
}
