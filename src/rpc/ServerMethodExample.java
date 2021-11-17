
package rpc;

import java.util.LinkedHashMap;
import java.util.Map;

import com.midfield_system.api.system.rpc.Registerable;
import com.midfield_system.api.system.rpc.RpcRequest;
import com.midfield_system.api.system.rpc.RpcResponse;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: ServerMethodExample
 *
 * Date Modified: 2021.11.17
 *
 */

//==============================================================================
public class ServerMethodExample
	implements	Registerable
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String METHOD_PREFIX = "Ex";
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD: RPC要求処理の例
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getBooleanResult(RpcRequest request)
	{
		boolean result = true;
		
		// Boolean 値を結果とするRPC応答を生成する．
		RpcResponse response = RpcResponse.booleanResponse(
			request, result
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getSuccessResult(RpcRequest request)
	{
		boolean isSuccess = true;
		
		// "success" (または "failure") を結果とするRPC応答を生成する．
		RpcResponse response = RpcResponse.successResponse(
			request, isSuccess
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getResponseWithResult(RpcRequest request)
	{
		Object result = new String("Any resulting object.");
		
		// 何らかのオブジェクトを結果とするRPC応答を生成する．
		RpcResponse response = RpcResponse.responseWithResult(
			request, result
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getParseError(RpcRequest request)
	{
		String data = new String(
			"Invalid JSON was received by the server."
			+ "An error occurred on the server while parsing the JSON text."
		);
		// 解析エラー発生時のRPC応答を生成する．
		RpcResponse response = RpcResponse.parseError(
			data
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getInvalidRequest(RpcRequest request)
	{
		// 無効なリクエストを受信した場合のRPC応答を生成する．
		RpcResponse response = RpcResponse.invalidRequest(
			request
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getMethodNotFound(RpcRequest request)
	{
		// メソッドが存在しない/利用できない場合のRPC応答を生成する．
		RpcResponse response = RpcResponse.methodNotFound(
			request
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getInvalidParams(RpcRequest request)
	{
		String data = new String("Invalid method parameter(s).");
		
		// メソッドのパラメータが無効な場合のRPC応答を生成する．
		RpcResponse response = RpcResponse.invalidParams(
			request, data
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getInternalError(RpcRequest request)
	{
		String data = new String("Internal JSON-RPC error.");
		
		// 内部エラーが発生した場合のRPC応答を生成する．
		RpcResponse response = RpcResponse.internalError(
			request, data
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getServerError(RpcRequest request)
	{
		String data = new String("Reserved for implementation-defined server-errors.");
		
		// サーバ側で何らかのエラーが発生した場合のRPC応答を生成する．
		RpcResponse response = RpcResponse.serverError(
			request, data
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse echo(RpcRequest request)
	{
		// RPC要求内のパラメータ(エコー文字列)を取得する．
		String echoString = (String)request.getParams();
		
		// エコー文字列を結果とするRPC応答を生成する．
		RpcResponse response = RpcResponse.responseWithResult(
			request, echoString
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse orderProducts(RpcRequest request)
	{
		// RPC要求内のパラメータを取得する．
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>)request.getParams();
		
		// RPC応答の結果を生成する．
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		int productNo = Integer.parseInt((String)params.get("productNo"));
		int quantity = Integer.parseInt((String)params.get("quantity"));
		int unitPrice = 100;
		int totalPrice = quantity * unitPrice;
		
		result.put("productNo",		Integer.toString(productNo));
		result.put("quantity",		Integer.toString(quantity));
		result.put("unitPrice",		Integer.toString(unitPrice));
		result.put("totalPrice",	Integer.toString(totalPrice));
		
		// 結果を含むRPC応答を生成する．
		RpcResponse response = RpcResponse.responseWithResult(
			request, result
		);
		// RPC応答を返す．
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse notification(RpcRequest request)
	{
		// Notification の場合は，null を返す．
		RpcResponse response = null;
		return response;
	}
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: Registerable
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: Registerable
	//
	@Override
	public String getMethodPrefix()
	{
		return METHOD_PREFIX;
	}
}
