
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
        // 遠隔メソッド呼び出しの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // 引数を設定する．
        params.put("sourceNodeAddress", addr);
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>)this.mfs.invoke(
            "StreamInfoManager.fetchSourceStreamInfoList", params
        );
        // ストリーム情報リストを生成して返す．
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
        // ストリーム情報リストを生成する．
        List<RcStreamInfo> lsStmInf = new ArrayList<RcStreamInfo>();
        
        // 遠隔メソッド呼び出しの結果として得られた配列を操作し，
        // 配列の要素をストリーム情報リストへ追加する．
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
        // ストリーム情報リストを返す．
        return lsStmInf;
    }
}
