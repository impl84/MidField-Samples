
package grpc.v1.client;

import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

abstract public class MfsGrpcExampleBase
{
    private final ManagedChannel managedChannel;
    
    public MfsGrpcExampleBase(String host, int port)
    {
        var target = host + ":" + port;
        
        var channelCredentials = InsecureChannelCredentials.create();
        
        managedChannel = Grpc.newChannelBuilder(target, channelCredentials)
            .build();
    }
    
    abstract public void execute();
    
    void shutdown()
    {
        try {
            this.managedChannel
                .shutdownNow()
                .awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    protected ManagedChannel getManagedChannel()
    {
        return this.managedChannel;
    }
}
