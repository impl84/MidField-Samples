
package grpc.v1.client.experiment;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.Reporter;

public class GrpcExperimentExample
    extends
        ExampleBase
{
    public GrpcExperimentExample(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        while (true) {
            Reporter.message("");
            Reporter.message("--------------------------------------------------------");
            Reporter.message("> 1: Round Trip");
            Reporter.message("> 2: Response Stream");
            Reporter.message("> 3: Request Stream");
            Reporter.message("> 4: Bidirectional Stream");
            Reporter.message("> --");
            Reporter.message("> The other: Quit");
            var number = Reporter.readLine("> Enter the number: ");
            
            var experimenter = nextGrpcClientExperimenter(number);
            if (experimenter != null) {
                experimenter.doExperiments();
            }
            else {
                Reporter.message("Quit");
                break;
            }
        }
    }
    
    private Experimenter nextGrpcClientExperimenter(String number)
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
