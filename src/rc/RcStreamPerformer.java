
package rc;

import java.util.LinkedHashMap;
import java.util.Map;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcStreamPerformer
 *
 * Date Modified: 2022.06.08
 *
 */
public class RcStreamPerformer
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final RcMfsNode mfs;
    private final int       performerNumber;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String open()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出し，結果を返す．
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.open");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String start()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出し，結果を返す．
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.start");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String stop()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出し，結果を返す．
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.stop");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String close()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出し，結果を返す．
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.close");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String delete()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出し，結果を返す．
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.delete");
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    RcStreamPerformer(RcMfsNode mfs)
        throws RemoteControlException
    {
        if (mfs == null) {
            throw new NullPointerException();
        }
        this.mfs = mfs;
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>)this.mfs.invoke(
            "StreamPerformer.newInstance", null
        );
        // 結果から performerNumber を取得する．
        this.performerNumber = Integer.parseInt((String)map.get("performerNumber"));
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private String invokeRemoteMethod(String methodName)
        throws RemoteControlException
    {
        // 遠隔メソッド呼び出しの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // 引数を設定する．
        params.put("performerNumber", Integer.toString(this.performerNumber));
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(methodName, params);
        
        // 結果を返す．
        return result;
    }
}
