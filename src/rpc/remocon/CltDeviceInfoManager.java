
package rpc.remocon;

import com.midfield_system.rpc.api.RequestObject;

/**
 * 
 * Sample code for MidField API
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
    public RequestObject getVideoInputDeviceInfoList(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject getAudioInputDeviceInfoList(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
}