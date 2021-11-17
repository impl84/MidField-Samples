
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
//  PUBLIC METHOD: RPC�v�������̗�
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getBooleanResult(RpcRequest request)
	{
		boolean result = true;
		
		// Boolean �l�����ʂƂ���RPC�����𐶐�����D
		RpcResponse response = RpcResponse.booleanResponse(
			request, result
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getSuccessResult(RpcRequest request)
	{
		boolean isSuccess = true;
		
		// "success" (�܂��� "failure") �����ʂƂ���RPC�����𐶐�����D
		RpcResponse response = RpcResponse.successResponse(
			request, isSuccess
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getResponseWithResult(RpcRequest request)
	{
		Object result = new String("Any resulting object.");
		
		// ���炩�̃I�u�W�F�N�g�����ʂƂ���RPC�����𐶐�����D
		RpcResponse response = RpcResponse.responseWithResult(
			request, result
		);
		// RPC������Ԃ��D
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
		// ��̓G���[��������RPC�����𐶐�����D
		RpcResponse response = RpcResponse.parseError(
			data
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getInvalidRequest(RpcRequest request)
	{
		// �����ȃ��N�G�X�g����M�����ꍇ��RPC�����𐶐�����D
		RpcResponse response = RpcResponse.invalidRequest(
			request
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getMethodNotFound(RpcRequest request)
	{
		// ���\�b�h�����݂��Ȃ�/���p�ł��Ȃ��ꍇ��RPC�����𐶐�����D
		RpcResponse response = RpcResponse.methodNotFound(
			request
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getInvalidParams(RpcRequest request)
	{
		String data = new String("Invalid method parameter(s).");
		
		// ���\�b�h�̃p�����[�^�������ȏꍇ��RPC�����𐶐�����D
		RpcResponse response = RpcResponse.invalidParams(
			request, data
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getInternalError(RpcRequest request)
	{
		String data = new String("Internal JSON-RPC error.");
		
		// �����G���[�����������ꍇ��RPC�����𐶐�����D
		RpcResponse response = RpcResponse.internalError(
			request, data
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse getServerError(RpcRequest request)
	{
		String data = new String("Reserved for implementation-defined server-errors.");
		
		// �T�[�o���ŉ��炩�̃G���[�����������ꍇ��RPC�����𐶐�����D
		RpcResponse response = RpcResponse.serverError(
			request, data
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse echo(RpcRequest request)
	{
		// RPC�v�����̃p�����[�^(�G�R�[������)���擾����D
		String echoString = (String)request.getParams();
		
		// �G�R�[����������ʂƂ���RPC�����𐶐�����D
		RpcResponse response = RpcResponse.responseWithResult(
			request, echoString
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse orderProducts(RpcRequest request)
	{
		// RPC�v�����̃p�����[�^���擾����D
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>)request.getParams();
		
		// RPC�����̌��ʂ𐶐�����D
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		int productNo = Integer.parseInt((String)params.get("productNo"));
		int quantity = Integer.parseInt((String)params.get("quantity"));
		int unitPrice = 100;
		int totalPrice = quantity * unitPrice;
		
		result.put("productNo",		Integer.toString(productNo));
		result.put("quantity",		Integer.toString(quantity));
		result.put("unitPrice",		Integer.toString(unitPrice));
		result.put("totalPrice",	Integer.toString(totalPrice));
		
		// ���ʂ��܂�RPC�����𐶐�����D
		RpcResponse response = RpcResponse.responseWithResult(
			request, result
		);
		// RPC������Ԃ��D
		return response;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public RpcResponse notification(RpcRequest request)
	{
		// Notification �̏ꍇ�́Cnull ��Ԃ��D
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
