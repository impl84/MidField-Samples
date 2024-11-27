
package grpc.performer;

import java.io.IOException;

import com.midfield_system.grpc.v1.ConfigureIncomingStreamRequest;
import com.midfield_system.grpc.v1.ConfigureOutgoingStreamRequest;
import com.midfield_system.grpc.v1.ConnectionMode;
import com.midfield_system.grpc.v1.CreateStreamPerformerRequest;
import com.midfield_system.grpc.v1.DeleteSegmentIoRequest;
import com.midfield_system.grpc.v1.ListOutgoingStreamFormatRequest;
import com.midfield_system.grpc.v1.ListStreamInfoRequest;
import com.midfield_system.grpc.v1.OperationRequest;
import com.midfield_system.grpc.v1.ProtocolType;
import com.midfield_system.grpc.v1.SegmentIoGrpc;
import com.midfield_system.grpc.v1.SegmentIoGrpc.SegmentIoBlockingStub;
import com.midfield_system.grpc.v1.StreamInfoProviderGrpc;
import com.midfield_system.grpc.v1.StreamInfoProviderGrpc.StreamInfoProviderBlockingStub;
import com.midfield_system.grpc.v1.StreamPerformerGrpc;
import com.midfield_system.grpc.v1.StreamPerformerGrpc.StreamPerformerBlockingStub;

public class NetworkToNetwork
    extends
        MfsGrpcExample
{
    private final StreamPerformerBlockingStub performer;
    private final SegmentIoBlockingStub       segment;
    private final String                      sourceAddress;
    
    private final StreamInfoProviderBlockingStub stmInfProv;
    
    NetworkToNetwork(String serverAddress, int portNumber, String sourceAddress)
    {
        super(serverAddress, portNumber);
        this.sourceAddress = sourceAddress;
        
        this.stmInfProv = StreamInfoProviderGrpc.newBlockingStub(getManagedChannel());
        this.segment    = SegmentIoGrpc.newBlockingStub(getManagedChannel());
        this.performer  = StreamPerformerGrpc.newBlockingStub(getManagedChannel());
    }
    
    @Override
    public void execute()
    {
        var stmReq    = ListStreamInfoRequest.newBuilder()
            .setSourceNodeAddress(this.sourceAddress)
            .build();
        var stmRes    = this.stmInfProv.listStreamInfo(stmReq);
        var stmInfIdx = stmRes.getDefaultStreamInfoIndex();
        
        String segIoId = this.segment.createSegmentIo(null).getSegmentIoId();
        this.segment.configureIncomingStream(
            ConfigureIncomingStreamRequest.newBuilder()
                .setSegmentIoId(segIoId)
                .setStreamInfoIndex(stmInfIdx)
                .build()
        );
        var outFmtRes = this.segment.listOutgoingStreamFormat(
            ListOutgoingStreamFormatRequest.newBuilder()
                .setSegmentIoId(segIoId)
                .build()
        );
        this.segment.configureOutgoingStream(
            ConfigureOutgoingStreamRequest.newBuilder()
                .setSegmentIoId(segIoId)
                .setVideoFormatIndex(outFmtRes.getDefaultVideoFormatIndex())
                .setAudioFormatIndex(outFmtRes.getDefaultAudioFormatIndex())
                .setProtocolType(ProtocolType.TCP)
                .setConnectionMode(ConnectionMode.PASSIVE)
                .setPreviewable(true)
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
