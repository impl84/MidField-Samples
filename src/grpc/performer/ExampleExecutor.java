
package grpc.performer;

import com.midfield_system.api.log.BlockingQueueLogHandler;
import com.midfield_system.api.log.MfsLogger;
import com.midfield_system.grpc.v1.server.MfsGrpcServer;
import com.midfield_system.midfield.MfsApplication;

public class ExampleExecutor
{
    private static final int    PORT_NUMBER    = MfsGrpcServer.DEFAULT_PORT_NUMBER;
    private static final String SERVER_ADDRESS = "localhost";
    
    @SuppressWarnings("unused")
    private static final String SOURCE_ADDRESS = "172.16.126.178";
    
    public static void main(String[] args)
    {
        int exampleNumber = 2;
        if (args.length > 0) {
            exampleNumber = Integer.parseInt(args[0]);
        }
        
        var handler = new BlockingQueueLogHandler();
        var logger  = MfsLogger.getLogger();
        logger.addHandler(handler);
        Runnable task = () -> {
            while (true) {
                String message = handler.getMessage();
                if (message != null) {
                    System.out.print(message);
                }
                else {
                    break;
                }
            }
        };
        new Thread(task).start();
        
        MfsGrpcExample example = null;
        try {
            MfsApplication.launch();
            MfsGrpcServer.launch(args, null);
            
            example = switch (exampleNumber) {
            case 0  -> new DeviceToNetwork(SERVER_ADDRESS, PORT_NUMBER);
            case 1  -> new DeviceToNetwork(SERVER_ADDRESS, PORT_NUMBER);
            case 2  -> new DeviceToRenderer(SERVER_ADDRESS, PORT_NUMBER);
            case 3  -> new MixerToNetwork(SERVER_ADDRESS, PORT_NUMBER);
            case 4  -> new MixerToRenderer(SERVER_ADDRESS, PORT_NUMBER);
            case 5  -> new NetworkToNetwork(SERVER_ADDRESS, PORT_NUMBER, SOURCE_ADDRESS);
            case 6  -> new NetworkToRenderer(SERVER_ADDRESS, PORT_NUMBER, SOURCE_ADDRESS);
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
            logger.removeHandler(handler);
            handler.close();
        }
    }
}
