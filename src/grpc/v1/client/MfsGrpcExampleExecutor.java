
package grpc.v1.client;

import com.midfield_system.grpc.v1.server.MfsGrpcServer;

import grpc.v1.client.experiment.GrpcExperimentExample;

public class MfsGrpcExampleExecutor
{
    private static final String HOST = "localhost";
    private static final int    PORT = MfsGrpcServer.DEFAULT_PORT;
    
//    @SuppressWarnings("unused")
//    private static final String SOURCE_ADDRESS = "172.16.126.178";
    
    public static void main(String[] args)
    {
        //MfsApplication.launch();
        //MfsGrpcServer.launch(args, null);
        
        while (true) {
            Reporter.message("");
            Reporter.message("--------------------------------------------------------");
            Reporter.message("> 0: GrpcExperimentExample");
            Reporter.message("> 1: NodeControlExample");
            Reporter.message("> --");
            Reporter.message("> The other: Quit");
            var number = Reporter.readLine("> Enter the number: ");
            
            var example = nextGrpcClientExample(number);
            if (example != null) {
                example.execute();
                example.shutdown();
            }
            else {
                Reporter.message("Quit");
                break;
            }
        }
    }
    
    private static ExampleBase nextGrpcClientExample(String number)
    {
        var example = switch (number) {
        case "0" -> new GrpcExperimentExample(HOST, PORT);
        case "1" -> new NodeControlExample(HOST, PORT);
//          case 1  -> new DeviceToNetwork(SERVER_ADDRESS, PORT_NUMBER);
//          case 2  -> new DeviceToNetwork(SERVER_ADDRESS, PORT_NUMBER);
//          case 3  -> new DeviceToRenderer(SERVER_ADDRESS, PORT_NUMBER);
//          case 4  -> new MixerToNetwork(SERVER_ADDRESS, PORT_NUMBER);
//          case 5  -> new MixerToRenderer(SERVER_ADDRESS, PORT_NUMBER);
//          case 6  -> new NetworkToNetwork(SERVER_ADDRESS, PORT_NUMBER, SOURCE_ADDRESS);
//          case 7  -> new NetworkToRenderer(SERVER_ADDRESS, PORT_NUMBER, SOURCE_ADDRESS);
        default -> null;
        };
        return example;
    }
}
