
package rpc;

import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.midfield_system.api.system.rpc.RpcClient;
import com.midfield_system.api.system.rpc.RpcResponse;
import com.midfield_system.api.util.Log;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: ClientMethodExample
 *
 * Date Modified: 2021.12.21
 *
 */

//==============================================================================
public class ClientMethodExample
{
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final RpcClient rpcClient;

//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//
	ClientMethodExample(RpcClient rpcClient)
	{
		this.rpcClient = rpcClient;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void invokeRpcMethods()
	{
		exampleSimpleRpc();
		exampleErrorResponse();
		exampleUsingFuture();
		exampleMethodWithStringParam();
		exampleMethodWithMapParam();
		exampleNotification();
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleSimpleRpc()
	{
		// ■単純なメソッド呼び出しの例：
		printTitle("単純なメソッド呼び出しの例：");
		
		printResult(invoke("Ex.getBooleanResult", null));
		printResult(invoke("Ex.getSuccessResult", null));
		printResult(invoke("Ex.getResponseWithResult", null));
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleErrorResponse()
	{
		// ■各種エラー発生時の例：
		printTitle("各種エラー発生時の例：");
		
		printResult(invoke("Ex.getParseError", null));
		printResult(invoke("Ex.getInvalidRequest", null));
		printResult(invoke("Ex.getMethodNotFound", null));
		printResult(invoke("Ex.getInvalidParams", null));
		printResult(invoke("Ex.getInternalError", null));
		printResult(invoke("Ex.getServerError", null));
		printResult(invoke(null, null));
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleUsingFuture()
	{
		// ■Future<RpcResponse> を利用したRPC要求と応答の例：
		printTitle("Future<RpcResponse> を利用したRPC要求と応答の例：");
		
		Future<RpcResponse> future0 = sendRequest("Ex.getBooleanResult", null);
		Future<RpcResponse> future1 = sendRequest("Ex.getSuccessResult", null);
		printResult(waitResponse(future0));
		printResult(waitResponse(future1));
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleMethodWithStringParam()
	{
		// ■文字列をパラメータとするRPC要求と応答の例：
		printTitle("文字列をパラメータとするRPC要求と応答の例：");
		
		// 文字列をパラメータとしてRPCを呼び出す．
		String echoString = (String)invoke(
			"Ex.echo",
			"hello"
		);
		// RPCの実行結果を表示する．
		printResult(echoString);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleMethodWithMapParam()
	{
		// ■複数のパラメータを伴うRPC要求と応答の例：
		printTitle("複数のパラメータを伴うRPC要求と応答の例：");
		
		// RPCの引数となるマップを生成する．
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		int productNo = 1;
		int quantity = 20;
		
		params.put("productNo", Integer.toString(productNo));
		params.put("quantity",  Integer.toString(quantity));
		
		// RPCを呼び出す．
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>)invoke(
			"Ex.orderProducts",
			params
		);
		// RPCの実行結果を表示する．
		if (result != null) {
			printResult(String.format("productNo: %s, quantity: %s, unitPrice: \\%s, totalPrice: \\%s",
				result.get("productNo"),
				result.get("quantity"),
				result.get("unitPrice"),
				result.get("totalPrice") 
			));
		}
		else {
			printResult("null");
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleNotification()
	{
		// ■RPC要求(Notification)の例：
		printTitle("Notification の例：");
		
		notify("Ex.notification", null);
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private Object invoke(String method, Object params)
	{
		Object result = null;
		try {
			Log.message("▼invoke: %s(%s)", method, params);
			
			// サーバのメソッドを呼び出し，RPC応答を取得する．
			RpcResponse response = this.rpcClient.invoke(
				method, params, 2000, TimeUnit.MILLISECONDS
			);	// InterruptedException, ExecutionException, TimeoutException
			
			// 結果が null の場合はエラー情報を出力する．
			result = response.getResult();
			if (result == null) {
				Map<String, Object> error = response.getError();
				Log.message("△ error: code: %s, message: %s, data: %s",
					error.get(ERR_CODE), error.get(ERR_MESSAGE), error.get(ERR_DATA)
				);
			}
		}
		catch (InterruptedException | ExecutionException | TimeoutException ex) {
			Log.warning("%s: %s", ex.getClass().getName(), ex.getMessage());
		}
		catch (Exception ex) {
			Log.error(ex);
		}
		return result;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private Future<RpcResponse> sendRequest(String method, Object params)
	{
		Log.message("▼request: %s(%s)", method, params);
		
		// RPC要求をサーバへ送る．
		Future<RpcResponse> future = this.rpcClient.request(method, params);
		return future;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private Object waitResponse(Future<RpcResponse> future)
	{
		Object result = null;
		try {
			// RPC応答を取得する．
			RpcResponse response = future.get(2000, TimeUnit.MILLISECONDS);
				// InterruptedException, ExecutionException, TimeoutException
			
			// 結果が null の場合はエラー情報を出力する．
			result = response.getResult();
			if (result == null) {
				Map<String, Object> error = response.getError();
				Log.message("△ error: code: %s, message: %s, data: %s",
					error.get(ERR_CODE), error.get(ERR_MESSAGE), error.get(ERR_DATA)
				);
			}
		}
		catch (InterruptedException | ExecutionException | TimeoutException ex) {
			Log.warning("%s: %s", ex.getClass().getName(), ex.getMessage());
		}
		catch (Exception ex) {
			Log.error(ex);
		}
		return result;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void notify(String method, Object params)
	{
		Log.message("◆notify: %s(%s)", method, params);
		
		// RPC要求(Notification)をサーバへ送る．
		this.rpcClient.notification(method, params);
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printTitle(String title)
	{
		Log.message(Log.LINE_SEPARATOR);
		Log.message("□%s", title);
		Log.message(Log.LINE_SEPARATOR);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printResult(Object result)
	{
		Log.message("△result: %s", result);
		Log.message();
	}
}
