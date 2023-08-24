
package rpc.performer;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.client.PerformerId;
import com.midfield_system.rpc.api.client.RemoteControlException;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class RemoteStreamingMixer
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RemoteOperator srcOp = null;
        RemoteOperator mixOp = null;
        RemoteOperator snkOp = null;
        
        try {
            // コマンドライン引数から必要なIPアドレスを取得する．
            var srcAddr   = args[0];
            var mixAddr   = args[1];
            var snkAddr   = args[2];
            var mixerName = args[3];
            
            // ▼Source Node，Mixer Node，Sink Node への遠隔操作を開始する．
            srcOp = new RemoteOperator(srcAddr, err -> System.err.println(err));
            mixOp = new RemoteOperator(mixAddr, err -> System.err.println(err));
            snkOp = new RemoteOperator(snkAddr, err -> System.err.println(err));
            
            // ▼ミキサーの処理を開始する．
            PerformerId mixerId = mixOp.setupMixerToNetwork(mixerName);
            
            // ▼ミキサーの出力をネットワーク経由で受信して再生表示を開始する．
            snkOp.setupNetworkToRenderer(mixerId);
            
            // ▼ミキサーへの入力となる映像の送信を開始する．
            PerformerId sourceId = srcOp.setupDeviceToNetwork();
            
            // ▼映像を受信して，ミキサーへの入力とする．
            mixOp.setupNetworkToMixer(sourceId, mixerId);
            
            // ▼終了を待つ．
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ▼全ての遠隔操作を終了する．
            if (srcOp != null) {
                srcOp.shutdownAll();
            }
            if (mixOp != null) {
                mixOp.shutdownAll();
            }
            if (snkOp != null) {
                snkOp.shutdownAll();
            }
        }
    }
}
