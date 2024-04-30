
package rpc.performer;

import com.midfield_system.api.util.Log;
import com.midfield_system.json_rpc.client.PerformerId;
import com.midfield_system.json_rpc.client.RemoteControlException;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class SourceNodeToSinkNode
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RemoteOperator srcOp = null;
        RemoteOperator snkOp = null;
        
        try {
            // コマンドライン引数から必要なIPアドレスを取得する．
            var srcAddr = args[0];
            var snkAddr = args[1];
            
            // ▼Source Node と Sink Node への遠隔操作を開始する．
            srcOp = new RemoteOperator(srcAddr, err -> System.err.println(err));
            snkOp = new RemoteOperator(snkAddr, err -> System.err.println(err));
            
            // ▼中継機能をセットアップし，中継処理を開始する．
            // (1) Source Node のビデオカメラとマイクからネットワークへ
            // (2) Source Node 内での中継（プレビュー有り）
            // (3) Source Node から Sink Node への中継（プレビュー有り）
            // (4) Sink Node で再生表示
            PerformerId pfmrId0 = srcOp.setupDeviceToNetwork();
            PerformerId pfmrId1 = srcOp.setupNetworkToNetwork(pfmrId0);
            PerformerId pfmrId2 = snkOp.setupNetworkToNetwork(pfmrId1);
            PerformerId pfmrId3 = snkOp.setupNetworkToRenderer(pfmrId2);
            
            // ▼それぞれの StreamPerofrmer を一旦停止した後に開始する．
            stopAndStart(srcOp, pfmrId0);
            stopAndStart(srcOp, pfmrId1);
            stopAndStart(snkOp, pfmrId2);
            stopAndStart(snkOp, pfmrId3);
            
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
            // ▼Source Node と Sink Node への遠隔操作を終了する．
            if (snkOp != null) {
                snkOp.shutdownAll();
            }
            if (srcOp != null) {
                srcOp.shutdownAll();
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
