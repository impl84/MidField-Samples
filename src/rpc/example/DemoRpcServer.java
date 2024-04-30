
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.json_rpc.api.Registerable;
import com.midfield_system.json_rpc.api.RegisterableArrayFactory;
import com.midfield_system.json_rpc.api.RpcServer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class DemoRpcServer
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SERVER_NAME   = null;       // ワイルドカードアドレス
    private static final int    PORT_NUMBER   = 60202;
    private static final int    MAX_IDLE_TIME = 600 * 1000; // [ms]
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RpcServer rpcServer = null;
        try {
            Log.message("> DemoRpcServer: RpcServer の処理を開始します．");
            
            // RegisterableArrayFactory のインスタンス
            RegisterableArrayFactory factory = () -> new Registerable[] {new DemoServerMethod()};
            
            // RpcServer を生成する．
            // (UnknownHostException)
            rpcServer = new RpcServer(
                SERVER_NAME,    // RPCサーバ名またはIPアドレス
                PORT_NUMBER,    // RPCサーバのポート番号
                MAX_IDLE_TIME,  // コネクション毎に許容する最大のアイドル時間
                false,          // JSONオブジェクト(文字列)を整形するか否か
                true,           // JSONオブジェクト(文字列)をログ出力するか否か
                factory         // RegisterableArrayFactory
            );
            // RpcServer の処理を開始する．
            rpcServer.open();
            
            // コンソールからの入力を待つ．
            Log.message("> DemoRpcServer: 終了する際は Enter キーを押してください．");
            System.in.read();
        }
        catch (Exception ex) {
            // RpcServer の動作中に例外が発生した．
            Log.message("> DemoRpcServer: RpcServer の実行中に例外が発生しました．");
            ex.printStackTrace();
        }
        finally {
            // RpcServer の処理を終了する．
            if (rpcServer != null) {
                Log.message("> DemoRpcServer: RpcServer の処理を終了します．");
                rpcServer.close();
            }
        }
    }
}
