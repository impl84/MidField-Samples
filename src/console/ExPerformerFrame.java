
package console;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.midfield_system.api.stream.PerformerState;
import com.midfield_system.api.stream.RendererStatus;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.stream.event.IoStatusEvent;
import com.midfield_system.api.stream.event.PerformerStateEvent;
import com.midfield_system.api.stream.event.RendererStatusEvent;
import com.midfield_system.api.stream.event.StreamEvent;
import com.midfield_system.api.stream.event.StreamEventListener;
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.gui.misc.MessagePanel;
import com.midfield_system.protocol.IoStatus;

import util.AppUtilities;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: ExPerformerFrame
 *
 * Date Modified: 2021.08.20
 *
 */

//==============================================================================
@SuppressWarnings("serial")
public class ExPerformerFrame
	extends		JFrame
	implements	StreamEventListener,
				WindowListener
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final Dimension DIM_MIN_FRAME = new Dimension(400, 360);
	private static final Dimension DIM_PREF_FRAME = new Dimension(960, 480);
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================

	//- PRIVATE VARIABLE -------------------------------------------------------
	private StreamPerformer performer = null;
	
	private MessagePanel mpStreamEvent = null;
	private MessagePanel mpRendererStatus = null;
	private MessagePanel mpIoStatus = null;
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: StreamEventListener
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: StreamEventListener
	//
	@Override
	public void update(StreamEvent ev)
	{
		if (ev instanceof RendererStatusEvent) {
			// �Đ��\���X�e�[�^�X��\������D
			RendererStatus status = ((RendererStatusEvent)ev).getRendererStatus();
			this.mpRendererStatus.println(status.toString());
		}
		else if (ev instanceof IoStatusEvent) {
			// ���o�̓X�e�[�^�X��\������D
			IoStatus status = ((IoStatusEvent)ev).getIoStatus();
			this.mpIoStatus.println(status.toString());
		}
		else if (ev instanceof PerformerStateEvent) {
			// StreamPerformer �̏�ԏ���\������D
			this.mpStreamEvent.println(ev.toString());
			
			// StreamPerformer �̏�Ԃ� FINAL �֑J�ڂ��Ă���ꍇ�́C
			// ���� ExPerformerFrame ���I������D
			PerformerState curStat = ((PerformerStateEvent)ev).getCurrentState();
			if (curStat == PerformerState.FINAL) {
				SwingUtilities.invokeLater(() -> dispose());
			}
		}
		else {
			// ��L�ȊO�� StreamEvent ��\������D
			this.mpStreamEvent.println(ev.toString());
		}
	}
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: WindowListener
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: WindowListener
	//
	@Override
	public void windowActivated(WindowEvent ev) { }
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: WindowListener
	//
	@Override
	public void windowClosed(WindowEvent ev) { }
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: WindowListener
	//
	@Override
	public void windowClosing(WindowEvent ev)
	{
		if (this.performer != null) {
			this.performer.removeStreamEventListener(this);
			this.performer.delete();
			this.performer = null;
		}
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: WindowListener
	//
	@Override
	public void windowDeactivated(WindowEvent ev) { }
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: WindowListener
	//
	@Override
	public void windowDeiconified(WindowEvent ev) { }
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: WindowListener
	//
	@Override
	public void windowIconified(WindowEvent ev) { }
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: WindowListener
	//
	@Override
	public void windowOpened(WindowEvent ev) { }
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------

	//- PACKAGE METHOD ---------------------------------------------------------
	//
	ExPerformerFrame(StreamPerformer stm)
	{
		// GUI ���Z�b�g�A�b�v����D
		setupGui(stm);
		
		// �^����ꂽ StreamPerformer ��ێ����C
		// ���̃C���X�^���X�� StreamEventListener �Ƃ��Ēǉ�����D
		this.performer = stm;
		this.performer.addStreamEventListener(this);
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	void setupGui(StreamPerformer stm)
	{
		// JSplitPane ���E�̃p�l���𐶐�����D
		JPanel pnlLeft = createLeftPane(stm);
		JComponent pnlRight = createRightPane();
		
		// JSplitPane �𐶐����CContentPane �ɔz�u����D
		JSplitPane splitPane = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight
		);
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(splitPane, BorderLayout.CENTER);
		
		// ���̃t���[���Ɋւ���e�평���ݒ������D
		setTitle("Experimental Viewer for Stream Performer");
		setPreferredSize(DIM_PREF_FRAME);
		setMinimumSize(DIM_MIN_FRAME);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		addNotify();
		pack();
		
		// JSplitPane �̃f�B�o�C�_��^���ɐݒ肷��D
		splitPane.setDividerLocation(0.5);
		
		// ���̃t���[�����X�N���[���̒����ɕ\������D
		AppUtilities.setLocationToCenter(this);
		setVisible(true);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private JPanel createLeftPane(StreamPerformer stm)
	{
		// VideoCanvas �z�u�p�� JPanel �𐶐�����D
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Video Canvas"));
		
		// �^����ꂽ StreamPerformer �� VideoCanvas �����̃p�l���ɒǉ�����D
		VideoCanvas vidCvs = stm.getVideoCanvas();
		if (vidCvs != null) {
			panel.add(vidCvs, BorderLayout.CENTER);
		}
		// VideoCanvas �z�u�p�� JPanel ��Ԃ��D
		return panel;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private JComponent createRightPane()
	{
		// StreamEvent �o�͗p�� MessagePanel �𐶐�����D
		this.mpStreamEvent = new MessagePanel();
		this.mpStreamEvent.setBorder(new TitledBorder("Stream Event"));
		
		// RendererStatusEvent �o�͗p�� MessagePanel �𐶐�����D
		this.mpRendererStatus = new MessagePanel();
		this.mpRendererStatus.setBorder(new TitledBorder("Renderer Status"));
		
		// IoStatusEvent �o�͗p�� MessagePanel �𐶐�����D
		this.mpIoStatus = new MessagePanel();
		this.mpIoStatus.setBorder(new TitledBorder("I/O Status"));
		
		// �e�R���|�[�l���g��z�u����D
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.add(this.mpStreamEvent);
		panel.add(this.mpRendererStatus);
		panel.add(this.mpIoStatus);
		
		// �e�R���|�[�l���g���z�u���ꂽ JPanel ��Ԃ��D
		return panel;
	}
}