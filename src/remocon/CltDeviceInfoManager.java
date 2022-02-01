
package remocon;

import com.midfield_system.api.system.rpc.RpcRequest;

/*----------------------------------------------------------------------------*/
/**
 * CltDeviceInfoManager 
 *
 * Copyright (C) Koji Hashimoto
 *
 * Date Modified: 2021.09.03
 * Koji Hashimoto 
 *
 */
public class CltDeviceInfoManager
{
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PUBLIC METHOD:
// -----------------------------------------------------------------------------

	// - CONSTRUCTOR -----------------------------------------------------------
	//
	public CltDeviceInfoManager()
	{
		//
	}
	
// -----------------------------------------------------------------------------
//  PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	//	
	public RpcRequest getInputVideoDeviceInfoList(String[] args)
	{
		// RPC�v���𐶐����ĕԂ��D
		RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
		return rpcReq;
	}
	
	// - PUBLIC METHOD ---------------------------------------------------------
	//	
	public RpcRequest getInputAudioDeviceInfoList(String[] args)
	{
		// RPC�v���𐶐����ĕԂ��D
		RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
		return rpcReq;
	}
}