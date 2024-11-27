
package grpc.performer;

import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

abstract class MfsGrpcExample
{
    private final ManagedChannel channel;
    
    MfsGrpcExample(String serverAddress, int portNumber)
    {
        var target             = serverAddress + ":" + portNumber;
        var channelCredentials = InsecureChannelCredentials.create();
        channel = Grpc.newChannelBuilder(target, channelCredentials)
            .build();
    }
    
    abstract void execute();
    
    void shutdown()
    {
        try {
            this.channel
                .shutdownNow()
                .awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    protected ManagedChannel getManagedChannel()
    {
        return this.channel;
    }
}
