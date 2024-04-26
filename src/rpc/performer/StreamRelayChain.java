
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
public class StreamRelayChain
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RemoteOperator remOp = null;
        
        try {
            // コマンドライン引数から必要なIPアドレスを取得する．
            var srcAddr = args[0];
            
            // ▼遠隔操作を開始する．
            remOp = new RemoteOperator(srcAddr, err -> System.err.println(err));
            
            // ▼中継機能をセットアップし，中継処理を開始する．
            // (1) ビデオカメラとマイクからネットワークへ
            // (2) 中継（プレビュー有り）
            // (3) 中継（プレビュー有り）
            // (4) 再生表示
            PerformerId pfmrId0 = remOp.setupDeviceToNetwork();
            PerformerId pfmrId1 = remOp.setupNetworkToNetwork(pfmrId0);
            PerformerId pfmrId2 = remOp.setupNetworkToNetwork(pfmrId1);
            PerformerId pfmrId3 = remOp.setupNetworkToRenderer(pfmrId2);
            
            // ▼それぞれの StreamPerofrmer を一旦停止した後に開始する．
            stopAndStart(remOp, pfmrId0);
            stopAndStart(remOp, pfmrId1);
            stopAndStart(remOp, pfmrId2);
            stopAndStart(remOp, pfmrId3);
            
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
            // ▼遠隔操作を終了する．
            if (remOp != null) {
                remOp.shutdownAll();
            }
        }
    }
    
    private static void stopAndStart(RemoteOperator remOp, PerformerId pfmrId)
        throws InterruptedException,
            RemoteControlException
    {
        // 指定された StreamPerformer の処理を 5秒後に一旦停止し，
        // さらに 5秒経過したら開始する．
        Thread.sleep(5000);
        remOp.stopPerformer(pfmrId);
        
        Thread.sleep(5000);
        remOp.startPerformer(pfmrId);
    }
}
