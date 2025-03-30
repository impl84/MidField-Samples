
package grpc.v1.client;

import com.midfield_system.grpc.v1.server.MfsGrpcServer;

import grpc.v1.client.experiment.GrpcExperimentExample;
import grpc.v1.client.performer.DeviceToRenderer;
import grpc.v1.client.performer.NetworkToRenderer;

public class MfsGrpcExampleExecutor
{
    private static final String HOST = "localhost";
    private static final int    PORT = MfsGrpcServer.DEFAULT_PORT;
    
    private static final String TARGET_NODE = "172.16.126.170";
    //private static final String TARGET_NODE = "192.168.3.15";
    
    public static void main(String[] args)
    {
        while (true) {
            Reporter.message("");
            Reporter.message("--------------------------------------------------------");
            Reporter.message("> 0: GrpcExperimentExample");
            Reporter.message("> 1: NodeControlExample");
            Reporter.message("> 2: DeviceToRenderer");
            Reporter.message("> 3: NetworkToRenderer");
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
        case "2" -> new DeviceToRenderer(HOST, PORT);
        case "3" -> new NetworkToRenderer(HOST, PORT, TARGET_NODE);
//          case "4" -> new DeviceToNetwork(HOST, PORT);
//          case "5" -> new NetworkToNetwork(HOST, PORT, TARGET_NODE);
//          case "6" -> new MixerToNetwork(HOST, PORT);
//          case "7" -> new MixerToRenderer(HOST, PORT);
        default -> null;
        };
        return example;
    }
}
