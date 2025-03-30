
package grpc.v1.client.experiment;

import io.grpc.Context.CancellableContext;

/**
 * A utility class that manages the cancellation of a gRPC context after a
 * specified duration.
 * <p>
 * This class is designed to be used in scenarios where a gRPC call needs to be
 * cancelled after a certain period, allowing for graceful handling of timeouts
 * and cancellations.
 */
public class ContextCanceller
{
    /**
     * The context to be cancelled.
     */
    private final CancellableContext context;
    
    /**
     * A description of the cancellation reason.
     */
    private final String description;
    
    /**
     * The thread that will perform the cancellation after the specified duration.
     */
    private final Thread thread;
    
    /**
     * Constructs a ContextCanceller with the specified context, description, and
     * cancellation duration.
     *
     * @param context
     *        The CancellableContext to be cancelled.
     * @param description
     *        A description of the cancellation reason.
     * @param milliseconds
     *        The duration in milliseconds after which the context will be
     *        cancelled.
     */
    public ContextCanceller(CancellableContext context, String description, long milliseconds)
    {
        this.context     = context;
        this.description = description;
        
        this.thread = new Thread(() -> cancel(milliseconds));
    }
    
    /**
     * Waits for the cancellation thread to finish.
     * This method blocks until the cancellation process is complete.
     */
    public void waitCancelTime()
    {
        try {
            this.thread.join();
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }
    
    /**
     * Starts the cancellation thread.
     * This method should be called to initiate the cancellation process.
     */
    public void start()
    {
        this.thread.start();
    }
    
    /**
     * Cancels the context after the specified duration.
     * This method runs in a separate thread to avoid blocking the main thread.
     *
     * @param milliseconds
     *        The duration in milliseconds after which the context will be
     *        cancelled.
     */
    private void cancel(long milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
            RuntimeException ex = new RuntimeException(this.description);
            this.context.cancel(ex);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }
}
