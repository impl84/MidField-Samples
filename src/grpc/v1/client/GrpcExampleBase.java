
package grpc.v1.client;

import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

abstract public class GrpcExampleBase
{
    private final ManagedChannel managedChannel;
    
    public GrpcExampleBase(String host, int port)
    {
        var creds = InsecureChannelCredentials.create();
        this.managedChannel = Grpc
            .newChannelBuilderForAddress(host, port, creds)
            .build();
        
        Runtime.getRuntime()
            .addShutdownHook(new ShutdownHookForGrpcClient(managedChannel));
        
        Reporter.println("gRPC client started for experiments on " + host + ":" + port);
        Reporter.println();
    }
    
    abstract public void execute();
    
    public void shutdown()
    {
        try {
            if (this.managedChannel == null) {
                Reporter.error("gRPC client was not initialized.");
                return;
            }
            Reporter.println();
            Reporter.println("Shutting down ManagedChannel...");
            
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
                    Reporter.println("ManagedChannel terminated gracefully.");
                }
                else {
                    Reporter.error(
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
            Reporter.println();
            Reporter.println("Shutdown hook triggered for gRPC client.");
            
            if (this.managedChannel.isShutdown()) {
                Reporter.println("(ManagedChannel is already shut down.)");
            }
            if (this.managedChannel.isTerminated()) {
                Reporter.println("(ManagedChannel is already terminated.)");
                return;
            }
            Reporter.error(
                "ManagedChannel hasn't been shut down yet, so shutting down NOW..."
            );
            this.managedChannel.shutdownNow();
            
            this.managedChannel.awaitTermination(8, TimeUnit.SECONDS);
            Reporter.error("ManagedChannel terminated.");
        }
        catch (InterruptedException ex) {
            Reporter.error("ManagedChannel shutdown interrupted.\n", ex);
        }
        finally {
            if (this.managedChannel.isTerminated() == false) {
                Reporter.error("ManagedChannel hasn't been terminated.");
            }
        }
    }
}
