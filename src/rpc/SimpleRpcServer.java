
package rpc;

import com.midfield_system.api.system.rpc.Registerable;
import com.midfield_system.api.system.rpc.RegisterableArrayFactory;
import com.midfield_system.api.system.rpc.RpcServer;
import com.midfield_system.api.util.Log;

import util.ConsolePrinter;
import util.ConsoleReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: SimpleRpcServer
 *
 * Date Modified: 2021.09.06
 *
 */

//==============================================================================
public class SimpleRpcServer
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String SERVER_NAME = null; // ワイルドカードアドレス
	private static final int PORT_NUMBER = 60202;
	private static final int MAX_IDLE_TIME = 600 * 1000;	// [ms]
	
	// RegisterableArrayFactory の例
	private static final RegisterableArrayFactory FACTORY = () -> {
		// RPC要求処理メソッドを含む Registerable の配列を生成して返す．
		Registerable example = new ServerMethodExample();
		Registerable[] registerableArray = {
			example
		};
		return registerableArray;
	};
	
//==============================================================================
//  CLASS METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void main(String[] args)
	{
		ConsoleReader reader = ConsoleReader.getInstance();
		ConsolePrinter printer = ConsolePrinter.getInstance();
		Log.setLogPrinter(printer);
		
		RpcServer rpcServer = null;
		try {
			printer.println("> SimpleRpcServer: RpcServer の処理を開始します．");
			printer.println("> SimpleRpcServer: 終了する際は Enter キーを押してください．");
			printer.println("");
			
			// RpcServer を生成する．
			rpcServer = new RpcServer(
				SERVER_NAME,	// RPCサーバ名またはIPアドレス
				PORT_NUMBER,	// RPCサーバのポート番号
				MAX_IDLE_TIME,	// コネクション毎に許容する最大のアイドル時間
				false,			// JSONオブジェクト(文字列)を整形するか否か
				true,			// JSONオブジェクト(文字列)をログ出力するか否か
				FACTORY			// RegisterableArrayFactory のインスタンス
			);	// UnknownHostException
			
			// RpcServer の処理を開始する．
			rpcServer.open();
			
			// コンソールからの入力を待つ．
			reader.readLine();
		}
		catch (Exception ex) {
			// RpcServer の動作中に例外が発生した．
			printer.println("> SimpleRpcServer: RpcServer の実行中に例外が発生しました．");
			ex.printStackTrace();
		}
		finally {
			// RpcServer の処理を終了する．
			if (rpcServer != null) {
				rpcServer.close();
			}
			// ConsoleReader を解放する．
			reader.release();
			
			printer.println("");
			printer.println("> SimpleRpcServer: RpcServer の処理を終了します．");
		}
	}
}
