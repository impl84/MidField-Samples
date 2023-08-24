
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.InternalErrorListener;
import com.midfield_system.rpc.api.RpcClient;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class DemoRpcClient
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SERVER_NAME = "127.0.0.1";
    private static final int    SERVER_PORT = 60202;
    
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
        
        RpcClient rpcClient = null;
        try {
            Log.message("> DemoRpcClient: RpcClient の処理を開始します．");
            
            // InternalErrorListener のインスタンス
            InternalErrorListener listener = error -> Log.error(error.toString());
            
            // RpcClient を生成する．
            // (UnknownHostException, IOException)
            rpcClient = new RpcClient(
                SERVER_NAME,    // RPCサーバ名またはIPアドレス
                SERVER_PORT,    // RPCサーバのポート番号
                false,          // JSONオブジェクト(文字列)を整形するか否か
                true,           // JSONオブジェクト(文字列)をログ出力するか否か
                listener        // InternalErrorListener
            );
            // RpcClient の処理を開始する．
            rpcClient.open();
            
            // クライアント側のメソッドの例を実行する．
            DemoClientMethod clientMethod = new DemoClientMethod(rpcClient);
            clientMethod.invokeAll();
        }
        catch (Exception ex) {
            // RpcClient の動作中に例外が発生した．
            Log.message("> DemoRpcClient: RpcClient の実行中に例外が発生しました．");
            ex.printStackTrace();
        }
        finally {
            // RpcClient の処理を終了する．
            if (rpcClient != null) {
                Log.message("> DemoRpcClient: RpcClient の処理を終了します．");
                rpcClient.close();
            }
        }
    }
}
