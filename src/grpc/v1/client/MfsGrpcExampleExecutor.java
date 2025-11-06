
package grpc.v1.client;

import com.midfield_system.grpc.v1.server.MfsGrpcServer;

import grpc.v1.client.experiment.GrpcExperimentExample;
import grpc.v1.client.performer.DeviceToMixerInput;
import grpc.v1.client.performer.DeviceToNetwork;
import grpc.v1.client.performer.DeviceToRenderer;
import grpc.v1.client.performer.MixerControlExample;
import grpc.v1.client.performer.MixerToRenderer;
import grpc.v1.client.performer.NetworkToNetwork;
import grpc.v1.client.performer.NetworkToRenderer;
import io.grpc.StatusRuntimeException;

public class MfsGrpcExampleExecutor
{
    private static final String HOST = "localhost";
    
    private static final String[] MENU_LIST = {
        "",
        "--------------------------------------------------------",
        "> 0: GrpcExperimentExample",
        "> 1: NodeControlExample",
        "> 2: DeviceToRenderer",
        "> 3: DeviceToNetwork",
        "> 4: NetworkToRenderer",
        "> 5: NetworkToNetwork",
        "> 6: MixerToRenderer",
        "> 7: DeviceToMixerInput",
        "> --",
        "> 8: MixerControlExample",
        "> --",
        "> The other: Quit",
    };
    
    private static final int PORT = MfsGrpcServer.DEFAULT_PORT;
    
    public static void main(String[] args)
    {
        while (true) {
            for (var menu : MENU_LIST) {
                Reporter.message(menu);
            }
            var number  = Reporter.readLine("> Enter the number: ");
            var example = nextGrpcClientExample(number);
            if (example != null) {
                executeExample(example);
            }
            else {
                Reporter.message("Quit");
                break;
            }
        }
    }
    
    private static void executeExample(ExampleBase example)
    {
        try {
            example.execute();
        }
        catch (StatusRuntimeException ex) {
            Reporter.error("gRPCエラー発生", ex);
            ex.printStackTrace();
        }
        catch (Exception ex) {
            Reporter.error("実行時エラー発生", ex);
            ex.printStackTrace();
        }
        finally {
            example.shutdown();
        }
    }
    
    private static ExampleBase nextGrpcClientExample(String number)
    {
        var example = switch (number) {
        case "0" -> new GrpcExperimentExample(HOST, PORT);
        case "1" -> new NodeControlExample(HOST, PORT);
        case "2" -> new DeviceToRenderer(HOST, PORT);
        case "3" -> new DeviceToNetwork(HOST, PORT);
        case "4" -> new NetworkToRenderer(HOST, PORT);
        case "5" -> new NetworkToNetwork(HOST, PORT);
        case "6" -> new MixerToRenderer(HOST, PORT);
        case "7" -> new DeviceToMixerInput(HOST, PORT);
        case "8" -> new MixerControlExample(HOST, PORT);
        default  -> null;
        };
        return example;
    }
}
