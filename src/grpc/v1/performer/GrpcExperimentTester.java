
package grpc.v1.performer;

import java.io.IOException;

import com.midfield_system.grpc.v1.GrpcExperimentGrpc;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc.GrpcExperimentBlockingStub;

public class GrpcExperimentTester
    extends
        MfsGrpcExampleBase
{
    private final GrpcExperimentBlockingStub grpcExperiment;
    
    GrpcExperimentTester(String serverAddress, int portNumber)
    {
        super(serverAddress, portNumber);
        
        this.grpcExperiment = GrpcExperimentGrpc.newBlockingStub(getManagedChannel());
    }
    
    @Override
    public void execute()
    {
        try {
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
