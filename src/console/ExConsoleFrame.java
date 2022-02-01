
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

import com.midfield_system.api.system.event.ResourceStatusEvent;
import com.midfield_system.api.system.event.ScreenSaverStatusEvent;
import com.midfield_system.api.system.event.SystemEvent;
import com.midfield_system.api.system.event.SystemEventListener;
import com.midfield_system.api.util.LogPrinter;
import com.midfield_system.gui.misc.MessagePanel;

import util.AppUtilities;
import util.InputField;
import util.LineReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: ExConsoleFrame
 *
 * Date Modified: 2021.08.23
 *
 */
@SuppressWarnings("serial")
public class ExConsoleFrame
	extends		JFrame
	implements	SystemEventListener,
				WindowListener
{
	// - PRIVATE CONSTANT VALUE ------------------------------------------------
	private static final Dimension DIM_MIN_FRAME = new Dimension(800, 480);
	private static final Dimension DIM_PREF_FRAME = new Dimension(1080, 640);
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================
	
	// - PRIVATE VARIABLE ------------------------------------------------------
	private MessagePanel mpConsoleOut = null;
	private InputField ifConsoleIn = null;
	
	private MessagePanel mpSystemLog = null;
	private MessagePanel mpSystemEvent = null;
	private MessagePanel mpResourceStatus  = null;
	
// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: SystemEventListener
// -----------------------------------------------------------------------------

	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: SystemEventListener
	//
	@Override
	public void update(SystemEvent ev)
	{
		// MidField System �����Ŕ��������C�x���g�̏��� GUI ��ɕ\������D
		// ���̃��\�b�h�� MidField System �����ŗ��p���Ă���X���b�h����
		// �Ă΂�Ă���̂ŁCGUI ��ւ̕\���ɌW�鏈���� EDT �ɈϏ�����D
		SwingUtilities.invokeLater(() -> updateOnEdt(ev));
	}

// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: WindowListener
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: WindowListener
	//
	@Override
	public void windowActivated(WindowEvent ev) { }
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: WindowListener
	//
	@Override
	public void windowClosed(WindowEvent ev) { }
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: WindowListener
	//
	@Override
	public void windowClosing(WindowEvent ev)
	{
		if (this.ifConsoleIn != null) {
			this.ifConsoleIn.putString("9");
			this.ifConsoleIn.putString("9");
		}
	}
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: WindowListener
	//
	@Override
	public void windowDeactivated(WindowEvent ev) { }
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: WindowListener
	//
	@Override
	public void windowDeiconified(WindowEvent ev) { }
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: WindowListener
	//
	@Override
	public void windowIconified(WindowEvent ev) { }
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: WindowListener
	//
	@Override
	public void windowOpened(WindowEvent ev) { }
	
// -----------------------------------------------------------------------------
//  PACKAGE METHOD:
// -----------------------------------------------------------------------------

	// - PACKAGE METHOD --------------------------------------------------------
	//
	ExConsoleFrame()
	{
		setupGui();
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	LineReader getLineReader()
	{
		LineReader reader = this.ifConsoleIn;
		return reader;
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	LogPrinter getConsolePrinter()
	{
		LogPrinter printer = this.mpConsoleOut;
		return printer;
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	LogPrinter getSystemLogPrinter()
	{
		LogPrinter printer = this.mpSystemLog;
		return printer;
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void putCommandSequence(String commands)
	{
		int len = commands.length();
		for (int i = 0; i < len; i++) {
			String command = Character.toString(commands.charAt(i));
			this.ifConsoleIn.putString(command);
		}
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	void setupGui()
	{
		// ���̃t���[���S�̂� Look and Feel ��ݒ肷��D
		AppUtilities.setLookAndFeel(this);
		
		// JSplitPane ���E�̃p�l���𐶐�����D
		JComponent pnlLeft = createLeftPane();
		JComponent pnlRight = createRightPane();
		
		// JSplitPane �𐶐����CContentPane �ɔz�u����D
		JSplitPane splitPane = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight
		);
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(splitPane, BorderLayout.CENTER);
		
		// ���̃t���[���Ɋւ���e�평���ݒ������D
		setTitle("Experimental Console for MidField System");
		setPreferredSize(DIM_PREF_FRAME);
		setMinimumSize(DIM_MIN_FRAME);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		pack();
		
		// JSplitPane �̃f�B�o�C�_��^���ɐݒ肷��D
		splitPane.setDividerLocation(0.5);
		
		// ���̃t���[�����X�N���[���̒����ɕ\������D
		AppUtilities.setLocationToCenter(this);
		setVisible(true);
	}
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private JComponent createLeftPane()
	{
		// �R���\�[���o�͗p�� MessagePanel �𐶐�����D
		this.mpConsoleOut = new MessagePanel();
		this.mpConsoleOut.setBorder(new TitledBorder("Experimental Console"));
		
		// �R���\�[�����͗p�̃t�B�[���h�𐶐����C
		// MessagePanel �̉����ɔz�u����D
		this.ifConsoleIn = new InputField();
		this.mpConsoleOut.add(this.ifConsoleIn.getComponent(), BorderLayout.SOUTH);
		
		// �R���\�[���o�͗p�� MessagePanel ��Ԃ��D
		return this.mpConsoleOut;
	}
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private JComponent createRightPane()
	{
		// ���O�o�͗p�� MessagePanel �𐶐�����D
		this.mpSystemLog = new MessagePanel();
		this.mpSystemLog.setBorder(new TitledBorder("MidField System Log"));
		
		// SystemEvent �\���p�� MessagePanel �𐶐�����D
		this.mpSystemEvent = new MessagePanel();
		this.mpSystemEvent.setBorder(new TitledBorder("System Event"));
		
		// �������p�󋵂�\������ MessagePanel �𐶐�����D
		this.mpResourceStatus = new MessagePanel();
		this.mpResourceStatus.setBorder(new TitledBorder("Resource Status"));
		
		// �e�R���|�[�l���g��z�u����D
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.add(this.mpSystemLog);
		panel.add(this.mpSystemEvent);
		panel.add(this.mpResourceStatus);
		
		// �e�R���|�[�l���g���z�u���ꂽ JPanel ��Ԃ��D
		return panel;
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private void updateOnEdt(SystemEvent ev)
	{
		if (ev instanceof ResourceStatusEvent) {
			this.mpResourceStatus.println(ev);
		}
		else if (ev instanceof ScreenSaverStatusEvent) {
			;
		}
		else {
			this.mpSystemEvent.println(ev);
		}
	}
}