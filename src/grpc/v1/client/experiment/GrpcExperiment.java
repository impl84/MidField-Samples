
package grpc.v1.client.experiment;

import java.io.BufferedReader;

import grpc.v1.client.MfsGrpcExampleBase;

public class GrpcExperiment
    extends
        MfsGrpcExampleBase
{
    public GrpcExperiment(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        GrpcClientExperimenter[] experimenters = {
            new CltRoundTrip(getManagedChannel()),
            new CltResponseStream(getManagedChannel()),
            new CltRequestStream(getManagedChannel()),
            new CltBidirectionalStream(getManagedChannel())
        };
        for (GrpcClientExperimenter experimenter : experimenters) {
            experimenter.doExperiments();
        }
/*        
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("[clt] > GrpcExperimentTester#execute() を開始します．");
            
            this.interactiveLoop(reader);
            
            System.out.println("[clt] > GrpcExperimentTester#execute() を終了します．");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
*/        
    }
    
    private void interactiveLoop(BufferedReader reader)
    {
        while (true) {
            try {
                System.out.println(
                    "[clt] Enter a command (1: Request Stream, 2: Response Stream, 3: Round Trip, q: Quit): "
                );
                String command = reader.readLine();
                
                switch (command) {
                case "1":
//                    callExperimentalRequestStream();
                    break;
                case "2":
//                    callExperimentalResponseStream();
                    break;
                case "3":
//                    callExperimentalRoundTrip();
                    break;
                case "q":
                    System.out.println("[clt] Exiting...");
                    return;
                default:
                    System.out.println("[clt] Invalid command. Please try again.");
                }
            }
            catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
}
