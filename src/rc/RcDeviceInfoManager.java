
package rc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcDeviceInfoManager
 *
 * Date Modified: 2022.06.16
 *
 */
public class RcDeviceInfoManager
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
    public List<RcDeviceInfo> getInputVideoDeviceInfoList()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>)this.mfs.invoke(
            "DeviceInfoManager.getInputVideoDeviceInfoList", null
        );
        // デバイスリストを生成して返す．
        return newInputDeviceInfoList(result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public List<RcDeviceInfo> getInputAudioDeviceInfoList()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>)this.mfs.invoke(
            "DeviceInfoManager.getInputAudioDeviceInfoList", null
        );
        // デバイスリストを生成して返す．
        return newInputDeviceInfoList(result);
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    RcDeviceInfoManager(RcMfsNode mfs)
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
    private List<RcDeviceInfo> newInputDeviceInfoList(
        ArrayList<Map<String, Object>> result
    )
    {
        // デバイスリストを生成する．
        List<RcDeviceInfo> lsDevInf = new ArrayList<RcDeviceInfo>();
        
        // 遠隔メソッド呼び出しの結果として得られた配列を操作し，
        // 配列の要素をデバイスリストへ追加する．
        for (int index = 0; index < result.size(); index++) {
            Map<String, Object> map = result.get(index);
            
            @SuppressWarnings("unchecked")
            RcDeviceInfo devInf = new RcDeviceInfo(
                index,
                (String)map.get("deviceName"),
                (ArrayList<String>)map.get("formatList"),
                Integer.parseInt((String)map.get("preferredIndex"))
            );
            lsDevInf.add(devInf);
        }
        // デバイスリストを返す．
        return lsDevInf;
    }
}
