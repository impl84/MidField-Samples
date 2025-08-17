
package grpc.v1.server;

import com.midfield_system.grpc.v1.server.MfsGrpcServer;
import com.midfield_system.midfield.MfsApplication;

public class MfsGrpcServerLauncher
{
    public static void main(String[] args)
    {
        try {
            MfsApplication.launch();
            MfsGrpcServer.launch(args, null);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
