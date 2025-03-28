
package grpc.v1.performer;

import com.midfield_system.grpc.v1.server.MfsGrpcServer;
import com.midfield_system.midfield.MfsApplication;

public class ExampleExecutor
{
    private static final int    PORT_NUMBER    = MfsGrpcServer.DEFAULT_PORT_NUMBER;
    private static final String SERVER_ADDRESS = "localhost";
    
    @SuppressWarnings("unused")
//    private static final String SOURCE_ADDRESS = "172.16.126.178";
    
    public static void main(String[] args)
    {
        int exampleNumber = 0;
        if (args.length > 0) {
            exampleNumber = Integer.parseInt(args[0]);
        }
        
        MfsGrpcExampleBase example = null;
        try {
            MfsApplication.launch();
            MfsGrpcServer.launch(args, null);
            
            example = switch (exampleNumber) {
            case 0 -> new GrpcExperimentTester(SERVER_ADDRESS, PORT_NUMBER);
//            case 1  -> new DeviceToNetwork(SERVER_ADDRESS, PORT_NUMBER);
//            case 2  -> new DeviceToNetwork(SERVER_ADDRESS, PORT_NUMBER);
//            case 3  -> new DeviceToRenderer(SERVER_ADDRESS, PORT_NUMBER);
//            case 4  -> new MixerToNetwork(SERVER_ADDRESS, PORT_NUMBER);
//            case 5  -> new MixerToRenderer(SERVER_ADDRESS, PORT_NUMBER);
//            case 6  -> new NetworkToNetwork(SERVER_ADDRESS, PORT_NUMBER, SOURCE_ADDRESS);
//            case 7  -> new NetworkToRenderer(SERVER_ADDRESS, PORT_NUMBER, SOURCE_ADDRESS);
            default -> throw new IllegalArgumentException(
                "Unexpected example number: " + exampleNumber
            );
            };
            example.execute();
            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (example != null) {
                example.shutdown();
            }
        }
    }
}
