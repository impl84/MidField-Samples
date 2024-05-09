
package rpc.remocon;

import static com.midfield_system.rpc.api.server.ParamName.PERFORMER_ID;

import java.util.Map;
import java.util.TreeMap;

import com.midfield_system.rpc.api.RequestObject;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.07
 *
 */
public class CltStreamPerformer
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public CltStreamPerformer()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject newInstance(String[] args)
    {
        // RPC�v���𐶐����ĕԂ��D
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject start(String[] args)
    {
        // start �R�}���h�v���𐶐����ĕԂ��D
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject stop(String[] args)
    {
        // stop �R�}���h�v���𐶐����ĕԂ��D
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject delete(String[] args)
    {
        // delete �R�}���h�v���𐶐����ĕԂ��D
        return newRpcCommandRequest(args);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private RequestObject newRpcCommandRequest(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC�v���ɕK�v�ƂȂ�����̐����m�F����D
        if (args.length >= 2) {
            // RPC�v���p�̃}�b�v�𐶐�����D
            map = new TreeMap<String, Object>();
            
            // StreamPerformer �̃C���f�b�N�X�ԍ����}�b�v�ɐݒ肷��D
            map.put(PERFORMER_ID, args[1]);
        }
        // RPC�v���𐶐����ĕԂ��D
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
}