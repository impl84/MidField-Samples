
package remocon;

import static com.midfield_system.api.system.rpc.ParamName.SOURCE_NODE_ADDRESS;

import java.util.HashMap;
import java.util.Map;

import com.midfield_system.api.system.rpc.RpcRequest;

/*----------------------------------------------------------------------------*/
/**
 * CltStreamInfoManager
 *
 * Copyright (C) Koji Hashimoto
 *
 * Date Modified: 2021.09.02 Koji Hashimoto
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
    public RpcRequest fetchSourceStreamInfoList(String[] args)
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
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], map);
        return rpcReq;
    }
}