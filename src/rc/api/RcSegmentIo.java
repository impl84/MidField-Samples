
package rc.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcSegmentIo
 *
 * Date Modified: 2022.06.16
 *
 */
public class RcSegmentIo
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
    public String configureInputDevice(RcDeviceInfo vidDev, RcDeviceInfo audDev)
        throws RemoteControlException
    {
        // ���u���\�b�h�Ăяo���̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // ������ݒ肷��D
        params.put("videoDeviceIndex", Integer.toString(vidDev.getDeviceIndex()));
        params.put("videoFormatIndex", Integer.toString(vidDev.getPreferredIndex()));
        params.put("audioDeviceIndex", Integer.toString(audDev.getDeviceIndex()));
        params.put("audioFormatIndex", Integer.toString(audDev.getPreferredIndex()));
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureInputDevice", params
        );
        // ���ʂ�Ԃ��D
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String configureIncomingStream(RcStreamInfo stmInf)
        throws RemoteControlException
    {
        // ���u���\�b�h�Ăяo���̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // ������ݒ肷��D
        params.put("streamInfoIndex", Integer.toString(stmInf.getStreamInfoIndex()));
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureIncomingStream", params
        );
        // ���ʂ�Ԃ��D
        return result;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public List<RcStreamFormat> getOutputVideoFormatList()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>)this.mfs.invoke(
            "SegmentIo.getOutputVideoFormatList", null
        );
        // �t�H�[�}�b�g���X�g�𐶐����ĕԂ��D
        return newOutputFormatList(result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public List<RcStreamFormat> getOutputAudioFormatList()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>)this.mfs.invoke(
            "SegmentIo.getOutputAudioFormatList", null
        );
        // �t�H�[�}�b�g���X�g�𐶐����ĕԂ��D
        return newOutputFormatList(result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String configureOutgoingStream(RcStreamFormat vidFmt, RcStreamFormat audFmt)
        throws RemoteControlException
    {
        // ���u���\�b�h�Ăяo���̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // ������ݒ肷��D
        params.put("videoFormatIndex", Integer.toString(vidFmt.getFormatIndex()));
        params.put("audioFormatIndex", Integer.toString(audFmt.getFormatIndex()));
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureOutgoingStream", params
        );
        // ���ʂ�Ԃ��D
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String configureDefaultRenderer()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureDefaultRenderer", null
        );
        // ���ʂ�Ԃ��D
        return result;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String setLiveSource(boolean isLiveSource)
        throws RemoteControlException
    {
        // ���u���\�b�h�Ăяo���̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // ������ݒ肷��D
        params.put("isLiveSource", Boolean.toString(isLiveSource));
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.setLiveSource", params
        );
        // ���ʂ�Ԃ��D
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String setPreviewer()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.setPreviewer", null
        );
        // ���ʂ�Ԃ��D
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String setTransportProtocol(ProtocolType type, ConnectionMode mode)
        throws RemoteControlException
    {
        // ���u���\�b�h�Ăяo���̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // ������ݒ肷��D
        params.put("protocolType", type.toString());
        params.put("connectionMode", mode.toString());
        
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.setTransportProtocol", params
        );
        // ���ʂ�Ԃ��D
        return result;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    RcSegmentIo(RcMfsNode mfs)
    {
        if (mfs == null) {
            throw new NullPointerException();
        }
        this.mfs = mfs;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    String reset()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        String result = (String)this.mfs.invoke("SegmentIo.reset", null);
        
        // ���ʂ�Ԃ��D
        return result;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private List<RcStreamFormat> newOutputFormatList(Map<String, Object> result)
    {
        // �t�H�[�}�b�g���X�g�𐶐�����D
        List<RcStreamFormat> lsStmFmt = new ArrayList<RcStreamFormat>();
        
        // ���u���\�b�h�Ăяo���̌��ʂƂ��ē���ꂽ�}�b�v����C
        // ������̃t�H�[�}�b�g���X�g���擾����D
        @SuppressWarnings("unchecked")
        ArrayList<String> list = (ArrayList<String>)result.get("formatList");
        
        // ������̃t�H�[�}�b�g���X�g�̗v�f���C�t�H�[�}�b�g���X�g�֒ǉ�����D
        for (int index = 0; index < list.size(); index++) {
            RcStreamFormat stmFmt = new RcStreamFormat(
                index,
                list.get(index)
            );
            lsStmFmt.add(stmFmt);
        }
        // �t�H�[�}�b�g���X�g��Ԃ��D
        return lsStmFmt;
    }
}
