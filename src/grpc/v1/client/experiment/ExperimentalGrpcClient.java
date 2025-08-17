
package grpc.v1.client.experiment;

import java.util.concurrent.TimeUnit;

import com.midfield_system.api.log.ConsolePrinter;
import com.midfield_system.api.log.Log;

import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

/**
 * Experimental gRPC client for testing purposes.
 * ■■■■■■■■必要がなくなるクラス．■■■■■■■■
 */
public class ExperimentalGrpcClient
{
    private static final String HOST = "localhost";
    private static final int    PORT = 55555;
    
    public static void main(String[] args)
    {
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        ManagedChannel managedChannel = null;;
        try {
            ChannelCredentials creds = InsecureChannelCredentials.create();
            managedChannel = Grpc
                .newChannelBuilderForAddress(HOST, PORT, creds)
                .build();
            
            Runtime.getRuntime()
                .addShutdownHook(new ShutdownHookForGrpcClient(managedChannel));
            
            Log.message("gRPC client started for experiments on " + HOST + ":" + PORT);
            Log.message();
            
            GrpcClientExperimenter[] experimenters = {
                new CltRoundTrip(managedChannel),
                new CltResponseStream(managedChannel),
                new CltRequestStream(managedChannel),
                new CltBidirectionalStream(managedChannel)
            };
            for (GrpcClientExperimenter experimenter : experimenters) {
                experimenter.doExperiments();
            }
            Log.message();
            Log.message("Shutting down ManagedChannel...");
            managedChannel.shutdown();
        }
        catch (Exception ex) {
            Log.error("Unexpected error in gRPC client.", ex);
        }
        finally {
            if (managedChannel == null) {
                Log.error("gRPC client was not initialized.");
                return;
            }
            try {
                managedChannel.awaitTermination(8, TimeUnit.SECONDS);
            }
            catch (InterruptedException ex) {
                Log.warning("ManagedChannel shutdown interrupted.", ex);
            }
            finally {
                if (managedChannel.isTerminated()) {
                    Log.message("ManagedChannel terminated gracefully.");
                }
                else {
                    Log.warning("ManagedChannel hasn't been shut down yet, so shutting down NOW...");
                    managedChannel.shutdownNow();
                }
            }
        }
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
            Log.message();
            Log.message("Shutdown hook triggered for gRPC client.");
            
            if (this.managedChannel.isShutdown()) {
                Log.message("(ManagedChannel is already shut down.)");
            }
            if (this.managedChannel.isTerminated()) {
                Log.message("(ManagedChannel is already terminated.)");
                return;
            }
            Log.warning(
                "ManagedChannel hasn't been shut down yet, so shutting down NOW..."
            );
            this.managedChannel.shutdownNow();
            
            this.managedChannel.awaitTermination(8, TimeUnit.SECONDS);
            Log.warning("ManagedChannel terminated.");
        }
        catch (InterruptedException ex) {
            Log.warning("ManagedChannel shutdown interrupted.", ex);
        }
        finally {
            if (this.managedChannel.isTerminated() == false) {
                Log.warning("ManagedChannel hasn't been terminated.");
            }
        }
    }
}
