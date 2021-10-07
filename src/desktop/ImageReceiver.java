
package desktop;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.stream.RendererStatus;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.stream.StreamPerformer;
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
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.protocol.IoStatus;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: ImageReceiver 
 *
 * Date Modified: 2021.09.20
 *
 */

//==============================================================================
public class ImageReceiver
	implements	StreamEventListener
{
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final DesktopViewer viewer;
	
	private StreamPerformer pfmr = null;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: StreamEventListener
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
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//
	ImageReceiver(DesktopViewer viewer)
	{
		this.viewer = viewer;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	VideoCanvas createVideoCanvas(IoParam inPrm)
		throws	SystemException,
				StreamException
	{
		// SegmentIo �̓��o�͂�ݒ肷��D
		SegmentIo segIo = new SegmentIo();
		segIo.addIncomingStream(inPrm);
		segIo.configureDefaultRenderer();
		
		// Stream Performer �𐶐�����D
		this.pfmr = StreamPerformer.newInstance(segIo);
			// SystemException, StreamException
		
		// ���̃C���X�^���X�� StreamEventListener �Ƃ��ēo�^����D
		this.pfmr.addStreamEventListener(this);
		
		// VideoCanvas ���擾����D
		VideoCanvas vidCvs = this.pfmr.getVideoCanvas();
		
		// VideoCanvas ��Ԃ��D
		return vidCvs;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void start()
		throws	StreamException
	{
		try {
			// ���o�͏������J�n����D
			this.pfmr.open();
				// StreamException
			
			this.pfmr.start();
				// StreamException
		}
		catch (StreamException ex) {
			close();
			throw ex;
		}
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void close()
	{
		if (this.pfmr != null) {
			this.pfmr.removeStreamEventListener(this);
			this.pfmr.delete();
			this.pfmr = null;
		}
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: StreamEventListener
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void evHn_IoStatus(IoStatusEvent ev)
	{
		SwingUtilities.invokeLater(() -> {
			IoStatus stat = ev.getIoStatus();
			this.viewer.updateConnectionStatus(
				stat.getBitRate(),
				stat.getPacketLossRate()
			);
		});
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void evHn_RendererStatus(RendererStatusEvent ev)
	{
		SwingUtilities.invokeLater(() -> {
			RendererStatus stat = ev.getRendererStatus();
			this.viewer.updatePlayoutStatus(
				stat.getVidFrameRate()
			);
		});
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void evHn_MixerStatus(MixerStatusEvent ev)
	{
		// Not Implemented.
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void evHn_PerformerState(PerformerStateEvent ev)
	{
		// Not Implemented.
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void evHn_Segment(SegmentEvent ev)
	{
		Dialog.warning(this.viewer, ev.toString());
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void evHn_FlowUpdate(FlowUpdateEvent ev)
	{
		// Not Implemented.
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void evHn_StreamExceptionEvent(StreamExceptionEvent ev)
	{
		Dialog.error(this.viewer, ev.toString());
	}
}
