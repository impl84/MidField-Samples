
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
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>)this.mfs.invoke(
            "DeviceInfoManager.getInputVideoDeviceInfoList", null
        );
        // �f�o�C�X���X�g�𐶐����ĕԂ��D
        return newInputDeviceInfoList(result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public List<RcDeviceInfo> getInputAudioDeviceInfoList()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>)this.mfs.invoke(
            "DeviceInfoManager.getInputAudioDeviceInfoList", null
        );
        // �f�o�C�X���X�g�𐶐����ĕԂ��D
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
        // �f�o�C�X���X�g�𐶐�����D
        List<RcDeviceInfo> lsDevInf = new ArrayList<RcDeviceInfo>();
        
        // ���u���\�b�h�Ăяo���̌��ʂƂ��ē���ꂽ�z��𑀍삵�C
        // �z��̗v�f���f�o�C�X���X�g�֒ǉ�����D
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
        // �f�o�C�X���X�g��Ԃ��D
        return lsDevInf;
    }
}
