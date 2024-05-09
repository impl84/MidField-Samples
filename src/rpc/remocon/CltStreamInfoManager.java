
package rpc.remocon;

import static com.midfield_system.rpc.api.server.ParamName.SOURCE_NODE_ADDRESS;

import java.util.HashMap;
import java.util.Map;

import com.midfield_system.rpc.api.RequestObject;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2022.06.08
 *
 */
public class CltStreamInfoManager
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public CltStreamInfoManager()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject fetchSourceStreamInfoList(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC�v���ɕK�v�ƂȂ�����̐����m�F����D
        if (args.length >= 2) {
            // RPC�v���p�̃}�b�v�𐶐�����D
            map = new HashMap<String, Object>();
            
            // �\�[�X�m�[�h�A�h���X���}�b�v�ɐݒ肷��D
            map.put(SOURCE_NODE_ADDRESS, args[1]);
        }
        // RPC�v���𐶐����ĕԂ��D
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
}