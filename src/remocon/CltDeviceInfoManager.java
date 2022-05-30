
package remocon;

import com.midfield_system.api.rpc.RpcRequest;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: CltDeviceInfoManager
 * 
 * Date Modified: 2022.06.08
 *
 */
public class CltDeviceInfoManager
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public CltDeviceInfoManager()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest getInputVideoDeviceInfoList(String[] args)
    {
        // RPC要求を生成して返す．
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest getInputAudioDeviceInfoList(String[] args)
    {
        // RPC要求を生成して返す．
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
        return rpcReq;
    }
}