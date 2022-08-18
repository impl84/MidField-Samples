
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
        // 遠隔メソッド呼び出しの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // 引数を設定する．
        params.put("videoDeviceIndex", Integer.toString(vidDev.getDeviceIndex()));
        params.put("videoFormatIndex", Integer.toString(vidDev.getPreferredIndex()));
        params.put("audioDeviceIndex", Integer.toString(audDev.getDeviceIndex()));
        params.put("audioFormatIndex", Integer.toString(audDev.getPreferredIndex()));
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureInputDevice", params
        );
        // 結果を返す．
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String configureIncomingStream(RcStreamInfo stmInf)
        throws RemoteControlException
    {
        // 遠隔メソッド呼び出しの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // 引数を設定する．
        params.put("streamInfoIndex", Integer.toString(stmInf.getStreamInfoIndex()));
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureIncomingStream", params
        );
        // 結果を返す．
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
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>)this.mfs.invoke(
            "SegmentIo.getOutputVideoFormatList", null
        );
        // フォーマットリストを生成して返す．
        return newOutputFormatList(result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public List<RcStreamFormat> getOutputAudioFormatList()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>)this.mfs.invoke(
            "SegmentIo.getOutputAudioFormatList", null
        );
        // フォーマットリストを生成して返す．
        return newOutputFormatList(result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String configureOutgoingStream(RcStreamFormat vidFmt, RcStreamFormat audFmt)
        throws RemoteControlException
    {
        // 遠隔メソッド呼び出しの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // 引数を設定する．
        params.put("videoFormatIndex", Integer.toString(vidFmt.getFormatIndex()));
        params.put("audioFormatIndex", Integer.toString(audFmt.getFormatIndex()));
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureOutgoingStream", params
        );
        // 結果を返す．
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String configureDefaultRenderer()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.configureDefaultRenderer", null
        );
        // 結果を返す．
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
        // 遠隔メソッド呼び出しの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // 引数を設定する．
        params.put("isLiveSource", Boolean.toString(isLiveSource));
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.setLiveSource", params
        );
        // 結果を返す．
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String setPreviewer()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.setPreviewer", null
        );
        // 結果を返す．
        return result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String setTransportProtocol(ProtocolType type, ConnectionMode mode)
        throws RemoteControlException
    {
        // 遠隔メソッド呼び出しの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        // 引数を設定する．
        params.put("protocolType", type.toString());
        params.put("connectionMode", mode.toString());
        
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke(
            "SegmentIo.setTransportProtocol", params
        );
        // 結果を返す．
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
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        String result = (String)this.mfs.invoke("SegmentIo.reset", null);
        
        // 結果を返す．
        return result;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private List<RcStreamFormat> newOutputFormatList(Map<String, Object> result)
    {
        // フォーマットリストを生成する．
        List<RcStreamFormat> lsStmFmt = new ArrayList<RcStreamFormat>();
        
        // 遠隔メソッド呼び出しの結果として得られたマップから，
        // 文字列のフォーマットリストを取得する．
        @SuppressWarnings("unchecked")
        ArrayList<String> list = (ArrayList<String>)result.get("formatList");
        
        // 文字列のフォーマットリストの要素を，フォーマットリストへ追加する．
        for (int index = 0; index < list.size(); index++) {
            RcStreamFormat stmFmt = new RcStreamFormat(
                index,
                list.get(index)
            );
            lsStmFmt.add(stmFmt);
        }
        // フォーマットリストを返す．
        return lsStmFmt;
    }
}
