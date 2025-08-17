
package grpc.v1.client.experiment;

import grpc.v1.client.GrpcExampleBase;
import grpc.v1.client.Reporter;

public class GrpcExperiment
    extends
        GrpcExampleBase
{
    public GrpcExperiment(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        while (true) {
            Reporter.println();
            var number = Reporter.readLine(
                "GrpcExperiment> Enter the number (1: Round Trip, 2: Response Stream, 3: Request Stream, 4: Bidirectional Stream, Other: Quit)"
            );
            
            var experimenter = nextGrpcClientExperimenter(number);
            if (experimenter != null) {
                experimenter.doExperiments();
            }
            else {
                Reporter.println("Quit");
                break;
            }
        }
    }
    
    private GrpcClientExperimenter nextGrpcClientExperimenter(String number)
    {
        var experimenter = switch (number) {
        case "1" -> new CltRoundTrip(getManagedChannel());
        case "2" -> new CltResponseStream(getManagedChannel());
        case "3" -> new CltRequestStream(getManagedChannel());
        case "4" -> new CltBidirectionalStream(getManagedChannel());
        default  -> null;
        };
        return experimenter;
    }
}
