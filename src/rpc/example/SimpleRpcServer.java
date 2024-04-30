
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
public class SimpleRpcServer
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RpcServer rpcServer = null;
        try {
            Log.message("> SimpleRpcServer: RpcServer の処理を開始します．");
            
            // RpcServer を生成する．
            // (UnknownHostException)
            rpcServer = new RpcServer(
                null,       // RPCサーバ名またはIPアドレス(ワイルドカードアドレス)
                60202,      // RPCサーバのポート番号
                600 * 1000, // コネクション毎に許容する最大のアイドル時間(msec)
                false,      // JSONオブジェクト(文字列)を整形するか否か
                true,       // JSONオブジェクト(文字列)をログ出力するか否か
                factory     // ServerMethodFactory の実装
            );
            // RpcServer の処理を開始する．
            rpcServer.open();
            
            // コンソールからの入力を待つ．
            Log.message("> SimpleRpcServer: 終了する際は Enter キーを押してください．");
            System.in.read();
        }
        catch (Exception ex) {
            // RpcServer の動作中に例外が発生した．
            Log.message("> SimpleRpcServer: RpcServer の実行中に例外が発生しました．");
            ex.printStackTrace();
        }
        finally {
            // RpcServer の処理を終了する．
            if (rpcServer != null) {
                Log.message("> SimpleRpcServer: RpcServer の処理を終了します．");
                rpcServer.close();
            }
        }
    }
    
    // RegisterableArrayFactory：ServerMethodFactory クラスとしての実装
    static RegisterableArrayFactory factory = new ServerMethodFactory();
    
    static class ServerMethodFactory
        implements
            RegisterableArrayFactory
    {
        @Override
        public Registerable[] createRegisterableArray()
        {
            Registerable[] registerableArray = new Registerable[] {
                new SimpleServerMethod()
            };
            return registerableArray;
        }
    }
    
    // ▼参考：RegisterableArrayFactory の実装（無名クラス）
    static RegisterableArrayFactory factory_ex0 = new RegisterableArrayFactory()
    {
        @Override
        public Registerable[] createRegisterableArray()
        {
            return new Registerable[] {new SimpleServerMethod()};
        }
    };
    
    // ▼参考：RegisterableArrayFactory の実装（ラムダ式）
    static RegisterableArrayFactory factory_ex1 = () -> { return new Registerable[] {new SimpleServerMethod()}; };
    
    // ▼参考：RegisterableArrayFactory の実装（可能な限り省略したラムダ式）
    static RegisterableArrayFactory factory_ex2 = () -> new Registerable[] {new SimpleServerMethod()};
    
}
