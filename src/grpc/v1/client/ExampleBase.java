
package grpc.v1.client;

import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

abstract public class ExampleBase
{
    private final ManagedChannel managedChannel;
    
    public ExampleBase(String host, int port)
    {
        var creds = InsecureChannelCredentials.create();
        this.managedChannel = Grpc
            .newChannelBuilderForAddress(host, port, creds)
            .build();
        
        Runtime.getRuntime()
            .addShutdownHook(new ShutdownHookForGrpcClient(managedChannel));
        
        Reporter.message("gRPC client started for experiments on " + host + ":" + port);
        Reporter.message();
    }
    
    abstract public void execute();
    
    public void shutdown()
    {
        try {
            if (this.managedChannel == null) {
                Reporter.warning("gRPC client was not initialized.");
                return;
            }
            Reporter.message();
            Reporter.message("Shutting down ManagedChannel...");
            
            this.managedChannel.shutdown();
        }
        catch (Exception ex) {
            Reporter.error("Unexpected error in gRPC client.\n", ex);
        }
        finally {
            try {
                this.managedChannel.awaitTermination(8, TimeUnit.SECONDS);
            }
            catch (InterruptedException ex) {
                Reporter.warning("ManagedChannel shutdown interrupted.", ex);
            }
            finally {
                if (this.managedChannel.isTerminated()) {
                    Reporter.message("ManagedChannel terminated gracefully.");
                }
                else {
                    Reporter.warning(
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
            Reporter.message();
            Reporter.message("Shutdown hook triggered for gRPC client.");
            
            if (this.managedChannel.isShutdown()) {
                Reporter.message("(ManagedChannel is already shut down.)");
            }
            if (this.managedChannel.isTerminated()) {
                Reporter.message("(ManagedChannel is already terminated.)");
                return;
            }
            Reporter.warning(
                "ManagedChannel hasn't been shut down yet, so shutting down NOW..."
            );
            this.managedChannel.shutdownNow();
            
            this.managedChannel.awaitTermination(8, TimeUnit.SECONDS);
            Reporter.warning("ManagedChannel terminated.");
        }
        catch (InterruptedException ex) {
            Reporter.warning("ManagedChannel shutdown interrupted.\n", ex);
        }
        finally {
            if (this.managedChannel.isTerminated() == false) {
                Reporter.warning("ManagedChannel hasn't been terminated.");
            }
        }
    }
}
