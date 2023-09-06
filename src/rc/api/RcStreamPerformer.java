
package rc.api;

import java.util.LinkedHashMap;
import java.util.Map;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcStreamPerformer
 *
 * Date Modified: 2022.06.21
 *
 */
public class RcStreamPerformer
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final RcMfsNode mfs;
    private final int       performerIndex;
    
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
        // �Ή����鉓�u���\�b�h���Ăяo���C���ʂ�Ԃ��D
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.open");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String start()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���C���ʂ�Ԃ��D
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.start");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String stop()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���C���ʂ�Ԃ��D
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.stop");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String close()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���C���ʂ�Ԃ��D
        // (RemoteControlException)
        return invokeRemoteMethod("StreamPerformer.close");
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String delete()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���C���ʂ�Ԃ��D
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
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>)this.mfs.invoke(
            "StreamPerformer.newInstance", null
        );
        // ���ʂ��� performerIndex ���擾����D
        this.performerIndex = Integer.parseInt((String)map.get("performerIndex"));
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private String invokeRemoteMethod(String methodName)
        throws RemoteControlException
    {
        // ���u���\�b�h�Ăяo���̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // ������ݒ肷��D
        params.put("performerIndex", Integer.toString(this.performerIndex));
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(methodName, params);
        
        // ���ʂ�Ԃ��D
        return result;
    }
}
