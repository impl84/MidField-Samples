
package rc.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcStreamInfoManager
 *
 * Date Modified: 2022.06.09
 *
 */
public class RcStreamInfoManager
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final RcMfsNode mfs;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public List<RcStreamInfo> fetchSourceStreamInfoList(String addr)
        throws RemoteControlException
    {
        // ���u���\�b�h�Ăяo���̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // ������ݒ肷��D
        params.put("sourceNodeAddress", addr);
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>)this.mfs.invoke(
            "StreamInfoManager.fetchSourceStreamInfoList", params
        );
        // �X�g���[����񃊃X�g�𐶐����ĕԂ��D
        return newSourceStreamInfoList(result);
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    RcStreamInfoManager(RcMfsNode mfs)
    {
        if (mfs == null) {
            throw new NullPointerException();
        }
        this.mfs = mfs;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //  
    private List<RcStreamInfo> newSourceStreamInfoList(
        ArrayList<Map<String, Object>> result
    )
    {
        // �X�g���[����񃊃X�g�𐶐�����D
        List<RcStreamInfo> lsStmInf = new ArrayList<RcStreamInfo>();
        
        // ���u���\�b�h�Ăяo���̌��ʂƂ��ē���ꂽ�z��𑀍삵�C
        // �z��̗v�f���X�g���[����񃊃X�g�֒ǉ�����D
        for (int index = 0; index < result.size(); index++) {
            Map<String, Object> map = result.get(index);
            
            RcStreamInfo stmInf = new RcStreamInfo(
                index,
                (String)map.get("videoConnection"),
                (String)map.get("videoDescription"),
                (String)map.get("videoFormat"),
                (String)map.get("audioConnection"),
                (String)map.get("audioDescription"),
                (String)map.get("audioFormat")
            );
            lsStmInf.add(stmInf);
        }
        // �X�g���[����񃊃X�g��Ԃ��D
        return lsStmInf;
    }
}
