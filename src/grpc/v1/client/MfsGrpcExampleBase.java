
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
        var creds = InsecureChannelCredentials.create();
        this.managedChannel = Grpc
            .newChannelBuilderForAddress(host, port, creds)
            .build();
        
        Runtime.getRuntime()
            .addShutdownHook(new ShutdownHookForGrpcClient(managedChannel));
        
        System.out.println("gRPC client started for experiments on " + host + ":" + port);
        System.out.println();
    }
    
    abstract public void execute();
    
    public void shutdown()
    {
        try {
            if (this.managedChannel == null) {
                System.err.println("gRPC client was not initialized.");
                return;
            }
            System.out.println();
            System.out.println("Shutting down ManagedChannel...");
            
            this.managedChannel.shutdown();
        }
        catch (Exception ex) {
            System.err.printf("Unexpected error in gRPC client.\n", ex);
        }
        finally {
            try {
                this.managedChannel.awaitTermination(8, TimeUnit.SECONDS);
            }
            catch (InterruptedException ex) {
                System.err.printf("ManagedChannel shutdown interrupted.", ex);
            }
            finally {
                if (this.managedChannel.isTerminated()) {
                    System.out.println("ManagedChannel terminated gracefully.");
                }
                else {
                    System.err.println(
                        "ManagedChannel hasn't been shut down yet, so shutting down NOW..."
                    );
                    this.managedChannel.shutdownNow();
                }
            }
        }
        
    }
    
    protected ManagedChannel getManagedChannel()
    {
        return this.managedChannel;
    }
}

/**
 * Shutdown hook for the gRPC client.
 */
class ShutdownHookForGrpcClient
    extends
        Thread
{
    private final ManagedChannel managedChannel;
    
    ShutdownHookForGrpcClient(ManagedChannel managedChannel)
    {
        super("ShutdownHookForGrpcClient");
        this.managedChannel = managedChannel;
    }
    
    @Override
    public void run()
    {
        try {
            System.out.println();
            System.out.println("Shutdown hook triggered for gRPC client.");
            
            if (this.managedChannel.isShutdown()) {
                System.out.println("(ManagedChannel is already shut down.)");
            }
            if (this.managedChannel.isTerminated()) {
                System.out.println("(ManagedChannel is already terminated.)");
                return;
            }
            System.err.println(
                "ManagedChannel hasn't been shut down yet, so shutting down NOW..."
            );
            this.managedChannel.shutdownNow();
            
            this.managedChannel.awaitTermination(8, TimeUnit.SECONDS);
            System.err.println("ManagedChannel terminated.");
        }
        catch (InterruptedException ex) {
            System.err.printf("ManagedChannel shutdown interrupted.\n", ex);
        }
        finally {
            if (this.managedChannel.isTerminated() == false) {
                System.err.println("ManagedChannel hasn't been terminated.");
            }
        }
    }
}
