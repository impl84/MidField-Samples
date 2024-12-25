
package grpc.v0.performer;

import java.io.IOException;

import com.midfield_system.grpc.v0.ConfigureInputDeviceRequest;
import com.midfield_system.grpc.v0.ConfigureRendererRequest;
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

public class DeviceToRenderer
    extends
        MfsGrpcExample
{
    private final DeviceInfoProviderBlockingStub devInfProv;
    private final StreamPerformerBlockingStub    performer;
    private final SegmentIoBlockingStub          segment;
    
    DeviceToRenderer(String serverAddress, int portNumber)
    {
        super(serverAddress, portNumber);
        
        this.devInfProv = DeviceInfoProviderGrpc.newBlockingStub(getManagedChannel());
        this.segment    = SegmentIoGrpc.newBlockingStub(getManagedChannel());
        this.performer  = StreamPerformerGrpc.newBlockingStub(getManagedChannel());
    }
    
    @Override
    public void execute()
    {
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
        
        String segIoId = this.segment.createSegmentIo(null).getSegmentIoId();
        this.segment.configureInputDevice(
            ConfigureInputDeviceRequest.newBuilder()
                .setSegmentIoId(segIoId)
                .setVideoDeviceIndex(vidDevIdx)
                .setVideoFormatIndex(vidDevInf.getDefaultFormatIndex())
                .setAudioDeviceIndex(audDevIdx)
                .setAudioFormatIndex(audDevInf.getDefaultFormatIndex())
                .build()
        );
        this.segment.configureRenderer(
            ConfigureRendererRequest.newBuilder()
                .setSegmentIoId(segIoId)
                .build()
        );
        var perfId = this.performer.createStreamPerformer(
            CreateStreamPerformerRequest.newBuilder()
                .setSegmentIoId(segIoId)
                .build()
        ).getPerformerId();
        
        var opReq = OperationRequest.newBuilder()
            .setPerformerId(perfId)
            .build();
        this.performer.startStreamPerformer(opReq);
        
        try {
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        this.performer.stopStreamPerformer(opReq);
        this.performer.deleteStreamPerformer(opReq);
        this.segment.deleteSegmentIo(
            DeleteSegmentIoRequest.newBuilder()
                .setSegmentIoId(segIoId)
                .build()
        );
    }
}
