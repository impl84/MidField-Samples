
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
		// ���P���ȃ��\�b�h�Ăяo���̗�F
		printTitle("�P���ȃ��\�b�h�Ăяo���̗�F");
		
		printResult(invoke("Ex.getBooleanResult", null));
		printResult(invoke("Ex.getSuccessResult", null));
		printResult(invoke("Ex.getResponseWithResult", null));
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleErrorResponse()
	{
		// ���e��G���[�������̗�F
		printTitle("�e��G���[�������̗�F");
		
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
		// ��Future<RpcResponse> �𗘗p����RPC�v���Ɖ����̗�F
		printTitle("Future<RpcResponse> �𗘗p����RPC�v���Ɖ����̗�F");
		
		Future<RpcResponse> future0 = sendRequest("Ex.getBooleanResult", null);
		Future<RpcResponse> future1 = sendRequest("Ex.getSuccessResult", null);
		printResult(waitResponse(future0));
		printResult(waitResponse(future1));
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleMethodWithStringParam()
	{
		// ����������p�����[�^�Ƃ���RPC�v���Ɖ����̗�F
		printTitle("��������p�����[�^�Ƃ���RPC�v���Ɖ����̗�F");
		
		// ��������p�����[�^�Ƃ���RPC���Ăяo���D
		String echoString = (String)invoke(
			"Ex.echo",
			"hello"
		);
		// RPC�̎��s���ʂ�\������D
		printResult(echoString);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void exampleMethodWithMapParam()
	{
		// �������̃p�����[�^�𔺂�RPC�v���Ɖ����̗�F
		printTitle("�����̃p�����[�^�𔺂�RPC�v���Ɖ����̗�F");
		
		// RPC�̈����ƂȂ�}�b�v�𐶐�����D
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		int productNo = 1;
		int quantity = 20;
		
		params.put("productNo", Integer.toString(productNo));
		params.put("quantity",  Integer.toString(quantity));
		
		// RPC���Ăяo���D
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>)invoke(
			"Ex.orderProducts",
			params
		);
		// RPC�̎��s���ʂ�\������D
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
		// ��RPC�v��(Notification)�̗�F
		printTitle("Notification �̗�F");
		
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
			Log.message("��invoke: %s(%s)", method, params);
			
			// �T�[�o�̃��\�b�h���Ăяo���CRPC�������擾����D
			RpcResponse response = this.rpcClient.invoke(
				method, params, 2000, TimeUnit.MILLISECONDS
			);	// InterruptedException, ExecutionException, TimeoutException
			
			// ���ʂ� null �̏ꍇ�̓G���[�����o�͂���D
			result = response.getResult();
			if (result == null) {
				Map<String, Object> error = response.getError();
				Log.message("�� error: code: %s, message: %s, data: %s",
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
		Log.message("��request: %s(%s)", method, params);
		
		// RPC�v�����T�[�o�֑���D
		Future<RpcResponse> future = this.rpcClient.request(method, params);
		return future;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private Object waitResponse(Future<RpcResponse> future)
	{
		Object result = null;
		try {
			// RPC�������擾����D
			RpcResponse response = future.get(2000, TimeUnit.MILLISECONDS);
				// InterruptedException, ExecutionException, TimeoutException
			
			// ���ʂ� null �̏ꍇ�̓G���[�����o�͂���D
			result = response.getResult();
			if (result == null) {
				Map<String, Object> error = response.getError();
				Log.message("�� error: code: %s, message: %s, data: %s",
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
		Log.message("��notify: %s(%s)", method, params);
		
		// RPC�v��(Notification)���T�[�o�֑���D
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
		Log.message("��%s", title);
		Log.message(Log.LINE_SEPARATOR);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printResult(Object result)
	{
		Log.message("��result: %s", result);
		Log.message();
	}
}
