
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
        // ���u�� MidField System �ɑ΂��đ�����J�n����D
        this.mfsRmt = new MfsRemote(nodeAddr, portNumber, errHandler);
        
        // ���u�� MidField System �ւ̑�����J�n����D
        this.mfsRmt.initializeRemoteControl();
        
        // ���u�� MidField System �œ��삷�� StreamPerformer �̃}�b�v�𐶐�����D
        this.mapPfmr = new HashMap<PerformerId, StreamPerformer>();
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void shutdownAll()
    {
        // ���쒆�� StreamPerformer ��S�č폜����D
        this.mapPfmr.values().stream()
            .forEach(StreamPerformer::delete);
        
        // ���u�� MidField System �ւ̑�����I������D
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
        
        // ���r�f�I�ƃI�[�f�B�I�f�o�C�X�œ��͂��\������D
        configureInputDevice(segIo);
        
        // �����������_���ŏo�͂��\������D
        boolean usePreferredRenderer = true;
        configureRenderer(segIo, usePreferredRenderer);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupDeviceToNetwork()
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ���r�f�I�ƃI�[�f�B�I�f�o�C�X�œ��͂��\������D
        configureInputDevice(segIo);
        
        // �����M�X�g���[���ŏo�͂��\������D
        configureOutgoingStream(segIo, true);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupDeviceToMixer(PerformerId mixerId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ���r�f�I�ƃI�[�f�B�I�f�o�C�X�œ��͂��\������D
        configureInputDevice(segIo);
        
        // ���~�L�T�[�ւ̓��͂Ƃ��ďo�͂��\������D
        configureMixerInput(mixerId, segIo);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupNetworkToRenderer(PerformerId sourceId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ����M�X�g���[���œ��͂��\������D
        configureIncomingStream(sourceId, segIo);
        
        // �����������_���ŏo�͂��\������D
        boolean usePreferredRenderer = true;
        configureRenderer(segIo, usePreferredRenderer);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupNetworkToNetwork(PerformerId sourceId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ����M�X�g���[���œ��͂��\������D
        configureIncomingStream(sourceId, segIo);
        
        // �����M�X�g���[���ŏo�͂��\������D
        // �E�����v���r���[���[�𗘗p����D
        boolean usePreferredPreviewer = true;
        configureOutgoingStream(segIo, usePreferredPreviewer);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupNetworkToMixer(PerformerId sourceId, PerformerId mixerId)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ����M�X�g���[���œ��͂��\������D
        configureIncomingStream(sourceId, segIo);
        
        // ���~�L�T�[�ւ̓��͂Ƃ��ďo�͂��\������D
        configureMixerInput(mixerId, segIo);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupMixerToRenderer(String mixerName)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ���~�L�T�[�Ƃ��ē��͂��\������D
        configureStreamingMixer(mixerName, segIo);
        
        // �������_���ŏo�͂��\������D
        // �E���������_���ł͖����C�f�t�H���g�����_���𗘗p����D
        boolean usePreferredRenderer = false;
        configureRenderer(segIo, usePreferredRenderer);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
        return setupStreamPerformer(segIo);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public PerformerId setupMixerToNetwork(String mixerName)
        throws RemoteControlException
    {
        var segIo = this.mfsRmt.newSegmentIo();
        
        // ���~�L�T�[�Ƃ��ē��͂��\������D
        configureStreamingMixer(mixerName, segIo);
        
        // �����M�X�g���[���ŏo�͂��\������D
        // �E�����v���r���[���[�ł͖����C�f�t�H���g�v���r���[���[�𗘗p����D
        boolean usePreferredPreviewer = false;
        configureOutgoingStream(segIo, usePreferredPreviewer);
        
        // ��RcStreamPerformer �𐶐����ď������J�n���C����ID��Ԃ��D
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
// PRIVATE METHOD: SegmentIo �̓��͂̍\��
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureInputDevice(SegmentIo segIo)
        throws RemoteControlException
    {
        // ���r�f�I�ƃI�[�f�B�I�f�o�C�X�œ��͂��\������D
        // �E���̓f�o�C�X��I�����Đݒ肷��D
        var devInfMgr = this.mfsRmt.getDeviceInfoManager();
        
        var lsVidDev = devInfMgr.getVideoInputDeviceInfoList();
        if (lsVidDev.size() <= 0) {
            throw new RemoteControlException(
                "�����p�\�ȃr�f�I���̓f�o�C�X������܂���D"
            );
        }
        var lsAudDev = devInfMgr.getAudioInputDeviceInfoList();
        if (lsAudDev.size() <= 0) {
            throw new RemoteControlException(
                "�����p�\�ȃI�[�f�B�I���̓f�o�C�X������܂���D"
            );
        }
        segIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureIncomingStream(PerformerId srcPfmrId, SegmentIo segIo)
        throws RemoteControlException
    {
        // ����M�X�g���[���œ��͂��\������D
        // �E���M�������M���Ă���X�g���[���̒�����C
        // �����ŗ^����ꂽ�p�t�H�[�}ID�̃X�g���[����I�����Ďg���D
        var stmInfMgr = this.mfsRmt.getStreamInfoManager();
        var srcAddr   = srcPfmrId.getNodeAddress();
        var lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(srcAddr);
        
        var stmInf = lsStmInf.stream()
            .filter(inf -> srcPfmrId.equals(inf.getVideoPerformerId()))
            .findFirst()
            .orElseThrow(
                () -> new RemoteControlException(
                    "����M���ׂ��X�g���[��������܂���D"
                )
            );
        
        // RcSegmentIo �̓��͂���M�X�g���[���Ƃ��č\������D
        segIo.configureIncomingStream(stmInf);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureStreamingMixer(String mixerName, SegmentIo segIo)
        throws RemoteControlException
    {
        // ���~�L�T�[�Ƃ��ē��͂��\������D
        // �E�~�L�T�[�̃t�H�[�}�b�g�ɂ́C�V�X�e���v���p�e�B�̐ݒ�l��K�p����D
        segIo.configureStreamingMixer(mixerName);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD: SegmentIo �̏o�͂̍\��
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void configureOutgoingStream(SegmentIo segIo, boolean usePreferredPreviewer)
        throws RemoteControlException
    {
        // �����M�X�g���[���ŏo�͂��\������D
        // �E���M�t�H�[�}�b�g��I�����Đݒ肷��D
        // �ETCP�𗘗p���C�R�l�N�V�����ڑ��v�����󂯓����D
        // �E�����v���r���[���\�𗘗p����D
        var lsVidFmt = segIo.getOutputVideoFormatList();
        if (lsVidFmt.size() <= 0) {
            throw new RemoteControlException(
                "�����M�\�ȃr�f�I�t�H�[�}�b�g������܂���D"
            );
        }
        var lsAudFmt = segIo.getOutputAudioFormatList();
        if (lsAudFmt.size() <= 0) {
            throw new RemoteControlException(
                "�����M�\�ȃI�[�f�B�I�t�H�[�}�b�g������܂���D"
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
        // ���~�L�T�[�ւ̓��͂Ƃ��ďo�͂��\������D
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