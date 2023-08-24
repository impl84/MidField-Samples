
package rpc.performer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.rpc.api.InternalErrorListener;
import com.midfield_system.rpc.api.client.MfsRemote;
import com.midfield_system.rpc.api.client.PerformerId;
import com.midfield_system.rpc.api.client.RemoteControlException;
import com.midfield_system.rpc.api.client.SegmentIo;
import com.midfield_system.rpc.api.client.StreamPerformer;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.25
 *
 */
public class RemoteOperator
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final int DEFAULT_PORT_NUMBER = 60202;
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final MfsRemote mfsRmt;
    
    private final Map<PerformerId, StreamPerformer> mapPfmr;
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public RemoteOperator(String nodeName, InternalErrorListener errHandler)
        throws UnknownHostException,
            IOException
    {
        this(nodeName, DEFAULT_PORT_NUMBER, errHandler);
    }
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public RemoteOperator(
        String nodeAddr, int portNumber, InternalErrorListener errHandler
    )
        throws UnknownHostException,
            IOException
    {
        // 遠隔の MidField System に対して操作を開始する．
        this.mfsRmt = new MfsRemote(nodeAddr, portNumber, errHandler);
        
        // 遠隔の MidField System への操作を開始する．
        this.mfsRmt.initializeRemoteControl();
        
        // 遠隔の MidField System で動作する StreamPerformer のマップを生成する．
        this.mapPfmr = new HashMap<PerformerId, StreamPerformer>();
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void shutdownAll()
    {
        // 動作中の StreamPerformer を全て削除する．
        this.mapPfmr.values().stream()
            .forEach(StreamPerformer::delete);
        
        // 遠隔の MidField System への操作を終了する．
        this.mfsRmt.shutdownRemoteControl();
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: 
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupDeviceToRenderer()
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼ビデオとオーディオデバイスで入力を構成する．
        configureInputDevice(segIo);
        
        // ▼推奨レンダラで出力を構成する．
        boolean usePreferredRenderer = true;
        configureRenderer(segIo, usePreferredRenderer);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupDeviceToNetwork()
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼ビデオとオーディオデバイスで入力を構成する．
        configureInputDevice(segIo);
        
        // ▼送信ストリームで出力を構成する．
        configureOutgoingStream(segIo, true);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupDeviceToMixer(PerformerId mixerId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼ビデオとオーディオデバイスで入力を構成する．
        configureInputDevice(segIo);
        
        // ▼ミキサーへの入力として出力を構成する．
        configureMixerInput(mixerId, segIo);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupNetworkToRenderer(PerformerId sourceId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼受信ストリームで入力を構成する．
        configureIncomingStream(sourceId, segIo);
        
        // ▼推奨レンダラで出力を構成する．
        boolean usePreferredRenderer = true;
        configureRenderer(segIo, usePreferredRenderer);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupNetworkToNetwork(PerformerId sourceId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼受信ストリームで入力を構成する．
        configureIncomingStream(sourceId, segIo);
        
        // ▼送信ストリームで出力を構成する．
        // ・推奨プレビューワーを利用する．
        boolean usePreferredPreviewer = true;
        configureOutgoingStream(segIo, usePreferredPreviewer);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupNetworkToMixer(PerformerId sourceId, PerformerId mixerId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼受信ストリームで入力を構成する．
        configureIncomingStream(sourceId, segIo);
        
        // ▼ミキサーへの入力として出力を構成する．
        configureMixerInput(mixerId, segIo);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupMixerToRenderer(String mixerName)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼ミキサーとして入力を構成する．
        configureStreamingMixer(mixerName, segIo);
        
        // ▼レンダラで出力を構成する．
        // ・推奨レンダラでは無く，デフォルトレンダラを利用する．
        boolean usePreferredRenderer = false;
        configureRenderer(segIo, usePreferredRenderer);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupMixerToNetwork(String mixerName)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ▼ミキサーとして入力を構成する．
        configureStreamingMixer(mixerName, segIo);
        
        // ▼送信ストリームで出力を構成する．
        // ・推奨プレビューワーでは無く，デフォルトプレビューワーを利用する．
        boolean usePreferredPreviewer = false;
        configureOutgoingStream(segIo, usePreferredPreviewer);
        
        // ▼RcStreamPerformer を生成して処理を開始し，そのIDを返す．
        return setupStreamPerformer(segIo);
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void startPerformer(PerformerId pfmrId)
        throws RemoteControlException
    {
        var pfmr = this.mapPfmr.get(pfmrId);
        if (pfmr != null) {
            pfmr.start();
        }
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void stopPerformer(PerformerId pfmrId)
        throws RemoteControlException
    {
        var pfmr = this.mapPfmr.get(pfmrId);
        if (pfmr != null) {
            pfmr.stop();
        }
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void cleanupPerformer(PerformerId pfmrId)
    {
        var pfmr = this.mapPfmr.remove(pfmrId);
        if (pfmr != null) {
            pfmr.delete();
        }
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD: SegmentIo の入力の構成
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureInputDevice(SegmentIo segIo)
        throws RemoteControlException
    {
        // ▼ビデオとオーディオデバイスで入力を構成する．
        // ・入力デバイスを選択して設定する．
        var devInfMgr = this.mfsRmt.getDeviceInfoManager();
        
        var lsVidDev = devInfMgr.getVideoInputDeviceInfoList();
        if (lsVidDev.size() <= 0) {
            throw new RemoteControlException(
                "※利用可能なビデオ入力デバイスがありません．"
            );
        }
        var lsAudDev = devInfMgr.getAudioInputDeviceInfoList();
        if (lsAudDev.size() <= 0) {
            throw new RemoteControlException(
                "※利用可能なオーディオ入力デバイスがありません．"
            );
        }
        segIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureIncomingStream(PerformerId srcPfmrId, SegmentIo segIo)
        throws RemoteControlException
    {
        // ▼受信ストリームで入力を構成する．
        // ・送信元が送信しているストリームの中から，
        // 引数で与えられたパフォーマIDのストリームを選択して使う．
        var stmInfMgr = this.mfsRmt.getStreamInfoManager();
        var srcAddr   = srcPfmrId.getNodeAddress();
        var lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(srcAddr);
        
        var stmInf = lsStmInf.stream()
            .filter(inf -> srcPfmrId.equals(inf.getVideoPerformerId()))
            .findFirst()
            .orElseThrow(
                () -> new RemoteControlException(
                    "※受信すべきストリームがありません．"
                )
            );
        
        // RcSegmentIo の入力を受信ストリームとして構成する．
        segIo.configureIncomingStream(stmInf);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureStreamingMixer(String mixerName, SegmentIo segIo)
        throws RemoteControlException
    {
        // ▼ミキサーとして入力を構成する．
        // ・ミキサーのフォーマットには，システムプロパティの設定値を適用する．
        segIo.configureStreamingMixer(mixerName);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD: SegmentIo の出力の構成
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureOutgoingStream(SegmentIo segIo, boolean usePreferredPreviewer)
        throws RemoteControlException
    {
        // ▼送信ストリームで出力を構成する．
        // ・送信フォーマットを選択して設定する．
        // ・TCPを利用し，コネクション接続要求を受け入れる．
        // ・推奨プレビューワ―を利用する．
        var lsVidFmt = segIo.getOutputVideoFormatList();
        if (lsVidFmt.size() <= 0) {
            throw new RemoteControlException(
                "※送信可能なビデオフォーマットがありません．"
            );
        }
        var lsAudFmt = segIo.getOutputAudioFormatList();
        if (lsAudFmt.size() <= 0) {
            throw new RemoteControlException(
                "※送信可能なオーディオフォーマットがありません．"
            );
        }
        segIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
        segIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
        if (usePreferredPreviewer) {
            segIo.setPreferredPreviewer();
        }
        else {
            segIo.setPreviewer();
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureRenderer(SegmentIo segIo, boolean usePreferredRenderer)
        throws RemoteControlException
    {
        if (usePreferredRenderer) {
            segIo.configurePreferredRenderer();
        }
        else {
            segIo.configureRenderer();
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureMixerInput(PerformerId mixerId, SegmentIo segIo)
        throws RemoteControlException
    {
        // ▼ミキサーへの入力として出力を構成する．
        segIo.configureMixerInput(mixerId);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private PerformerId setupStreamPerformer(SegmentIo segIo)
        throws RemoteControlException
    {
        var pfmr = this.mfsRmt.newStreamPerformer(segIo);
        pfmr.start();
        
        var pfmrId = pfmr.getPerformerId();
        this.mapPfmr.put(pfmrId, pfmr);
        
        return pfmrId;
    }
}