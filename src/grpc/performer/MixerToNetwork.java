
package grpc.performer;

import java.io.IOException;

import com.midfield_system.grpc.v1.ConfigureInputDeviceRequest;
import com.midfield_system.grpc.v1.ConfigureMixerInputRequest;
import com.midfield_system.grpc.v1.ConfigureOutgoingStreamRequest;
import com.midfield_system.grpc.v1.ConfigureStreamingMixerRequest;
import com.midfield_system.grpc.v1.ConnectionMode;
import com.midfield_system.grpc.v1.CreateStreamPerformerRequest;
import com.midfield_system.grpc.v1.DeleteSegmentIoRequest;
import com.midfield_system.grpc.v1.DeviceInfoProviderGrpc;
import com.midfield_system.grpc.v1.DeviceInfoProviderGrpc.DeviceInfoProviderBlockingStub;
import com.midfield_system.grpc.v1.ListDeviceInfoRequest;
import com.midfield_system.grpc.v1.ListOutgoingStreamFormatRequest;
import com.midfield_system.grpc.v1.MediaType;
import com.midfield_system.grpc.v1.OperationRequest;
import com.midfield_system.grpc.v1.ProtocolType;
import com.midfield_system.grpc.v1.SegmentIoGrpc;
import com.midfield_system.grpc.v1.SegmentIoGrpc.SegmentIoBlockingStub;
import com.midfield_system.grpc.v1.StreamPerformerGrpc;
import com.midfield_system.grpc.v1.StreamPerformerGrpc.StreamPerformerBlockingStub;

public class MixerToNetwork
    extends
        MfsGrpcExample
{
    private static final String MIXER_NAME = "Experimental Mixer";
    
    private final DeviceInfoProviderBlockingStub devInfProv;
    private final StreamPerformerBlockingStub    performer;
    private final SegmentIoBlockingStub          segment;
    
    MixerToNetwork(String serverAddress, int portNumber)
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
        var outFmtRes = this.segment.listOutgoingStreamFormat(
            ListOutgoingStreamFormatRequest.newBuilder()
                .setSegmentIoId(mixSegIoId)
                .build()
        );
        this.segment.configureOutgoingStream(
            ConfigureOutgoingStreamRequest.newBuilder()
                .setSegmentIoId(mixSegIoId)
                .setVideoFormatIndex(outFmtRes.getDefaultVideoFormatIndex())
                .setAudioFormatIndex(outFmtRes.getDefaultAudioFormatIndex())
                .setProtocolType(ProtocolType.TCP)
                .setConnectionMode(ConnectionMode.PASSIVE)
                .setPreviewable(true)
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
