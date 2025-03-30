
package grpc.v1.client;

import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

/**
 * Base class for gRPC client examples.
 * 
 * This class provides a managed channel for gRPC communication
 * and handles the shutdown process.
 * Subclasses must implement the `execute` method to define their specific behavior.
 */
abstract public class ExampleBase
{
    /**
     * ManagedChannel for gRPC communication.
     * This channel is created with insecure credentials for testing purposes.
     * In production, consider using secure credentials.
     */
    private final ManagedChannel managedChannel;
    
    /**
     * Shutdown hook for the gRPC client.
     * This hook ensures that the managed channel is properly shut down when the JVM
     * exits.
     */
    private final ShutdownHookForGrpcClient shutdownHook;
    
    /**
     * Constructor for the ExampleBase class.
     * Initializes the gRPC client with the specified host and port.
     * 
     * @param host
     *        the hostname or IP address of the gRPC server.
     * @param port
     *        the port number on which the gRPC server is listening.
     */
    public ExampleBase(String host, int port)
    {
        var creds = InsecureChannelCredentials.create();
        this.managedChannel = Grpc
            .newChannelBuilderForAddress(host, port, creds)
            .build();
        
        this.shutdownHook = new ShutdownHookForGrpcClient(this.managedChannel);
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        
        Reporter.message("gRPC client started for experiments on " + host + ":" + port);
        Reporter.message();
    }
    
    /**
     * Executes the specific gRPC client example.
     * This method must be implemented by subclasses to define their behavior.
     */
    abstract public void execute();
    
    /**
     * Shuts down the gRPC client and its managed channel.
     * This method gracefully shuts down the channel and waits for its termination.
     */
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
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
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
