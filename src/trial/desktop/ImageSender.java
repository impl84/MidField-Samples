
package trial.desktop;

import java.util.List;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.stream.VideoFormat;
import com.midfield_system.api.stream.event.FlowUpdateEvent;
import com.midfield_system.api.stream.event.IoStatusEvent;
import com.midfield_system.api.stream.event.MixerStatusEvent;
import com.midfield_system.api.stream.event.PerformerStateEvent;
import com.midfield_system.api.stream.event.RendererStatusEvent;
import com.midfield_system.api.stream.event.SegmentEvent;
import com.midfield_system.api.stream.event.StreamEvent;
import com.midfield_system.api.stream.event.StreamEventListener;
import com.midfield_system.api.stream.event.StreamExceptionEvent;
import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Log;

/*----------------------------------------------------------------------------*/
/**
 * Sample code for MidField API: ImageSender
 *
 * Date Modified: 2021.09.20
 *
 */
public class ImageSender
    implements
        StreamEventListener
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    
    // �f�X�N�g�b�v�C���[�W���L���v�`������ۂ̃t���[�����[�g
    // �i�l�� 2.0/4.0/8.0/16.0 �̂����ꂩ�D�j
    private static final double DEF_FRAME_RATE = 4.0;
    
    private static final String STR_FILTER_NOT_FOUND   = "�K�؂ȃf�X�N�g�b�v�L���v�`���t�B���^������܂���D";
    private static final String STR_NO_SUITABLE_FORMAT = "�K�؂ȃL���v�`���t�H�[�}�b�g������܂���D";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final DesktopServer server;
    
    private StreamPerformer pfmr     = null;
    private IoParam         outParam = null;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: StreamEventListener
    //
    @Override
    public void update(StreamEvent ev)
    {
        if (ev instanceof IoStatusEvent) {
            evHn_IoStatus((IoStatusEvent)ev);
        }
        else if (ev instanceof RendererStatusEvent) {
            evHn_RendererStatus((RendererStatusEvent)ev);
        }
        else if (ev instanceof MixerStatusEvent) {
            evHn_MixerStatus((MixerStatusEvent)ev);
        }
        else if (ev instanceof PerformerStateEvent) {
            evHn_PerformerState((PerformerStateEvent)ev);
        }
        else if (ev instanceof SegmentEvent) {
            evHn_Segment((SegmentEvent)ev);
        }
        else if (ev instanceof FlowUpdateEvent) {
            evHn_FlowUpdate((FlowUpdateEvent)ev);
        }
        else if (ev instanceof StreamExceptionEvent) {
            evHn_StreamExceptionEvent((StreamExceptionEvent)ev);
        }
        else {
            Log.error(ev.toString());
        }
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    ImageSender(DesktopServer server)
        throws SystemException,
            StreamException
    {
        this.server = server;
        
        // �f�X�N�g�b�v�C���[�W�L���v�`���p DeviceInfo ���擾����D
        DeviceInfo devInf = getDesktopImageSource();
        // SystemException
        
        // �K�؂ȃt�H�[�}�b�g���擾����D
        StreamFormat capFmt = getSuitableCaptureFormat(devInf);
        // SystemException
        
        // ���̓f�o�C�X�� SegmentIo �̓��͂��\������D
        SegmentIo segIo = new SegmentIo();
        segIo.configureInputDevice(devInf, capFmt, null, null);
        
        // �o�͉\�ȃt�H�[�}�b�g��񃊃X�g���擾����D
        List<StreamFormat> lsOutFmt = segIo.getOutputVideoFormatList();
        
        // �o�̓t�H�[�}�b�g�����擾����D�i�����ł͍ŏ��̗v�f��I���j
        VideoFormat outFmt = (VideoFormat)lsOutFmt.get(0);
        
        // SegmentIo �̏o�͂�ݒ肷��D
        ProtocolType   type = ProtocolType.TCP;
        ConnectionMode mode = ConnectionMode.PASSIVE;
        
        segIo.configureOutgoingStream(outFmt, null);
        segIo.setTransportProtocol(type, mode);
        segIo.setPrivateOutput(true);
        
        // Stream Performer �̐����Ɠ��o�͏����̊J�n�D
        setupPerformer(segIo);
        // SystemException, StreamException
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    IoParam getOutputParam()
    {
        return this.outParam;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void close()
    {
        if (this.pfmr != null) {
            this.pfmr.removeStreamEventListener(this);
            this.pfmr.delete();
            this.pfmr = null;
        }
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private DeviceInfo getDesktopImageSource()
        throws SystemException
    {
        DeviceInfoManager devMgr    = DeviceInfoManager.getInstance();
        DeviceInfo        dskImgSrc = devMgr.getDefaultDesktopImageSource();
        if (dskImgSrc == null) {
            // IO�f�o�C�X���X�g�̒��Ƀt�B���^�������D
            throw new SystemException(STR_FILTER_NOT_FOUND);
        }
        return dskImgSrc;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private StreamFormat getSuitableCaptureFormat(DeviceInfo devInf)
        throws SystemException
    {
        StreamFormat fmt = null;
        
        // �擾�����t���[�����[�g�̃t�H�[�}�b�g��T���D
        List<StreamFormat> lsStmFmt = devInf.getOutputFormatList();
        for (StreamFormat stmFmt : lsStmFmt) {
            if ((stmFmt instanceof VideoFormat) == false) {
                continue;
            }
            // -----------------------------------------------------------------
            VideoFormat vidFmt    = (VideoFormat)stmFmt;
            double      frameRate = vidFmt.getFrameRate();
            if (frameRate == DEF_FRAME_RATE) {
                // �K�؂ȃt�H�[�}�b�g�����݂���ꍇ�F
                fmt = vidFmt;
                break;
            }
        }
        if (fmt == null) {
            // �K�؂ȃt�H�[�}�b�g�����݂��Ȃ��ꍇ�F
            throw new SystemException(STR_NO_SUITABLE_FORMAT);
        }
        return fmt;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void setupPerformer(SegmentIo segIo)
        throws SystemException,
            StreamException
    {
        this.pfmr = null;
        try {
            // SegmentIo �����ƂɁCStream Performer �𐶐�����D
            this.pfmr = StreamPerformer.newInstance(segIo);
            // SystemException,StreamException
            
            // ���̃C���X�^���X�� StreamEventListener �Ƃ��ēo�^����D
            this.pfmr.addStreamEventListener(this);
            
            // ���o�͏������J�n����D
            this.pfmr.open();		// StreamException
            this.pfmr.start();		// StreamException
            
            // SegmentIo ����o�̓p�����[�^���擾����D
            segIo = this.pfmr.getSegmentIo();
            List<IoParam> lsOutPrm = segIo.getOutputParamList();
            this.outParam = lsOutPrm.get(0);
        }
        catch (SystemException | StreamException ex) {
            close();
            throw ex;
        }
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD: StreamEventListener
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void evHn_IoStatus(IoStatusEvent ev)
    {
        // Not Implemented.
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void evHn_RendererStatus(RendererStatusEvent ev)
    {
        // Not Implemented.
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void evHn_MixerStatus(MixerStatusEvent ev)
    {
        // Not Implemented.
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void evHn_PerformerState(PerformerStateEvent ev)
    {
        // Not Implemented.
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void evHn_Segment(SegmentEvent ev)
    {
        Log.warning(ev.toString());
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void evHn_FlowUpdate(FlowUpdateEvent ev)
    {
        if (ev.getPerformerInfo().isActive() == false) {
            // ��M�X�g���[�����k�ނ����̂ŁC
            // �f�X�N�g�b�v�L���v�`���������I��������D
            this.server.stopControl();
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void evHn_StreamExceptionEvent(StreamExceptionEvent ev)
    {
        Log.error(ev.toString());
    }
}
