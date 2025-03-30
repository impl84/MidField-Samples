
package grpc.v1.client;

import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

abstract public class ExampleBase
{
    /** メッセージ定数 */
    private static final String MSG_SHUTDOWN             = "・ManagedChannel を終了します．";
    private static final String MSG_SHUTDOWN_FAIL        = "・ManagedChannel を終了出来ませんでした．";
    private static final String MSG_SHUTDOWN_FORCE       = "・ManagedChannel を強制的に終了します．";
    private static final String MSG_SHUTDOWN_FORCE_OK    = "・ManagedChannel を強制的に終了しました．";
    private static final String MSG_SHUTDOWN_INTERRUPTED = "・ManagedChannel の終了処理が割り込みにより中断されました．";
    private static final String MSG_SHUTDOWN_OK          = "・ManagedChannel は正常に終了しました．";
    private static final String MSG_STARTED              = "▼%s 開始 (%s:%d)\n";
    private static final String MSG_STOPPED              = "▲%s 終了\n";
    
    /** 強制シャットダウン時のタイムアウト（秒） */
    private static final int SHUTDOWN_FORCE_TIMEOUT_SEC = 5;
    
    /** 通常シャットダウン時のタイムアウト（秒） */
    private static final int SHUTDOWN_TIMEOUT_SEC = 30;
    
    private final ManagedChannel managedChannel;
    
    public ExampleBase(String host, int port)
    {
        var creds = InsecureChannelCredentials.create();
        this.managedChannel = Grpc
            .newChannelBuilderForAddress(host, port, creds)
            .build();
        
        Reporter.message(MSG_STARTED, this.getClass().getSimpleName(), host, port);
        Reporter.message();
    }
    
    abstract public void execute();
    
    protected ManagedChannel getManagedChannel()
    {
        return this.managedChannel;
    }
    
    protected void shutdown()
    {
        if (this.managedChannel.isTerminated()) {
            return;
        }
        Reporter.message(MSG_SHUTDOWN);
        this.managedChannel.shutdown();
        
        try {
            var isTerminated = this.managedChannel
                .awaitTermination(SHUTDOWN_TIMEOUT_SEC, TimeUnit.SECONDS);
            if (isTerminated) {
                Reporter.message(MSG_SHUTDOWN_OK);
                return;
            }
            Reporter.message(MSG_SHUTDOWN_FORCE);
            this.managedChannel.shutdownNow();
            isTerminated = this.managedChannel
                .awaitTermination(SHUTDOWN_FORCE_TIMEOUT_SEC, TimeUnit.SECONDS);
            if (isTerminated) {
                Reporter.message(MSG_SHUTDOWN_FORCE_OK);
                return;
            }
            Reporter.warning(MSG_SHUTDOWN_FAIL);
        }
        catch (InterruptedException ex) {
            Reporter.warning(MSG_SHUTDOWN_INTERRUPTED, ex);
            Reporter.warning(MSG_SHUTDOWN_FORCE);
            this.managedChannel.shutdownNow();
            
            Thread.currentThread().interrupt();
        }
        finally {
            Reporter.message();
            Reporter.message(MSG_STOPPED, this.getClass().getSimpleName());
        }
    }
}
