
package rpc;

import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.util.Map;

import com.midfield_system.api.system.rpc.ErrorResponseHandler;
import com.midfield_system.api.system.rpc.RpcClient;
import com.midfield_system.api.util.Log;

import util.ConsolePrinter;
import util.ConsoleReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: SimpleRpcClient
 *
 * Date Modified: 2021.09.06
 *
 */

//==============================================================================
public class SimpleRpcClient
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String SERVER_NAME = "127.0.0.1";
	private static final int PORT_NUMBER = 60202;
	
	// ErrorResponseHandler の例
	private static final ErrorResponseHandler HANDLER = (reason, response, ex) -> {
		// RPC応答処理時に発生したエラー情報を出力する．
		Log.warning(reason);
		if (response != null) {
			Map<String, Object> error = response.getError();
			Log.warning("error: code: %s, message: %s, data: %s",
				error.get(ERR_CODE), error.get(ERR_MESSAGE), error.get(ERR_DATA)
			);
		}
		if (ex != null) {
			Log.warning(ex);
		}
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
		
		RpcClient rpcClient = null;
		try {
			printer.println("> SimpleRpcClient: RpcClient の処理を開始します．");
			printer.println("> SimpleRpcClient: 終了する際は Enter キーを押してください．");
			printer.println("");
			
			// RpcClient を生成する．
			rpcClient = new RpcClient(
				SERVER_NAME,	// RPCサーバ名またはIPアドレス
				PORT_NUMBER,	// RPCサーバのポート番号
				false,			// JSONオブジェクト(文字列)を整形するか否か
				true,			// JSONオブジェクト(文字列)をログ出力するか否か
				HANDLER			// ErrorResponseHandler のインスタンス
			);	// UnknownHostException
			
			// RpcClient の処理を開始する．
			rpcClient.open();
				// IOException
			
			// クライアント側のメソッドの例を実行する．
			ClientMethodExample example = new ClientMethodExample(rpcClient);
			example.invokeRpcMethods();
			
			// コンソールからの入力を待つ．
			reader.readLine();
		}
		catch (Exception ex) {
			// RpcClient の動作中に例外が発生した．
			printer.println("> SimpleRpcClient: RpcClient の実行中に例外が発生しました．");
			ex.printStackTrace();
		}
		finally {
			// RpcClient の処理を終了する．
			if (rpcClient != null) {
				rpcClient.close();
			}
			// ConsoleReader を解放する．
			reader.release();
			
			printer.println("");
			printer.println("> SimpleRpcClient: RpcClient の処理を終了します．");
		}
	}
}
